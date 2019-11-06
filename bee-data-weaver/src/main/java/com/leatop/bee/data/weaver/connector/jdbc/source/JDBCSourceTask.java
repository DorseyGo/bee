/**
 * File: JdbcSourceTask.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月12日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.utils.SystemTime;
import org.apache.kafka.common.utils.Time;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.conn.CacheConnectionSupplier;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialects;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.utils.VersionUtils;

/**
 * @description JDBC的source抽取任务，继承了sourceTask
 */
public class JDBCSourceTask extends SourceTask {

	private static final Logger log = LoggerFactory.getLogger(JDBCSourceTask.class);
	private Time time;
	private JDBCSourceTaskConfig config;
	private Dialect dialect;
	private CacheConnectionSupplier cachedConnectionProvider;
	private PriorityQueue<TableQuerier> tableQueue = new PriorityQueue<TableQuerier>();
	private final AtomicBoolean running = new AtomicBoolean(false);

	public JDBCSourceTask() {
		this.time = new SystemTime();
	}

	public JDBCSourceTask(final Time time) {
		this.time = time;
	}

	@Override
	public String version() {
		return VersionUtils.getVersion();
	}

	@Override
	public void start(final Map<String, String> properties) {
		log.info("Starting JDBC source task");
		try {
			config = new JDBCSourceTaskConfig(properties);
		} catch (ConfigException e) {
			throw new ConnectException("Couldn't start JdbcSourceTask due to configuration error",
					e);
		}

		final String url = config.getString(JDBCSourceConnectorConfig.CONNECTION_URL_CONFIG);
		final int maxConnAttempts = config
				.getInt(JDBCSourceConnectorConfig.CONNECTION_ATTEMPTS_CONFIG);
		final long retryBackoff = config
				.getLong(JDBCSourceConnectorConfig.CONNECTION_BACKOFF_CONFIG);

		final String dialectName = config.getString(JDBCSourceConnectorConfig.DIALECT_NAME_CONFIG);
		if (dialectName != null && !dialectName.trim().isEmpty()) {
			dialect = Dialects.create(dialectName, config);
		} else {
			dialect = Dialects.findBestFor(url, config);
		}

		log.info("Using JDBC dialect {}", dialect.name());

		cachedConnectionProvider = new CacheConnectionSupplier(dialect, maxConnAttempts,
				retryBackoff);

		List<String> tables = config.getList(JDBCSourceTaskConfig.TABLES_CONFIG);
		String query = config.getString(JDBCSourceTaskConfig.QUERY_CONFIG);
		if ((tables.isEmpty() && query.isEmpty()) || (!tables.isEmpty() && !query.isEmpty())) {
			throw new ConnectException("Invalid configuration: each JdbcSourceTask must have at "
					+ "least one table assigned to it or one query specified");
		}

		TableQuerier.QueryMode queryMode = (!query.isEmpty()) ? TableQuerier.QueryMode.QUERY
				: TableQuerier.QueryMode.TABLE;
		List<String> tablesOrQuery = (queryMode == TableQuerier.QueryMode.QUERY)
				? Collections.singletonList(query)
				: tables;

		String mode = config.getString(JDBCSourceTaskConfig.MODE_CONFIG);
		if (mode == null || mode.equals(JDBCSourceTaskConfig.MODE_UNSPECIFIED)) {
			throw new ConfigException("Query mode must be specified");
		}

		// used only in table mode
		Map<String, List<Map<String, String>>> partitionsByTableFqn = new HashMap<>();
		Map<Map<String, String>, Map<String, Object>> offsets = null;
		if (mode.equals(JDBCSourceTaskConfig.MODE_INCREMENTING)
				|| mode.equals(JDBCSourceTaskConfig.MODE_TIMESTAMP)
				|| mode.equals(JDBCSourceTaskConfig.MODE_TIMESTAMP_INCREMENTING)) {
			List<Map<String, String>> partitions = new ArrayList<>(tables.size());
			log.trace("Starting in TABLE mode");
			for (String table : tables) {
				List<Map<String, String>> tablePartitions = possibleTablePartitions(table);
				partitions.addAll(tablePartitions);
				partitionsByTableFqn.put(table, tablePartitions);
			}
			// 从上下文中获取到offset值
			offsets = context.offsetStorageReader().offsets(partitions);
			log.trace("The partition offsets are {}", offsets);
		}

		String incrementingColumn = config
				.getString(JDBCSourceTaskConfig.INCREMENTING_COLUMN_NAME_CONFIG);
		List<String> timestampColumns = config
				.getList(JDBCSourceTaskConfig.TIMESTAMP_COLUMN_NAME_CONFIG);
		Long timestampDelayInterval = config
				.getLong(JDBCSourceTaskConfig.TIMESTAMP_DELAY_INTERVAL_MS_CONFIG);
		TimeZone timeZone = config.timeZone();

		for (String tableOrQuery : tablesOrQuery) {
			final List<Map<String, String>> tablePartitionsToCheck;
			tablePartitionsToCheck = partitionsByTableFqn.get(tableOrQuery);
			Map<String, Object> offset = null;
			if (offsets != null) {
				for (Map<String, String> toCheckPartition : tablePartitionsToCheck) {
					offset = offsets.get(toCheckPartition);
					if (offset != null) {
						log.info("Found offset {} for partition {}", offsets, toCheckPartition);
						break;
					}
				}
			}

			if (mode.equals(JDBCSourceTaskConfig.MODE_INCREMENTING)) {
				tableQueue.add(new TimestampIncrementingTableQuerier(dialect, queryMode,
						tableOrQuery, config, properties, null, incrementingColumn, offset,
						timestampDelayInterval, timeZone));
			} else if (mode.equals(JDBCSourceTaskConfig.MODE_TIMESTAMP)) {
				tableQueue.add(new TimestampIncrementingTableQuerier(dialect, queryMode,
						tableOrQuery, config, properties, timestampColumns, null, offset,
						timestampDelayInterval, timeZone));
			} else if (mode.endsWith(JDBCSourceTaskConfig.MODE_TIMESTAMP_INCREMENTING)) {
				tableQueue.add(new TimestampIncrementingTableQuerier(dialect, queryMode,
						tableOrQuery, config, properties, timestampColumns, incrementingColumn,
						offset, timestampDelayInterval, timeZone));
			}
		}

		running.set(true);
		log.info("Started JDBC source task");
	}

