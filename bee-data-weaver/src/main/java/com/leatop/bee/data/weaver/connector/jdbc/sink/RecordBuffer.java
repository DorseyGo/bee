/**
 * File: RecordBuffer.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月10日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.sink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.common.Pair;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.domain.ColumnId;
import com.leatop.bee.data.weaver.connector.jdbc.domain.DatabaseStructure;
import com.leatop.bee.data.weaver.connector.jdbc.domain.FieldMetadata;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.jdbc.generator.TimeBasedTableNameGenerator;

/**
 * @author Dorsey
 *
 */
public class RecordBuffer {

	private static final Logger LOG = LoggerFactory.getLogger(RecordBuffer.class);
	private final TableId tableId;
	private final JDBCSinkConfig config;
	private final Dialect dialect;
	private final DatabaseStructure structure;
	private final Connection connection;

	private List<SinkRecord> records = new ArrayList<>();
	private Pair<Schema, Schema> curSchemaPair;
	private FieldMetadata fieldMetadata;
	private PreparedStatement pstmt;
	private Dialect.StatementBinder binder;

	public RecordBuffer(final TableId tableId, final JDBCSinkConfig config, final Dialect dialect,
			final DatabaseStructure structure, final Connection connection) {
		this.tableId = tableId;
		this.config = config;
		this.dialect = dialect;
		this.structure = structure;
		this.connection = connection;
	}

	/**
	 * Add the record to the buffer, and when it exceeds the batch size, all
	 * records in buffer will be flushed to the destination database.
	 * 
	 * @param record
	 *            the record to be added.
	 * @return the records to be flushed to the destination database.
	 * @throws SQLException
	 *             if any SQL error detected.
	 * @author DORSEy modified on 2019/8/9
	 */
	public List<SinkRecord> add(final SinkRecord record) throws SQLException {
		final Pair<Schema, Schema> schemaPair = new Pair<Schema, Schema>(record.keySchema(),
				record.valueSchema());
		if (curSchemaPair == null) {
			curSchemaPair = schemaPair;
			fieldMetadata = FieldMetadata.extract(tableId.getTableName(), config.pkFields,
					schemaPair, config.fieldWhiteLists);
			final String sql = getInsertSQL(tableId);
			close();
			pstmt = connection.prepareStatement(sql);
			binder = dialect.statementBinder(pstmt, schemaPair, fieldMetadata);
		}

		final List<SinkRecord> flushed;
		if (curSchemaPair.equals(schemaPair)) {
			records.add(record);
			if (records.size() >= config.batchSize) {
				LOG.debug("Flushing records after exceeding configed batch size {}",
						config.batchSize);
				flushed = flush();
			} else {
				flushed = Collections.emptyList();
			}
		} else {
			LOG.debug("Flushing records due to unequal schema, current: {}, next: {}",
					curSchemaPair, schemaPair);
			flushed = flush();
			curSchemaPair = null;
			flushed.addAll(add(record));
		}

		return flushed;
	}

	private String getInsertSQL(final TableId tableId) {
		return this.dialect.buildInsertStatement(tableId, asColumns(fieldMetadata.keyFieldNames()),
				asColumns(fieldMetadata.nonKeyFieldNames()));
	}

	private Collection<ColumnId> asColumns(final Set<String> columnNames) {
		Collection<ColumnId> columnIds = new ArrayList<>();
		for (String columnName : columnNames) {
			final ColumnId columnId = new ColumnId(tableId, columnName);
			columnIds.add(columnId);
		}

		return columnIds;
	}

	public List<SinkRecord> flush() throws SQLException {
		if (records.isEmpty()) {
			LOG.info("Records is empty");
			return Collections.emptyList();
		}

		LOG.debug("Flusing {} records", records.size());
		for (SinkRecord record : records) {
			binder.bindRecord(record);
		}

		int totalAffects = 0;
		boolean successNoInfo = false;

		try {
			for (int affects : pstmt.executeBatch()) {
				if (affects == Statement.SUCCESS_NO_INFO) {
					successNoInfo = true;
					continue;
				}

				totalAffects += affects;
			}

			if (totalAffects != records.size() && !successNoInfo) {
				throw new ConnectException("total affects:" + totalAffects
						+ " sum up not equal to records size: " + records.size());
			}
		} catch (SQLException e) {
			// if exception detected, record it in exception table.
			LOG.warn("Failed to flush records to underlying database", e);
			final String excepTableName = determineExceptionTableName(tableId.getTableName());
			TableId excepTableId = new TableId(tableId.catelogName(), tableId.getSchemaName(),
					excepTableName);

			final String sql = getInsertSQL(excepTableId);
			close();
			binder = null;
			pstmt = connection.prepareStatement(sql);
			binder = dialect.statementBinder(pstmt, curSchemaPair, fieldMetadata);

			// bind the record
			for (SinkRecord record : records) {
				binder.bindRecord(record);
			}

			pstmt.executeBatch();
		}

		final List<SinkRecord> flushed = records;
		records = new ArrayList<>();
		return flushed;
	}

	/**
	 * @param tableName
	 * @return
	 */
	private String determineExceptionTableName(final String tableName) {
		if (config.tableNameGeneratorClass.getName()
				.equalsIgnoreCase(TimeBasedTableNameGenerator.class.getName())) {
			final String tableNameFormat = config.tableNameFormat;
			final int leftBraceDelimIndx = tableNameFormat
					.lastIndexOf(TimeBasedTableNameGenerator.LEFT_BRACE_DELIM);
			assert leftBraceDelimIndx >= 0 : "No left brace delimiter found";
			final String originalTableName = tableNameFormat.substring(0, leftBraceDelimIndx);

			return (originalTableName + config.exceptionTableNameSuffix);
		}

		return (tableName + config.exceptionTableNameSuffix);
	}

	public void close() throws SQLException {
		LOG.info("Closing the prepared statement");
		if (pstmt != null) {
			pstmt.close();
			pstmt = null;
		}
	}

}
