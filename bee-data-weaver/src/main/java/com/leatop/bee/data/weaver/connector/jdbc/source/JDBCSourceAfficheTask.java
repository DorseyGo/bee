/**
 * File: JDBCSourceAfficheTask.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月16日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.utils.SystemTime;
import org.apache.kafka.common.utils.Time;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.leatop.bee.data.weaver.connector.jdbc.conn.CacheConnectionSupplier;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialects;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableNameAndBatchNo;
import com.leatop.bee.data.weaver.connector.jdbc.source.TableQuerier.QueryMode;
import com.leatop.bee.data.weaver.connector.jdbc.source.querier.AfficheTableQuerier;
import com.leatop.bee.data.weaver.connector.utils.VersionUtils;

/**
 * The task to push records to KAFKA cluster.
 * 
 * @author DORSEy
 *
 */
public class JDBCSourceAfficheTask extends SourceTask {

	private static final Logger LOG = LoggerFactory.getLogger(JDBCSourceAfficheTask.class);

	// ~~~ Fields
	// ========================================================
	private JDBCSourceTaskConfig config;
	private Dialect dialect;
	private CacheConnectionSupplier connectionSupplier;
	private PriorityQueue<TableQuerier> querierQueue = new PriorityQueue<TableQuerier>();
	private final Map<String, PriorityQueue<TableQuerier>> dependants = new HashMap<String, PriorityQueue<TableQuerier>>();
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final Time time;
	private List<String> prevTableNameAndBatchNoStrs;
	private String relationTabName;
	private String afficheTableName;

	public JDBCSourceAfficheTask() {
		time = new SystemTime();
	}

	@Override
	public String version() {
		return VersionUtils.getVersion();
	}

	@Override
	public void start(final Map<String, String> props) {
		LOG.info("Starting JDBC Affiche source  Task... ");
		try {
			config = new JDBCSourceTaskConfig(props);
			prevTableNameAndBatchNoStrs = new ArrayList<String>();
		} catch (ConfigException e) {
			throw new ConnectException("Failed to start JDBCSourceAfficheTask due to config error",
					e);
		}

		final String url = config.getString(JDBCSourceConnectorConfig.CONNECTION_URL_CONFIG);
		final int maxConnAttempts = config
				.getInt(JDBCSourceConnectorConfig.CONNECTION_ATTEMPTS_CONFIG);
		final long retryBackoff = config
				.getLong(JDBCSourceConnectorConfig.CONNECTION_BACKOFF_CONFIG);

		initDialect(url);

		LOG.info("Using JDBC dialect: {}", dialect.name());

		connectionSupplier = new CacheConnectionSupplier(dialect, maxConnAttempts, retryBackoff);
		relationTabName = config.getString(JDBCSourceTaskConfig.TABLE_RELATION_NAME_CONFIG);
		afficheTableName = config.getString(JDBCSourceTaskConfig.AFFICHE_TABLE_NAME_CONFIG);
		queueQuerier();

		running.set(true);
		LOG.info("Starting JDBC Affiche source task...");
	}

	/**
	 * Queue the querier. If the querier has sub-queries, queue it as well.
	 */
	private void queueQuerier() {
		final List<String> tableNameAndBatchNoStrs = config
				.getList(JDBCSourceTaskConfig.TABLE_NAME_AND_BATCH_NOS_CONFIG);
		if (prevTableNameAndBatchNoStrs.equals(tableNameAndBatchNoStrs)) {
			return;
		}

		prevTableNameAndBatchNoStrs = tableNameAndBatchNoStrs;
		final List<TableNameAndBatchNo> tableNameAndBatchNos = parse(tableNameAndBatchNoStrs);

		String batchNo = null;
		String tableName = null;
		// initialize the querier
		for (TableNameAndBatchNo tableNameAndBatchNo : tableNameAndBatchNos) {
			tableName = tableNameAndBatchNo.getTableName();
			batchNo = tableNameAndBatchNo.getBatchNo();

			querierQueue.add(new AfficheTableQuerier(dialect, QueryMode.TABLE, tableName, batchNo,
					config, new HashMap<String, String>()));

			try {
				List<String> detailTableNames = dialect.detailTableNames(
						connectionSupplier.getConnection(), dialect.parseTableId(relationTabName),
						tableName);

				PriorityQueue<TableQuerier> dependantQuerierQueue = dependants.get(batchNo);
				if (dependantQuerierQueue == null) {
					dependantQuerierQueue = new PriorityQueue<TableQuerier>();
					dependants.put(batchNo, dependantQuerierQueue);
				}

				if (detailTableNames != null && !detailTableNames.isEmpty()) {
					for (String detailTableName : detailTableNames) {
						dependantQuerierQueue.add(new AfficheTableQuerier(dialect, QueryMode.TABLE,
								detailTableName, batchNo, config, new HashMap<String, String>()));
					}
				}
			} catch (SQLException e) {
				LOG.warn("Failed to retrieve detail table names of {}", tableName, e);
			}
		}
	}

