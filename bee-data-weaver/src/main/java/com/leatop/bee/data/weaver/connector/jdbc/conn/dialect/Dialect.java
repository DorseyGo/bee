/**
 * File: Dialect.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.conn.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;

import com.leatop.bee.common.Pair;
import com.leatop.bee.data.weaver.connector.jdbc.buzi.SendBroadEntity;
import com.leatop.bee.data.weaver.connector.jdbc.conn.ConnectionSupplier;
import com.leatop.bee.data.weaver.connector.jdbc.domain.Column;
import com.leatop.bee.data.weaver.connector.jdbc.domain.ColumnId;
import com.leatop.bee.data.weaver.connector.jdbc.domain.FieldMetadata;
import com.leatop.bee.data.weaver.connector.jdbc.domain.JDBCDriverInfo;
import com.leatop.bee.data.weaver.connector.jdbc.domain.Table;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableNameAndBatchNo;
import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceConnectorConfig.NumericMapping;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;

/**
 * @author DORSEy
 *
 */
public interface Dialect extends ConnectionSupplier {

	String name();

	PreparedStatement createPreparedStatement(final Connection connection, final String query)
			throws SQLException;

	/**
	 * Return the current time on the specific database.
	 * 
	 * @param connection
	 * @param calendar
	 * @return
	 * @throws SQLException
	 * @throws ConnectException
	 */
	Timestamp currentDBTimestamp(final Connection connection, final Calendar calendar)
			throws SQLException, ConnectException;

	/**
	 * Returns a list of table IDs existing in the specific database.
	 * 
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	List<TableId> tables(final Connection connection) throws SQLException;

	/**
	 * Returns true if the specific table, identified by <tt>tableId</tt>,
	 * exists in the specific database. Otherwise, a connect exception will be
	 * raised.
	 * 
	 * @param connection
	 * @param tableId
	 * @return
	 * @throws SQLException
	 * @throws ConnectException
	 */
	boolean tableExists(final Connection connection, final TableId tableId)
			throws SQLException, ConnectException;

	/**
	 * @param connection
	 * @param tableId
	 * @return
	 */
	Table describeTable(final Connection connection, final TableId tableId) throws SQLException;

	Map<ColumnId, Column> describeColumns(final Connection connection, final String tableName,
			final String columnPattern) throws SQLException;

	Map<ColumnId, Column> describeColumns(final Connection connection, final String catelogPattern,
			final String schemaPattern, final String tableName, final String columnPattern)
			throws SQLException;

	Map<ColumnId, Column> describeColumns(ResultSetMetaData rsMetadata) throws SQLException;

	/**
	 * Returns the sql statement for insertion.
	 * 
	 * @param table
	 * @param keyColumns
	 * @param nonKeyColumns
	 * @return
	 */
	String buildInsertStatement(TableId table, Collection<ColumnId> keyColumns,
			Collection<ColumnId> nonKeyColumns);

	TableId parseTableId(String fqn);

	StatementBinder statementBinder(final PreparedStatement pstmt,
			final Pair<Schema, Schema> schemaPair, final FieldMetadata fieldMetadata)
			throws SQLException;

	/**
	 * @param pstmt
	 * @param index
	 * @param schema
	 * @param object
	 */
	void bindField(PreparedStatement pstmt, int index, Schema schema, Object object)
			throws SQLException;

	Map<ColumnId, Column> describeColumnsByQuerying(Connection connection, TableId tableId)
			throws SQLException;

	/**
	 * Returns the specified {@link TimeZone}.
	 * 
	 * @return
	 */
	TimeZone getTimeZone();

	public JDBCDriverInfo getJdbcDriverInfo();

	public NumericMapping getNumericMapping();

	/**
	 * Use the supplied {@link SchemaBuilder} to add a field that corresponds to
	 * the column with the specified definition.
	 *
	 * @param column
	 *            the definition of the column; may not be null
	 * @param builder
	 *            the schema builder; may not be null
	 * @return the name of the field, or null if no field was added
	 */
	String addFieldToSchema(Column column, SchemaBuilder builder);

	/**
	 * Get a new {@link SQLBuilder} that can be used to build the SQL statement
	 * with quoted identifiers.
	 * 
	 * @return
	 */
	SQLBuilder sqlBuilder();

	/**
	 * @param tableId
	 * @return
	 */
	List<TableNameAndBatchNo> tableNameAndBatchNos(final Connection conn, final TableId tableId)
			throws SQLException;

	List<String> detailTableNames(final Connection conn, final TableId tableId,
			final String parentTableName) throws SQLException;

	interface StatementBinder {

		void bindRecord(final SinkRecord record) throws SQLException;
	}

	/**
	 * @param connection
	 * @param afficheTableName
	 * @param batchNo
	 */
	void updateAfficheTable(final Connection connection, final TableId afficheTableId,
			final String batchNo) throws SQLException;

	/**
	 * @param conn
	 * @param announcement
	 * @param entity
	 */
	void addAnnouncement(Connection conn, String announcement, SendBroadEntity entity)
			throws SQLException;

}