	private List<Map<String, String>> possibleTablePartitions(final String table) {
		TableId tableId = dialect.parseTableId(table);
		return Arrays.asList(OffsetProtocols.sourcePartitionForProtocolV1(tableId),
				OffsetProtocols.sourcePartitionForProtocolV0(tableId));
	}

	@Override
	public void stop() throws ConnectException {
		log.info("Stopping JDBC source task");
		running.set(false);
	}

	protected void closeResources() {
		log.info("Closing resources for JDBC source task");
		try {
			if (cachedConnectionProvider != null) {
				cachedConnectionProvider.close();
			}
		} catch (Throwable t) {
			log.warn("Error while closing the connections", t);
		} finally {
			cachedConnectionProvider = null;
			try {
				if (dialect != null) {
					dialect.close();
				}
			} catch (Throwable t) {
				log.warn("Error while closing the {} dialect: ", dialect.name(), t);
			} finally {
				dialect = null;
			}
		}
	}

	@Override
	public List<SourceRecord> poll() throws InterruptedException {
		log.trace("{} Polling for new data");

		while (running.get()) {
			final TableQuerier querier = tableQueue.peek();
			if (!querier.querying()) {
				final long nextUpdate = querier.getLastUpdate()
						+ config.getLong(JDBCSourceTaskConfig.POLL_INTERVAL_MS_CONFIG);
				final long untilNext = nextUpdate - time.milliseconds();
				if (untilNext > 0) {
					log.trace("Waiting {} ms to poll {} next", untilNext, querier.toString());
					time.sleep(untilNext);
					continue;
				}
			}

			long startTime = System.currentTimeMillis();
			final List<SourceRecord> results = new ArrayList<>();
			try {
				log.debug("Checking for next block of results from {}", querier.toString());
				querier.maybeStartQuery(cachedConnectionProvider.getConnection());

				int batchMaxRows = config.getInt(JDBCSourceTaskConfig.BATCH_MAX_ROWS_CONFIG);
				boolean hadNext = true;
				while (results.size() < batchMaxRows && (hadNext = querier.next())) {
					results.add(querier.extractRecord());
				}

				if (!hadNext) {
					resetAndRequeueHead(querier);
				}

				if (results.isEmpty()) {
					log.trace("No updates for {}", querier.toString());
					continue;
				}

				long endTime = System.currentTimeMillis();
				log.info("Extract records {}, from {}, end in {}, cost {}ms", results.size(),
						startTime, endTime, (endTime - startTime));

				return results;
			} catch (SQLException sqle) {
				log.error("Failed to run query for table {}: {}", querier.toString(), sqle);
				resetAndRequeueHead(querier);
				return null;
			} catch (Throwable t) {
				resetAndRequeueHead(querier);
				closeResources();
				throw t;
			}
		}

		// Only in case of shutdown
		final TableQuerier querier = tableQueue.peek();
		if (querier != null) {
			resetAndRequeueHead(querier);
		}

		closeResources();
		return null;
	}

	private void resetAndRequeueHead(final TableQuerier expectedHead) {
		log.debug("Resetting querier {}", expectedHead.toString());
		TableQuerier removedQuerier = tableQueue.poll();
		assert removedQuerier == expectedHead;
		expectedHead.reset(time.milliseconds());
		tableQueue.add(expectedHead);
	}

}