	/**
	 * Decode the string representation.
	 * 
	 * @param tableNameAndBatchNoStrs
	 * @return
	 */
	private List<TableNameAndBatchNo> parse(final List<String> tableNameAndBatchNoStrs) {
		List<TableNameAndBatchNo> tableNameAndBatchNos = new ArrayList<TableNameAndBatchNo>();
		Splitter splitter = Splitter.on(TableNameAndBatchNo.DELIM_COLON);

		TableNameAndBatchNo tableNameAndBatchNo = null;
		for (String tableNameAndBatchNoStr : tableNameAndBatchNoStrs) {
			Iterator<String> iter = splitter.split(tableNameAndBatchNoStr).iterator();
			String tableName = iter.next();
			String batchNo = iter.next();
			tableNameAndBatchNo = new TableNameAndBatchNo(tableName, batchNo);

			tableNameAndBatchNos.add(tableNameAndBatchNo);
		}

		return tableNameAndBatchNos;
	}

	private void initDialect(final String url) {
		final String dialectName = config.getString(JDBCSourceConnectorConfig.DIALECT_NAME_CONFIG);
		if (dialectName != null && !dialectName.trim().isEmpty()) {
			dialect = Dialects.create(dialectName, config);
		} else {
			dialect = Dialects.findBestFor(url, config);
		}
	}

	@Override
	public List<SourceRecord> poll() throws InterruptedException {
		LOG.trace("{} polling for new records");
		final int batchMaxRows = config.getInt(JDBCSourceTaskConfig.BATCH_MAX_ROWS_CONFIG);
		Throwable cause = null;

		while (running.get()) {
			final TableQuerier querier = querierQueue.peek();
			if (querier == null) {
				continue;
			}

			final String batchNo = ((AfficheTableQuerier) querier).batchNo();
			PriorityQueue<TableQuerier> depandantQuerierQueue = dependants.get(batchNo);

			final List<SourceRecord> records = new ArrayList<SourceRecord>();
			if (depandantQuerierQueue != null && !depandantQuerierQueue.isEmpty()) {
				while (!depandantQuerierQueue.isEmpty()) {
					TableQuerier dependantQuerier = depandantQuerierQueue.peek();
					if (!dependantQuerier.querying()) {
						final long nextUpdate = querier.getLastUpdate()
								+ config.getLong(JDBCSourceTaskConfig.POLL_INTERVAL_MS_CONFIG);
						final long untilNext = (nextUpdate - time.milliseconds());

						if (untilNext > 0) {
							time.sleep(untilNext);
							continue;
						}
					}

					loadDependantRecords(dependantQuerier, batchMaxRows, records);
				}
			}

			// otherwise, no dependents
			if (!querier.querying()) {
				final long nextUpdate = querier.getLastUpdate()
						+ config.getLong(JDBCSourceTaskConfig.POLL_INTERVAL_MS_CONFIG);
				final long untilNext = (nextUpdate - time.milliseconds());

				if (untilNext > 0) {
					time.sleep(untilNext);
					continue;
				}
			}

			try {
				LOG.debug("Checking for next block of results from {}", querier.toString());
				querier.maybeStartQuery(connectionSupplier.getConnection());

				boolean hasNext = true;
				while ((records.size() < batchMaxRows) && (hasNext = querier.next())) {
					records.add(querier.extractRecord());
				}

				if (!hasNext) {
					// under this circumstance, we have reason to believe
					// that
					// this querier can be omitted.
					drainAndAttemptToQuerying(querier);
				}

				LOG.debug("Returning {} records for {}", records.size(), querier.toString());
				return records;
			} catch (SQLException e) {
				LOG.error("Failed to run query for table {}: {}", querier.toString(), e);
				resetAndRequeueHead(querier);
				cause = e;
				return null;
			} catch (Throwable t) {
				resetAndRequeueHead(querier);
				cleanup();
				throw t;
			} finally {
				if (cause == null) {
					try {
						dialect.updateAfficheTable(connectionSupplier.getConnection(),
								dialect.parseTableId(afficheTableName), batchNo);
						connectionSupplier.getConnection().commit();
					} catch (SQLException e) {
						LOG.warn("Failed to update the affiche table, with batch no: {}", batchNo);
					}
				}
			}
		}

		// Only in case of shutdown
		final TableQuerier querier = querierQueue.peek();
		if (querier != null) {
			resetAndRequeueHead(querier);
		}

		cleanup();
		return null;
	}

