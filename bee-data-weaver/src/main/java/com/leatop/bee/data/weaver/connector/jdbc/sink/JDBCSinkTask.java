/**
 * File: JDBCSinkTask.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.sink;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.leatop.bee.data.weaver.connector.jdbc.buzi.SendBroadEntity;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialects;
import com.leatop.bee.data.weaver.connector.jdbc.domain.DatabaseStructure;
import com.leatop.bee.data.weaver.connector.utils.VersionUtils;

/**
 * @author Dorsey
 *
 */
public class JDBCSinkTask extends SinkTask {

	private static final Logger LOG = LoggerFactory.getLogger(JDBCSinkTask.class);
	private static final String NAME_STR = "name";
	private static final String UNKNOWN = "UNKNOWN";
	private Dialect dialect;
	private JDBCSinkConfig config;
	private int remainingRetries;
	private JDBCDBWriter writer;
	private String connectorName;

	@Override
	public String version() {
		return VersionUtils.getVersion();
	}

	@Override
	public void flush(final Map<TopicPartition, OffsetAndMetadata> arg0) {
		// not necessary.
	}

	@Override
	public void put(final Collection<SinkRecord> records) {
		if (records.isEmpty()) {
			// no records pulled from Kafka right now.
			return;
		}

		final SinkRecord first = records.iterator().next();
		final int numRecords = records.size();
		LOG.debug(
				"Received {} records. First record kafka coordinates:({}-{}-{}). Writing them to the database...",
				numRecords, first.topic(), first.kafkaPartition(), first.kafkaOffset());

		try {
			writer.write(records, connectorName);
			genAfficheRecord(first, records.size());
		} catch (SQLException e) {
			if (remainingRetries == 0) {
				throw new ConnectException(e);
			} else {
				writer.closeQuietly();
				initWriter();
				remainingRetries--;
				context.timeout(config.retryBackoffMs);
				throw new RetriableException(e);
			}
		}
	}

	/**
	 * @param size
	 */
	private void genAfficheRecord(final SinkRecord record, final int numRecords)
			throws SQLException {
		final String announcement = config.getString(JDBCSinkConfig.ANNOUNCEMENT_TABLE_NAME_CONFIG);
		if (!Strings.isNullOrEmpty(announcement)) {
			final Connection conn = writer.connectionSupplier().getConnection();
			SendBroadEntity entity = new SendBroadEntity(
					writer.tableNameGenerator.genTableName(record),
					String.valueOf(new Date().getTime()), numRecords);
			dialect.addAnnouncement(conn, announcement, entity);
			conn.commit();
		}
	}

	@Override
	public void start(final Map<String, String> props) {
		LOG.info("Starting JDBC Sink task via using config: {}", props);
		config = new JDBCSinkConfig(props);
		connectorName = props.get(NAME_STR);
		connectorName = (connectorName == null) ? UNKNOWN : connectorName;

		remainingRetries = config.maxRetries;
		initWriter();
	}

	/**
	 * Initialize the {@link JDBCDBWriter}.
	 */
	void initWriter() {
		if (config.dialectName != null && !config.dialectName.trim().isEmpty()) {
			dialect = Dialects.create(config.dialectName, config);
		} else {
			dialect = Dialects.findBestFor(config.connectionUrl, config);
		}

		DatabaseStructure dbStructure = new DatabaseStructure(dialect);
		writer = new JDBCDBWriter(config, dialect, dbStructure);
	}

	@Override
	public void stop() {
		LOG.info("Attempt to stoping task...");
		try {
			writer.closeQuietly();
		} finally {
			try {
				if (dialect != null) {
					dialect.close();
				}
			} catch (Throwable cause) {
				LOG.warn("Failed to close dialect {}", dialect);
			} finally {
				dialect = null;
			}
		}
	}

}
