/**
 * File: TimestampIncrementingCriteria.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月12日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.ConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.domain.ColumnId;
import com.leatop.bee.data.weaver.connector.utils.DateTimeUtils;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;

/**
 * @description
 */
public class TimestampIncrementingCriteria {

	public interface CriteriaValues {

		/**
		 * @description 开始时间戳
		 * @return
		 * @throws SQLException
		 */
		Timestamp beginTimetampValue() throws SQLException;

		/**
		 * @description 结束时间戳
		 * @return
		 * @throws SQLException
		 */
		Timestamp endTimetampValue() throws SQLException;

		/**
		 * @description 最后时间戳
		 * @return
		 * @throws SQLException
		 */
		Long lastIncrementedValue() throws SQLException;
	}

	protected static final BigDecimal LONG_MAX_VALUE_AS_BIGDEC = new BigDecimal(Long.MAX_VALUE);

	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected final List<ColumnId> timestampColumns;
	protected final ColumnId incrementingColumn;
	protected final TimeZone timeZone;

	public TimestampIncrementingCriteria(final ColumnId incrementingColumn,
			final List<ColumnId> timestampColumns, final TimeZone timeZone) {
		this.timestampColumns = timestampColumns != null ? timestampColumns
				: Collections.<ColumnId> emptyList();
		this.incrementingColumn = incrementingColumn;
		this.timeZone = timeZone;
	}

	protected boolean hasTimestampColumns() {
		return !timestampColumns.isEmpty();
	}

	protected boolean hasIncrementedColumn() {
		return incrementingColumn != null;
	}

	/**
	 * @description 组装where条件语句
	 * @param builder
	 */
	public void whereClause(final SQLBuilder builder) {
		if (hasTimestampColumns() && hasIncrementedColumn()) {
			timestampIncrementingWhereClause(builder);
		} else if (hasTimestampColumns()) {
			timestampWhereClause(builder);
		} else if (hasIncrementedColumn()) {
			incrementingWhereClause(builder);
		}
	}

	/**
	 * @description 设置参数入口
	 * @param stmt
	 * @param values
	 * @throws SQLException
	 */
	public void setQueryParameters(final PreparedStatement stmt, final CriteriaValues values)
			throws SQLException {
		if (hasTimestampColumns() && hasIncrementedColumn()) {
			setQueryParametersTimestampIncrementing(stmt, values);
		} else if (hasTimestampColumns()) {
			setQueryParametersTimestamp(stmt, values);
		} else if (hasIncrementedColumn()) {
			setQueryParametersIncrementing(stmt, values);
		}
	}

	/**
	 * @description 设置时间戳和增量的参数
	 * @param stmt
	 * @param values
	 * @throws SQLException
	 */
	protected void setQueryParametersTimestampIncrementing(final PreparedStatement stmt,
			final CriteriaValues values) throws SQLException {
		Timestamp beginTime = values.beginTimetampValue();
		Timestamp endTime = values.endTimetampValue();
		Long incOffset = values.lastIncrementedValue();
		stmt.setTimestamp(1, endTime, DateTimeUtils.getTimeZoneCalendar(timeZone));
		stmt.setTimestamp(2, beginTime, DateTimeUtils.getTimeZoneCalendar(timeZone));
		stmt.setLong(3, incOffset);
		stmt.setTimestamp(4, beginTime, DateTimeUtils.getTimeZoneCalendar(timeZone));
		log.debug(
				"Executing prepared statement with start time value = {} end time = {} and incrementing"
						+ " value = {}",
				DateTimeUtils.formatTimestamp(beginTime, timeZone),
				DateTimeUtils.formatTimestamp(endTime, timeZone), incOffset);
	}

	/**
	 * @description 设置增量的参数
	 * @param stmt
	 * @param values
	 * @throws SQLException
	 */
	protected void setQueryParametersIncrementing(final PreparedStatement stmt,
			final CriteriaValues values) throws SQLException {
		Long incOffset = values.lastIncrementedValue();
		stmt.setLong(1, incOffset);
		log.debug("Executing prepared statement with incrementing value = {}", incOffset);
	}

	protected void setQueryParametersTimestamp(final PreparedStatement stmt,
			final CriteriaValues values) throws SQLException {
		Timestamp beginTime = values.beginTimetampValue();
		Timestamp endTime = values.endTimetampValue();
		stmt.setTimestamp(1, beginTime, DateTimeUtils.getTimeZoneCalendar(timeZone));
		stmt.setTimestamp(2, endTime, DateTimeUtils.getTimeZoneCalendar(timeZone));
		log.debug("Executing prepared statement with timestamp value = {} end time = {}",
				DateTimeUtils.formatTimestamp(beginTime, timeZone),
				DateTimeUtils.formatTimestamp(endTime, timeZone));
	}