	/**
	 * @param dependantQuerier
	 */
	private void loadDependantRecords(final TableQuerier dependantQuerier, final int batchMaxRows,
			final List<SourceRecord> records) {
		final String batchNo = ((AfficheTableQuerier) dependantQuerier).batchNo();
		try {
			dependantQuerier.maybeStartQuery(connectionSupplier.getConnection());

			boolean hasNext = true;
			while ((records.size() < batchMaxRows) && (hasNext = dependantQuerier.next())) {
				records.add(dependantQuerier.extractRecord());
			}

			if (!hasNext) {
				drainDependants(batchNo, dependantQuerier);
			}

			if (records.isEmpty()) {
				LOG.trace("No updates for {}", dependantQuerier.toString());
			}
		} catch (SQLException e) {
			LOG.error("Failed to run query for table {}: {}", dependantQuerier.toString(), e);
			resetAndRequeueDependants(batchNo, dependantQuerier);
		} catch (Throwable t) {
			resetAndRequeueDependants(batchNo, dependantQuerier);
			cleanup();
			throw t;
		}
	}

	/**
	 * @param batchNo
	 * @param dependantQuerier
	 */
	private void resetAndRequeueDependants(final String batchNo, final TableQuerier expected) {
		PriorityQueue<TableQuerier> dependantQuerierQueue = dependants.get(batchNo);
		assert dependantQuerierQueue != null && !dependantQuerierQueue.isEmpty();

		TableQuerier actual = dependantQuerierQueue.poll();
		assert expected == actual;
		expected.reset(time.milliseconds());
		dependantQuerierQueue.add(expected);
	}

	/**
	 * @param batchNo
	 */
	private void drainDependants(final String batchNo, final TableQuerier expected) {
		PriorityQueue<TableQuerier> dependantQuerierQueue = dependants.get(batchNo);
		assert dependantQuerierQueue != null && !dependantQuerierQueue.isEmpty();

		TableQuerier actual = dependantQuerierQueue.poll();
		assert expected == actual;
		actual.close();
	}

	/**
	 * Drain the querier, and release resources it holds.
	 * 
	 * @param querier
	 * @throws SQLException
	 */
	private void drainAndAttemptToQuerying(final TableQuerier expected) {
		TableQuerier actual = querierQueue.poll();
		assert actual == expected;
		expected.close();
	}

	/**
	 * clean up, release the resource it holds.
	 */
	private void cleanup() {
		LOG.info("Clean up for JDBC Affiche source task");
		try {
			if (connectionSupplier != null) {
				connectionSupplier.close();
			}
		} catch (Throwable cause) {
			LOG.warn("Error detected when closing the connections", cause);
		} finally {
			connectionSupplier = null;

			try {
				if (dialect != null) {
					dialect.close();
				}
			} catch (Throwable cause) {
				LOG.warn("Error detected when closing the dialect: {}", dialect.name(), cause);
			} finally {
				dialect = null;
			}
		}
	}

	/**
	 * @param querier
	 */
	private void resetAndRequeueHead(final TableQuerier expectedQuerier) {
		TableQuerier actualQuerier = querierQueue.poll();
		assert actualQuerier == expectedQuerier;

		expectedQuerier.reset(time.milliseconds());
		querierQueue.add(expectedQuerier);
	}

	@Override
	public void stop() {
		LOG.info("Stopping JDBC Affiche source task...");
		running.set(false);
	}

}
