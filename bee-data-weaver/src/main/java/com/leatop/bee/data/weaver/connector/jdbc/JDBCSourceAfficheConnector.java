/**
 * File: JDBCSourceAfficheConnector.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月16日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;
import org.apache.kafka.connect.util.ConnectorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.leatop.bee.data.weaver.connector.jdbc.conn.CacheConnectionSupplier;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialects;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableNameAndBatchNo;
import com.leatop.bee.data.weaver.connector.jdbc.source.AfficheSupervisorThread;
import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceAfficheTask;
import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceConnectorConfig;
import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceTaskConfig;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;
import com.leatop.bee.data.weaver.connector.utils.VersionUtils;

/**
 * Kinds of source connector, which retrieving data from source in
 * <tt>Affiche</tt> way.
 * 
 * @author DORSEy
 *
 */
public class JDBCSourceAfficheConnector extends SourceConnector {

	// ~~~ Fields
	// ==============================================================
	private static final Logger LOG = LoggerFactory.getLogger(JDBCSourceAfficheConnector.class);
	private static final long MAX_TIMEOUT = 10000L;

	private Map<String, String> configProps;
	private JDBCSourceConnectorConfig config;
	private CacheConnectionSupplier connectionSupplier;
	private Dialect dialect;
	private AfficheSupervisorThread supervisor;

	@Override
	public String version() {
		return VersionUtils.getVersion();
	}

	@Override
	public void start(final Map<String, String> props) {
		LOG.info("Starting JDBC Affiche source connector...");
		try {
			configProps = props;
			config = new JDBCSourceConnectorConfig(configProps);
		} catch (ConfigException e) {
			throw new ConnectException(
					"Couldn't start JDBCSourceAfficheConnector due to configuration error", e);
		}

		final String url = config.getString(JDBCSourceConnectorConfig.CONNECTION_URL_CONFIG);
		final int maxConnectionAttempts = config
				.getInt(JDBCSourceConnectorConfig.CONNECTION_ATTEMPTS_CONFIG);
		final long connectionRetryBackoff = config
				.getLong(JDBCSourceConnectorConfig.CONNECTION_BACKOFF_CONFIG);

		dialect = Dialects.findBestFor(url, config);
		connectionSupplier = new CacheConnectionSupplier(dialect, maxConnectionAttempts,
				connectionRetryBackoff);

		// verify whether connection can be established.
		verifyConnection(url);

		long retryBackoffs = config
				.getLong(JDBCSourceConnectorConfig.AFFICHE_TABLE_POLL_INTERVAL_IN_MS_CONFIG);
		final String afficheTableName = config
				.getString(JDBCSourceConnectorConfig.AFFICHE_TABLE_NAME_CONFIG);
		final long timeout = config.getLong(JDBCSourceConnectorConfig.TIMEOUT_IN_MS_CONFIG);
		if (Strings.isNullOrEmpty(afficheTableName)) {
			throw new ConfigException(
					"Config: " + JDBCSourceConnectorConfig.AFFICHE_TABLE_NAME_CONFIG + " missed");
		}

		// start a thread to monitor on affiche table
		// once it has records, retrieve them
		supervisor = new AfficheSupervisorThread(afficheTableName, dialect, connectionSupplier,
				context, retryBackoffs, timeout);
		supervisor.start();
	}

	/**
	 * Verify that whether the connection to database specified by
	 * <code>url</code> can be established.
	 */
	private void verifyConnection(final String url) {
		try {
			connectionSupplier.getConnection();
		} catch (SQLException e) {
			LOG.error("Failed to build connection to url: {}", url);
			throw new ConnectException("Failed to build connection to url: " + url, e);
		}
	}

	@Override
	public Class<? extends Task> taskClass() {
		return JDBCSourceAfficheTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(final int maxTasks) {
		final List<TableNameAndBatchNo> tableNameAndBatchNos = supervisor.tableNameAndBatchNos();
		final int numGroups = Math.min(tableNameAndBatchNos.size(), maxTasks);
		final List<List<TableNameAndBatchNo>> tableNameAndBatchNoGroups = ConnectorUtils
				.groupPartitions(tableNameAndBatchNos, numGroups);
		final List<Map<String, String>> taskConfigs = new ArrayList<Map<String, String>>();

		for (List<TableNameAndBatchNo> taskTnAndBn : tableNameAndBatchNoGroups) {
			Map<String, String> taskConfig = new HashMap<String, String>(configProps);

			SQLBuilder builder = dialect.sqlBuilder();
			builder.listBuilder().delimitedBy(",").of(taskTnAndBn);
			taskConfig.put(JDBCSourceTaskConfig.TABLE_NAME_AND_BATCH_NOS_CONFIG,
					builder.toString());

			taskConfigs.add(taskConfig);
		}

		LOG.trace("Task configs: {}, table name and batch numbers: {}", taskConfigs,
				tableNameAndBatchNos.toArray(new TableNameAndBatchNo[tableNameAndBatchNos.size()]));

		return taskConfigs;
	}

	@Override
	public void stop() {
		LOG.info("Stopping Affiche supervisor thread...");
		supervisor.shutdown();
		try {
			supervisor.join(MAX_TIMEOUT);
		} catch (InterruptedException ignore) {
			// ignore this exception
		} finally {
			if (connectionSupplier != null) {
				try {
					connectionSupplier.close();
				} finally {
					connectionSupplier = null;

					try {
						if (dialect != null) {
							dialect.close();
						}
					} catch (Throwable t) {
						LOG.warn("Failed to close dialect {}", dialect, t);
					} finally {
						// let GC
						dialect = null;
					}
				}
			}
		}
	}

	@Override
	public ConfigDef config() {
		return JDBCSourceConnectorConfig.CONFIG_DEF;
	}

}