	/**
	 * @description 取值
	 * @param schema
	 * @param record
	 * @param previousOffset
	 * @return
	 */
	public TimestampIncrementingOffset extractValues(final Schema schema, final Struct record,
			final TimestampIncrementingOffset previousOffset) {
		Timestamp extractedTimestamp = null;
		if (hasTimestampColumns()) {
			extractedTimestamp = extractOffsetTimestamp(schema, record);
			assert previousOffset == null || (previousOffset.getTimestampOffset() != null
					&& previousOffset.getTimestampOffset().compareTo(extractedTimestamp) <= 0);
		}

		Long extractedId = null;
		if (hasIncrementedColumn()) {
			extractedId = extractOffsetIncrementedId(schema, record);
			assert previousOffset == null || previousOffset.getIncrementingOffset() == -1L
					|| extractedId > previousOffset.getIncrementingOffset()
					|| hasTimestampColumns();
		}

		return new TimestampIncrementingOffset(extractedTimestamp, extractedId);
	}

	/**
	 * @description
	 * @param schema
	 * @param record
	 * @return
	 */
	protected Timestamp extractOffsetTimestamp(final Schema schema, final Struct record) {
		for (ColumnId timestampColumn : timestampColumns) {
			Timestamp ts = (Timestamp) record.get(timestampColumn.getColumnName());
			if (ts != null) {
				return ts;
			}
		}

		return null;
	}

	/**
	 * @description
	 * @param schema
	 * @param record
	 * @return
	 */
	protected Long extractOffsetIncrementedId(final Schema schema, final Struct record) {
		final Object incrementingColumnValue = record.get(incrementingColumn.getColumnName());
		final Long extractedId = ((Number) incrementingColumnValue).longValue();
		log.trace("Extracted incrementing column value: {}", extractedId);

		return extractedId;
	}

	protected Long extractDecimalId(final Object incrementingColumnValue) {
		final BigDecimal decimal = ((BigDecimal) incrementingColumnValue);
		if (decimal.compareTo(LONG_MAX_VALUE_AS_BIGDEC) > 0) {
			throw new ConnectException(
					"Decimal value for incrementing column exceeded Long.MAX_VALUE");
		}

		return decimal.longValue();
	}

	protected boolean isIntegralPrimitiveType(final Object incrementingColumnValue) {
		return incrementingColumnValue instanceof Long || incrementingColumnValue instanceof Integer
				|| incrementingColumnValue instanceof Short
				|| incrementingColumnValue instanceof Byte;
	}

	protected String coalesceTimestampColumns(final SQLBuilder builder) {
		if (timestampColumns.size() == 1) {
			builder.append(timestampColumns.get(0).getColumnName());
		} else {
			builder.append("COALESCE(");
			builder.listBuilder().delimitedBy(",").of(timestampColumns);
			builder.append(")");
		}

		return builder.toString();
	}

	protected void timestampIncrementingWhereClause(final SQLBuilder builder) {

		builder.append(" WHERE ");
		coalesceTimestampColumns(builder);
		builder.append(" < ? AND ((");
		coalesceTimestampColumns(builder);
		builder.append(" = ? AND ");
		builder.append(incrementingColumn.getColumnName());
		builder.append(" > ?");
		builder.append(") OR ");
		coalesceTimestampColumns(builder);
		builder.append(" > ?)");
		builder.append(" ORDER BY ");
		coalesceTimestampColumns(builder);
		builder.append(",");
		builder.append(incrementingColumn.getColumnName());
		builder.append(" ASC");
	}

	protected void incrementingWhereClause(final SQLBuilder builder) {
		builder.append(" WHERE ");
		builder.append(incrementingColumn.getColumnName());
		builder.append(" > ?");
		builder.append(" ORDER BY ");
		builder.append(incrementingColumn.getColumnName());
		builder.append(" ASC");
	}

	protected void timestampWhereClause(final SQLBuilder builder) {
		builder.append(" WHERE ");
		coalesceTimestampColumns(builder);
		builder.append(" > ? AND ");
		coalesceTimestampColumns(builder);
		builder.append(" < ? ORDER BY ");
		coalesceTimestampColumns(builder);
		builder.append(" ASC");
	}

}
