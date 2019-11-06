/**
 * File: JDBCSourceConnector.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;
import org.apache.kafka.connect.util.ConnectorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.conn.CacheConnectionSupplier;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialects;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceConnectorConfig;
import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceTask;
import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceTaskConfig;
import com.leatop.bee.data.weaver.connector.jdbc.source.TableMonitorThread;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;
import com.leatop.bee.data.weaver.connector.utils.VersionUtils;

/**
 * Source connector, which takes the database as data source from which the data
 * is crawled.
 * 
 * @description
 */
public class JDBCSourceConnector extends SourceConnector {

	private static final Logger log = LoggerFactory.getLogger(JDBCSourceConnector.class);

	private static final long MAX_TIMEOUT = 10000L;

	private Map<String, String> configProperties;
	private JDBCSourceConnectorConfig config;
	private CacheConnectionSupplier cachedConnectionProvider;
	private TableMonitorThread tableMonitorThread;
	private Dialect dialect;

	@Override
	public String version() {
		return VersionUtils.getVersion();
	}

	@Override
	public void start(final Map<String, String> properties) throws ConnectException {
		log.info("Starting JDBC Source Connector");
		try {
			configProperties = properties;
			config = new JDBCSourceConnectorConfig(configProperties);
		} catch (ConfigException e) {
			throw new ConnectException("Couldn't start JdbcSourceConnector due to configuration error", e);
		}

		final String dbUrl = config.getString(JDBCSourceConnectorConfig.CONNECTION_URL_CONFIG);
		final int maxConnectionAttempts = config.getInt(JDBCSourceConnectorConfig.CONNECTION_ATTEMPTS_CONFIG);
		final long connectionRetryBackoff = config.getLong(JDBCSourceConnectorConfig.CONNECTION_BACKOFF_CONFIG);
		dialect = Dialects.findBestFor(dbUrl, config);
		cachedConnectionProvider = new CacheConnectionSupplier(dialect, maxConnectionAttempts, connectionRetryBackoff);

		// Initial connection attempt
		try {
			cachedConnectionProvider.getConnection();
		} catch (SQLException e) {
			log.error("Failed to init connection to {}", dbUrl, e);
			throw new ConnectException("Failed to init connection to " + dbUrl);
		}

		long tablePollMs = config.getLong(JDBCSourceConnectorConfig.TABLE_POLL_INTERVAL_MS_CONFIG);
		List<String> whitelist = config.getList(JDBCSourceConnectorConfig.TABLE_WHITELIST_CONFIG);
		Set<String> whitelistSet = whitelist.isEmpty() ? null : new HashSet<>(whitelist);
		List<String> blacklist = config.getList(JDBCSourceConnectorConfig.TABLE_BLACKLIST_CONFIG);
		Set<String> blacklistSet = blacklist.isEmpty() ? null : new HashSet<>(blacklist);

		if (whitelistSet != null && blacklistSet != null) {
			throw new ConnectException(JDBCSourceConnectorConfig.TABLE_WHITELIST_CONFIG + " and "
					+ JDBCSourceConnectorConfig.TABLE_BLACKLIST_CONFIG + " are " + "exclusive.");
		}

		tableMonitorThread = new TableMonitorThread(dialect, cachedConnectionProvider, context, tablePollMs,
				whitelistSet, blacklistSet);
		tableMonitorThread.start();
	}

	@Override
	public Class<? extends Task> taskClass() {
		return JDBCSourceTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(final int maxTasks) {
		// synchronize with the source database.
		List<TableId> currentTables = tableMonitorThread.tables();
		int numGroups = Math.min(currentTables.size(), maxTasks);
		List<List<TableId>> tablesGrouped = ConnectorUtils.groupPartitions(currentTables, numGroups);
		List<Map<String, String>> taskConfigs = new ArrayList<>(tablesGrouped.size());
		for (List<TableId> taskTables : tablesGrouped) {
			Map<String, String> taskProps = new HashMap<>(configProperties);
			SQLBuilder builder = SQLBuilder.create();
			builder.listBuilder().delimitedBy(",").of(taskTables);
			taskProps.put(JDBCSourceTaskConfig.TABLES_CONFIG, builder.toString());
			taskConfigs.add(taskProps);
		}

		log.trace("Task configs with query: {}, tables: {}", taskConfigs, currentTables.toArray());
		return taskConfigs;
	}

	@Override
	public void stop() throws ConnectException {
		log.info("Stopping table monitoring thread");
		tableMonitorThread.shutdown();
		try {
			tableMonitorThread.join(MAX_TIMEOUT);
		} catch (InterruptedException e) {
			// Ignore, shouldn't be interrupted
		} finally {
			try {
				cachedConnectionProvider.close();
			} finally {
				try {
					if (dialect != null) {
						dialect.close();
					}
				} catch (Throwable t) {
					log.warn("Error while closing the {} dialect: ", dialect, t);
				} finally {
					dialect = null;
				}
			}
		}
	}

	@Override
	public ConfigDef config() {
		return JDBCSourceConnectorConfig.CONFIG_DEF;
	}
}
