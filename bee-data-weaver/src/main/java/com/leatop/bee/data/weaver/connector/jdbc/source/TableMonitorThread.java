/**
 * File: TableMonitorThread.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月12日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.kafka.connect.connector.ConnectorContext;
import org.apache.kafka.connect.errors.ConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.conn.ConnectionSupplier;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.utils.QuoteMethod;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;

/**
 * @description
 */
public class TableMonitorThread extends Thread {
	private static final Logger log = LoggerFactory.getLogger(TableMonitorThread.class);

	private final Dialect dialect;
	private final ConnectionSupplier connectionProvider;
	private final ConnectorContext context;
	private final CountDownLatch shutdownLatch;
	private final long pollMs;
	private Set<String> whitelist;
	private Set<String> blacklist;
	private List<TableId> tables;
	private Map<String, List<TableId>> duplicates;

	public TableMonitorThread(final Dialect dialect, final ConnectionSupplier connectionProvider,
			final ConnectorContext context, final long pollMs, final Set<String> whitelist,
			final Set<String> blacklist) {
		this.dialect = dialect;
		this.connectionProvider = connectionProvider;
		this.context = context;
		this.shutdownLatch = new CountDownLatch(1);
		this.pollMs = pollMs;
		this.whitelist = whitelist;
		this.blacklist = blacklist;
		this.tables = null;

	}

	@Override
	public void run() {
		log.info("Starting thread to monitor tables.");
		while (shutdownLatch.getCount() > 0) {
			try {
				if (updateTables()) {
					context.requestTaskReconfiguration();
				}
			} catch (Exception e) {
				context.raiseError(e);
				throw e;
			}

			try {
				log.debug("Waiting {} ms to check for changed.", pollMs);
				boolean shuttingDown = shutdownLatch.await(pollMs, TimeUnit.MILLISECONDS);
				if (shuttingDown) {
					return;
				}
			} catch (InterruptedException e) {
				log.error("Unexpected InterruptedException, ignoring: ", e);
			}
		}
	}

	public synchronized List<TableId> tables() {
		final long timeout = 10000L;
		long started = System.currentTimeMillis();
		long now = started;
		while (tables == null && now - started < timeout) {
			try {
				wait(timeout - (now - started));
			} catch (InterruptedException e) {
				log.info(e.getMessage(), e);
			}
			now = System.currentTimeMillis();
		}
		if (tables == null) {
			throw new ConnectException("Tables could not be updated quickly enough.");
		}
		if (!duplicates.isEmpty()) {
			String configText;
			if (whitelist != null) {
				configText = "'" + JDBCSourceConnectorConfig.TABLE_WHITELIST_CONFIG + "'";
			} else if (blacklist != null) {
				configText = "'" + JDBCSourceConnectorConfig.TABLE_BLACKLIST_CONFIG + "'";
			} else {
				configText = "'" + JDBCSourceConnectorConfig.TABLE_WHITELIST_CONFIG + "' or '"
						+ JDBCSourceConnectorConfig.TABLE_BLACKLIST_CONFIG + "'";
			}
			String msg = "The connector uses the unqualified table name as the topic name and has "
					+ "detected duplicate unqualified table names. This could lead to mixed data types in "
					+ "the topic and downstream processing errors. To prevent such processing errors, the "
					+ "JDBC Source connector fails to start when it detects duplicate table name "
					+ "configurations. Update the connector's " + configText
					+ " config to include exactly "
					+ "one table in each of the tables listed below.\n\t";
			throw new ConnectException(msg + duplicates.values());
		}
		return tables;
	}

	public void shutdown() {
		log.info("Shutting down thread monitoring tables.");
		shutdownLatch.countDown();
	}

	private synchronized boolean updateTables() {
		final List<TableId> tables;
		try {
			tables = dialect.tables(connectionProvider.getConnection());
			log.debug("Got the following tables: " + Arrays.toString(tables.toArray()));
		} catch (SQLException e) {
			log.error(
					"Error while trying to get updated table list, ignoring and waiting for next table poll"
							+ " interval",
					e);
			connectionProvider.close();
			return false;
		}

		final List<TableId> filteredTables = new ArrayList<>(tables.size());

		if (whitelist != null) {
			for (TableId table : tables) {
				String fqn1 = SQLBuilder.create().append(table, QuoteMethod.NEVER).toString();
				String fqn2 = SQLBuilder.create().append(table, QuoteMethod.ALWAYS).toString();
				if (whitelist.contains(fqn1) || whitelist.contains(fqn2)
						|| whitelist.contains(table.getTableName())) {
					filteredTables.add(table);
				}
			}
		} else if (blacklist != null) {
			for (TableId table : tables) {
				String fqn1 = SQLBuilder.create().append(table, QuoteMethod.NEVER).toString();
				String fqn2 = SQLBuilder.create().append(table, QuoteMethod.ALWAYS).toString();
				if (!(blacklist.contains(fqn1) || blacklist.contains(fqn2)
						|| blacklist.contains(table.getTableName()))) {
					filteredTables.add(table);
				}
			}
		} else {
			filteredTables.addAll(tables);
		}

		if (!filteredTables.equals(this.tables)) {
			log.info("After filtering the tables are: {}",
					// 2019-07-11 修改
					SQLBuilder.create().listBuilder().delimitedBy(",").of(filteredTables));
			Map<String, List<TableId>> duplicates = filteredTables.stream()
					.collect(Collectors.groupingBy(TableId::getTableName))// 2019-07-11
																			// 修改
					.entrySet().stream().filter(entry -> entry.getValue().size() > 1)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			this.duplicates = duplicates;
			List<TableId> previousTables = this.tables;
			this.tables = filteredTables;
			notifyAll();
			return previousTables != null;
		}
		return false;
	}
}
