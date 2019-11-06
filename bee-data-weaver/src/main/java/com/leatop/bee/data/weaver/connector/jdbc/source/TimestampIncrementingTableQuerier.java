/**
 * File: TimestampIncrementingTableQuerier.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月12日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.domain.Column;
import com.leatop.bee.data.weaver.connector.jdbc.domain.ColumnId;
import com.leatop.bee.data.weaver.connector.jdbc.source.SchemaMapping.FieldSetter;
import com.leatop.bee.data.weaver.connector.jdbc.source.TimestampIncrementingCriteria.CriteriaValues;
import com.leatop.bee.data.weaver.connector.utils.DateTimeUtils;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;

/**
 * An implementation of {@link TableQuerier}, which querying records based on
 * <tt>timestamp</tt> column or <tt>increment</tt> column, or combined.
 * 
 */
public class TimestampIncrementingTableQuerier extends TableQuerier implements CriteriaValues {
	private static final Logger log = LoggerFactory
			.getLogger(TimestampIncrementingTableQuerier.class);

	private final List<String> timestampColumnNames;
	private final List<ColumnId> timestampColumns;
	private String incrementingColumnName;
	private long timestampDelay;
	private TimestampIncrementingOffset offset;
	private TimestampIncrementingCriteria criteria;
	private final Map<String, String> partition;
	private final String topic;
	private final TimeZone timeZone;

	public TimestampIncrementingTableQuerier(final Dialect dialect, final QueryMode mode,
			final String name, final JDBCSourceTaskConfig config,
			final Map<String, String> originals,
			final List<String> timestampColumnNames, final String incrementingColumnName,
			final Map<String, Object> offsetMap, final Long timestampDelay,
			final TimeZone timeZone) {
		super(dialect, mode, name, config, originals);
		this.incrementingColumnName = incrementingColumnName;
		this.timestampColumnNames = timestampColumnNames != null ? timestampColumnNames
				: Collections.<String> emptyList();
		this.timestampDelay = timestampDelay;
		this.offset = TimestampIncrementingOffset.fromMap(offsetMap);

		this.timestampColumns = new ArrayList<>();
		for (String timestampColumn : this.timestampColumnNames) {
			if (timestampColumn != null && !timestampColumn.isEmpty()) {
				timestampColumns.add(new ColumnId(tableId, timestampColumn));
			}
		}

		String tableName = tableId.getTableName();
		topic = topicPrefix + tableName;// backward compatible
		partition = OffsetProtocols.sourcePartitionForProtocolV1(tableId);

		this.timeZone = timeZone;
	}

	@Override
	protected void createPreparedStatement(final Connection db) throws SQLException {
		findDefaultAutoIncrementingColumn(db);

		ColumnId incrementingColumn = null;
		if (incrementingColumnName != null && !incrementingColumnName.isEmpty()) {
			incrementingColumn = new ColumnId(tableId, incrementingColumnName);
		}

		SQLBuilder builder = dialect.sqlBuilder();
		builder.append("SELECT * FROM ");
		builder.append(tableId);

		criteria = new TimestampIncrementingCriteria(incrementingColumn, timestampColumns,
				timeZone);
		criteria.whereClause(builder);

		String queryString = builder.toString();
		recordQuery(queryString);
		log.debug("{} prepared SQL query: {}", this, queryString);
		stmt = dialect.createPreparedStatement(db, queryString);
	}

	private void findDefaultAutoIncrementingColumn(final Connection db) throws SQLException {
		if (incrementingColumnName != null && incrementingColumnName.isEmpty()) {
			// Find the first auto-incremented column ...
			for (Column defn : dialect.describeColumns(db, tableId.catelogName(),
					tableId.getSchemaName(), tableId.getTableName(), null).values()) {
				if (defn.isAutoIncrement()) {
					incrementingColumnName = defn.getColumnId().getColumnName();
					break;
				}
			}
		}

		if (incrementingColumnName != null && incrementingColumnName.isEmpty()) {
			log.debug("Falling back to describe '{}' table by querying {}", tableId, db);
			for (Column defn : dialect.describeColumnsByQuerying(db, tableId).values()) {
				if (defn.isAutoIncrement()) {
					incrementingColumnName = defn.getColumnId().getColumnName();
					break;
				}
			}
		}
	}

	@Override
	protected ResultSet executeQuery() throws SQLException {
		criteria.setQueryParameters(stmt, this);
		return stmt.executeQuery();
	}

	@Override
	public SourceRecord extractRecord() throws SQLException {
		Struct record = new Struct(schemaMapping.schema());
		for (FieldSetter setter : schemaMapping.fieldSetters()) {
			try {
				setter.setField(record, resultSet);
			} catch (IOException e) {
				log.warn("Ignoring record because processing failed:", e);
			} catch (SQLException e) {
				log.warn("Ignoring record due to SQL error:", e);
			}
		}

		offset = criteria.extractValues(schemaMapping.schema(), record, offset);
		return new SourceRecord(partition, offset.toMap(), topic, record.schema(), record);
	}

	@Override
	public Timestamp beginTimetampValue() {
		return offset.getTimestampOffset();
	}

	@Override
	public Timestamp endTimetampValue() throws SQLException {
		final long currentDbTime = dialect.currentDBTimestamp(stmt.getConnection(),
				DateTimeUtils.getTimeZoneCalendar(timeZone)).getTime();

		return new Timestamp(currentDbTime - timestampDelay);
	}

	@Override
	public Long lastIncrementedValue() {
		return offset.getIncrementingOffset();
	}

	@Override
	public String toString() {
		return "TimestampIncrementingTableQuerier{" + "table=" + tableId + ", query='" + query
				+ '\'' + ", topicPrefix='" + topicPrefix + '\'' + ", incrementingColumn='"
				+ (incrementingColumnName != null ? incrementingColumnName : "") + '\''
				+ ", timestampColumns=" + timestampColumnNames + '}';
	}
}
