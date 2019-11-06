/**
 * File: JDBCDBWriter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.sink;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.conn.CacheConnectionSupplier;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.domain.DatabaseStructure;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.jdbc.generator.TableNameGenerator;
import com.leatop.bee.data.weaver.connector.jdbc.generator.TableNameGeneratorFactory;

/**
 * @author Dorsey
 *
 */
public class JDBCDBWriter {

	private static final Logger LOG = LoggerFactory.getLogger(JDBCDBWriter.class);
	private final JDBCSinkConfig config;
	private final Dialect dialect;
	private final DatabaseStructure dbStructure;
	final CacheConnectionSupplier connectionSupplier;
	final TableNameGenerator tableNameGenerator;

	public JDBCDBWriter(final JDBCSinkConfig config, final Dialect dialect,
			final DatabaseStructure dbStructure) {
		this.config = config;
		this.dialect = dialect;
		this.dbStructure = dbStructure;
		this.tableNameGenerator = TableNameGeneratorFactory.getGenerator(config.tableNameGeneratorClass, config);

		this.connectionSupplier = new CacheConnectionSupplier(dialect) {

			@Override
			protected void onConnect(final java.sql.Connection conn) throws SQLException {
				conn.setAutoCommit(false);
			}
		};
	}

	public void write(final Collection<SinkRecord> records, final String connectorName)
			throws SQLException {
		final Connection connection = connectionSupplier.getConnection();
		final Map<TableId, RecordBuffer> buffer = new HashMap<>();
		for (SinkRecord record : records) {
			final TableId tableId = fromTopic(record);
			LOG.debug("Add record to table: {}", record, tableId);
			RecordBuffer recordBuffer = buffer.get(tableId);
			if (recordBuffer == null) {
				recordBuffer = new RecordBuffer(tableId, config, dialect, dbStructure, connection);
				buffer.put(tableId, recordBuffer);
			}

			recordBuffer.add(record);
		}

		List<SinkRecord> flushed = null;
		for (Iterator<Entry<TableId, RecordBuffer>> iter = buffer.entrySet().iterator(); iter
				.hasNext();) {
			Entry<TableId, RecordBuffer> entry = iter.next();
			TableId tableId = entry.getKey();
			RecordBuffer recordBuffer = entry.getValue();
			LOG.debug("Flush record to table: {}", tableId);

			flushed = recordBuffer.flush();
			recordBuffer.close();
		}

		// commit the update.
		connection.commit();
	}

	private TableId fromTopic(final SinkRecord record) {
		final String tableName = tableNameGenerator.genTableName(record);
		if (tableName.isEmpty()) {
			throw new ConnectException("Destination table name is not specified");
		}

		return dialect.parseTableId(tableName);
	}

	public void closeQuietly() {
		connectionSupplier.close();
	}

	public CacheConnectionSupplier connectionSupplier() {
		return connectionSupplier;
	}

}
