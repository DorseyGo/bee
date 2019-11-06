/**
 * File: GenericDialect.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.conn.dialect;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.data.Date;
import org.apache.kafka.connect.data.Decimal;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Time;
import org.apache.kafka.connect.errors.ConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.common.Pair;
import com.leatop.bee.common.utils.StringUtils;
import com.leatop.bee.data.weaver.connector.jdbc.buzi.SendBroadEntity;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.DialectSupplier.FixedScoreDialectSupplier;
import com.leatop.bee.data.weaver.connector.jdbc.domain.Column;
import com.leatop.bee.data.weaver.connector.jdbc.domain.Column.Mutability;
import com.leatop.bee.data.weaver.connector.jdbc.domain.Column.Nullability;
import com.leatop.bee.data.weaver.connector.jdbc.domain.ColumnId;
import com.leatop.bee.data.weaver.connector.jdbc.domain.FieldMetadata;
import com.leatop.bee.data.weaver.connector.jdbc.domain.JDBCDriverInfo;
import com.leatop.bee.data.weaver.connector.jdbc.domain.PreparedStatementBinder;
import com.leatop.bee.data.weaver.connector.jdbc.domain.Table;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableNameAndBatchNo;
import com.leatop.bee.data.weaver.connector.jdbc.sink.JDBCSinkConfig;
import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceConnectorConfig;
import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceConnectorConfig.NumericMapping;
import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceTaskConfig;
import com.leatop.bee.data.weaver.connector.utils.Identifier;
import com.leatop.bee.data.weaver.connector.utils.QuoteMethod;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;

/**
 * An implementation of {@link Dialect}, which provides the common operations.
 * 
 * @author DORSEy
 *
 */
public class GenericDialect implements Dialect {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected static final int NUMERIC_TYPE_SCALE_LOW = -84;
	protected static final int NUMERIC_TYPE_SCALE_HIGH = 127;
	protected static final int NUMERIC_TYPE_SCALE_UNSET = -127;

	public static class Supplier extends FixedScoreDialectSupplier {

		protected Supplier(final String name, final int score) {
			super(GenericDialect.class.getName(), DialectSupplier.AVERAGE_MATCH_SCORE);
		}

		@Override
		public Dialect create(final AbstractConfig config) {
			return new GenericDialect(config);
		}

	}

	// ~~~ fields
	// =========================================================
	private static final Logger LOG = LoggerFactory.getLogger(GenericDialect.class);
	protected final NumericMapping mapNumerics;
	protected AbstractConfig config;
	protected Queue<Connection> connections = new ConcurrentLinkedQueue<>();
	protected final Identifier identifier;
	private final QuoteMethod quoteSqlIdentifier;
	protected final String jdbcUrl;
	protected String catalogPattern;
	protected final String schemaPattern;
	private final TimeZone timeZone;

	/**
	 * table types can be "TABLE", "VIEW", etc.
	 */
	protected final Set<String> tableTypes;
	protected JDBCDriverInfo jdbcDriverInfo;

	// ~~~ constructors
	// =========================================================

	/**
	 * Constructor of {@link GenericDialect}, with config specified.
	 * 
	 * @param config
	 *            the config.
	 */
	public GenericDialect(final AbstractConfig config) {
		this(config, Identifier.DEFAULT_IDENTIFIER);
	}

	/**
	 * Constructor of {@link GenericDialect}, with configuration specified.
	 */
	public GenericDialect(final AbstractConfig config, final Identifier identifier) {
		this.config = config;
		this.jdbcUrl = config.getString(JDBCSinkConfig.CONNECTION_URL);
		this.identifier = identifier;
		if (config instanceof JDBCSinkConfig) {
			this.catalogPattern = JDBCSinkConfig.CATELOG_PATTERN_DEFAULT;
			this.schemaPattern = JDBCSinkConfig.SCHEMA_PATTERN_DEFAULT;
			this.tableTypes = new HashSet<>(Arrays.asList(JDBCSinkConfig.TABLE_TYPES_DEFAULT));
			this.quoteSqlIdentifier = QuoteMethod
					.get(config.getString(JDBCSinkConfig.QUOTE_SQL_IDENTIFIERS_CONFIG));
			// this.identifier = identifier;
		} else {
			this.catalogPattern = config.getString(JDBCSourceTaskConfig.CATALOG_PATTERN_CONFIG);
			this.schemaPattern = config.getString(JDBCSourceTaskConfig.SCHEMA_PATTERN_CONFIG);
			this.tableTypes = new HashSet<>(config.getList(JDBCSourceTaskConfig.TABLE_TYPE_CONFIG));
			this.quoteSqlIdentifier = QuoteMethod
					.get(config.getString(JDBCSourceConnectorConfig.QUOTE_SQL_IDENTIFIERS_CONFIG));
			// this.identifier = new Identifier(".", "\"", "\"");
		}
		if (config instanceof JDBCSourceConnectorConfig) {
			mapNumerics = ((JDBCSourceConnectorConfig) config).numericMapping();
		} else {
			mapNumerics = NumericMapping.NONE;
		}

		if (config instanceof JDBCSourceConnectorConfig) {
			timeZone = ((JDBCSourceConnectorConfig) config).timeZone();
		} else if (config instanceof JDBCSinkConfig) {
			timeZone = ((JDBCSinkConfig) config).timeZone;
		} else {
			timeZone = TimeZone.getTimeZone(ZoneOffset.ofTotalSeconds(8 * 60 * 60));
		}
	}

	// ~~~ methods
	// =========================================================

	@Override
	public Connection getConnection() throws SQLException {
		final String username = config.getString(JDBCSinkConfig.CONNECTION_USER);
		final String password = config
				.getPassword(JDBCSourceConnectorConfig.CONNECTION_PASSWORD_CONFIG).value();

		final Properties props = new Properties();
		if (username != null) {
			props.setProperty(Constants.USER, username);
		}

		if (password != null) {
			props.setProperty(Constants.PASSWORD, password);
		}

		Connection connection = DriverManager.getConnection(jdbcUrl, props);
		if (jdbcDriverInfo == null) {
			jdbcDriverInfo = createJDBCDriverInfo(connection);
		}

		connections.add(connection);
		return connection;
	}

	/**
	 * @param connection
	 * @return
	 */
	protected JDBCDriverInfo createJDBCDriverInfo(final Connection connection) throws SQLException {
		final DatabaseMetaData metadata = connection.getMetaData();

		return new JDBCDriverInfo(metadata.getDriverMajorVersion(),
				metadata.getDriverMinorVersion(), metadata.getDriverName(),
				metadata.getDatabaseProductName(), metadata.getDatabaseProductVersion());
	}

	@Override
	public void close() {
		Connection connection;
		while ((connection = connections.poll()) != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOG.warn("Ignore error in closing connection to {}", jdbcDriverInfo, e);
			}
		}
	}

	@Override
	public boolean isConnectionValid(final Connection connection, final int timeout)
			throws SQLException {
		if (jdbcDriverInfo().majorVersion() >= Constants.VERSION_THRESHOLD) {
			return connection.isValid(timeout);
		}

		final String sql = verifyConnectionQuery();
		if (sql != null) {
			try (Statement stmt = connection.createStatement()) {
				if (stmt.execute(sql)) {
					try (ResultSet rs = stmt.getResultSet()) {
						// no-op.
					}
				}
			}
		}

		return true;
	}

	/**
	 * Returns jdbc driver information based on the current connection.
	 * 
	 * @return
	 */
	protected JDBCDriverInfo jdbcDriverInfo() {
		if (jdbcDriverInfo == null) {
			try (Connection connection = getConnection()) {
				jdbcDriverInfo = createJDBCDriverInfo(connection);
			} catch (SQLException e) {
				throw new ConnectException("Unable to get JDBC driver information", e);
			}
		}

		return jdbcDriverInfo;
	}

	/**
	 * Returns a sql for verifying whether the connection is valid.
	 * 
	 * @return a sql for verifying whether the connection is valid.
	 */
	protected String verifyConnectionQuery() {
		return "SELECT 1";
	}

	@Override
	public String name() {
		return getClass().getSimpleName().replace(Constants.DIALECT_STRING, Constants.EMPTY_STRING);
	}

	@Override
	public PreparedStatement createPreparedStatement(final Connection connection,
			final String query) throws SQLException {
		LOG.trace("Creating a prepared statement {}", query);
		final PreparedStatement pstmt = connection.prepareStatement(query);
		configPreparedStatement(pstmt);

		return pstmt;
	}

	/**
	 * Perform any operations on {@link PreparedStatement} before it is used.
	 * 
	 * @param pstmt
	 */
	protected void configPreparedStatement(final PreparedStatement pstmt) {
		// sub-classes can customize this.
	}

	@Override
	public Timestamp currentDBTimestamp(final Connection connection, final Calendar calendar)
			throws SQLException, ConnectException {
		final String sql = currentTimestampQuery();
		Objects.requireNonNull(sql, "SQL to query current timestamp MUST not be null");
		StringUtils.requiresNonEmpty(sql, "SQL to query the current timestamp MUST not be empty");

		try (final Statement stmt = connection.createStatement()) {
			LOG.debug("Executing query {} to get current timestamp", sql);
			try (final ResultSet rs = stmt.executeQuery(sql)) {
				if (rs.next()) {
					return rs.getTimestamp(1, calendar);
				}

				throw new ConnectException(
						"Unable to get current timestamp from db by issue query: " + sql);
			}
		} catch (SQLException e) {
			LOG.error("Failed to get current timestamp from db by issue query: {}", sql);
			throw e;
		}
	}

	/**
	 * @return
	 */
	protected String currentTimestampQuery() {
		return "SELECT CURRENT_TIMESTAMP";
	}

	@Override
	public List<TableId> tables(final Connection connection) throws SQLException {
		final DatabaseMetaData metadata = connection.getMetaData();
		final String[] tableTypes = tableTypes(metadata, this.tableTypes);

		try (final ResultSet rs = metadata.getTables(this.catalogPattern(), this.schemaPattern(),
				Constants.PERCENTAGE, tableTypes)) {
			List<TableId> tableIds = new ArrayList<TableId>();

			TableId tableId = null;
			while (rs.next()) {
				String catelogName = rs.getString(1);
				String schemaName = rs.getString(2);
				String tableName = rs.getString(3);

				tableId = new TableId(catelogName, schemaName, tableName);
				if (includeTableId(tableId)) {
					tableIds.add(tableId);
				}
			}

			return tableIds;
		}
	}

	/**
	 * Returns true if the given table identifier should be included, otherwise
	 * false.
	 * 
	 */
	protected boolean includeTableId(final TableId tableId) {
		return true;
	}

	protected String[] tableTypes(final DatabaseMetaData metadata, final Set<String> tableTypes)
			throws SQLException {
		final Set<String> upperCases = upperCase(tableTypes);
		try (final ResultSet rs = metadata.getTableTypes()) {
			List<String> matchings = new ArrayList<>();

			while (rs.next()) {
				String tableType = rs.getString(1);

				if (upperCases != null && upperCases.contains(tableType.toUpperCase(Locale.ROOT))) {
					matchings.add(tableType);
				}
			}

			return matchings.toArray(new String[matchings.size()]);
		}

	}

	private Set<String> upperCase(final Set<String> tableTypes) {
		Set<String> upperCases = new HashSet<>();
		if (tableTypes != null && tableTypes.isEmpty() == false) {
			for (String tableType : tableTypes) {
				upperCases.add(tableType.toUpperCase(Locale.ROOT));
			}
		}

		return upperCases;
	}

	@Override
	public boolean tableExists(final Connection connection, final TableId tableId)
			throws SQLException, ConnectException {
		LOG.debug("Checking {} dialect for existence of table {}", this, tableId);
		try (ResultSet rs = connection.getMetaData().getTables(tableId.catelogName(),
				tableId.getSchemaName(), tableId.getTableName(), new String[] { "TABLE" })) {

			boolean exists = rs.next();
			LOG.debug("Using {} dialect table {} {}", this, tableId,
					(exists ? "present" : "absent"));

			return exists;
		}
	}

	@Override
	public Table describeTable(final Connection connection, final TableId tableId)
			throws SQLException {
		Map<ColumnId, Column> colFqdn = this.describeColumns(connection, tableId.catelogName(),
				tableId.getSchemaName(), tableId.getTableName(), null);
		if (colFqdn != null) {
			return new Table(tableId, colFqdn.values());
		}

		return null;
	}

	@Override
	public Map<ColumnId, Column> describeColumns(final Connection connection,
			final String tablePattern, final String columnPattern) throws SQLException {
		final TableId tableId = parseTableId(tablePattern);
		String catelog = tableId.catelogName() != null ? tableId.catelogName() : catalogPattern();
		String schemaPattern = tableId.getSchemaName() != null ? tableId.getSchemaName()
				: schemaPattern();

		return describeColumns(connection, catelog, schemaPattern, tableId.getTableName(),
				columnPattern);
	}

	@Override
	public TableId parseTableId(final String fqn) {
		final List<String> segments = identifier.parse(fqn);
		if (segments.isEmpty()) {
			throw new IllegalArgumentException("Illegal full qualified name: " + fqn);
		}

		if (segments.size() == 1) {
			return new TableId(null, null, segments.get(0));
		}

		if (segments.size() == 3) {
			return new TableId(segments.get(0), segments.get(1), segments.get(2));
		}

		com.leatop.bee.common.utils.Objects.requiresGreaterThan(segments, 2,
				"Illegal full qualified name: " + fqn);
		if (useCatelog()) {
			return new TableId(segments.get(0), null, segments.get(1));
		} else {
			return new TableId(null, segments.get(0), segments.get(1));
		}
	}

	/**
	 * Returns true if catelog is used in current dialect, otherwise false.
	 * 
	 * @return true if catelog is used in current dialect, otherwise false.
	 */
	protected boolean useCatelog() {
		return false;
	}

	@Override
	public Map<ColumnId, Column> describeColumns(final Connection connection,
			final String catelogPattern, final String schemaPattern, final String tablePattern,
			final String columnPattern) throws SQLException {
		LOG.debug("Describe {} dialect column metadata for catelog: {} schema: {} table: {}", this,
				catelogPattern, schemaPattern, tablePattern);

		final Map<ColumnId, Column> columns = new HashMap<ColumnId, Column>();
		final Set<ColumnId> pkCols = pkColumns(connection, catelogPattern, schemaPattern,
				tablePattern);
		DatabaseMetaData metadata = connection.getMetaData();

		try (ResultSet rs = metadata.getColumns(catelogPattern, schemaPattern, tablePattern,
				columnPattern)) {
			final String catelog = rs.getString(1);
			final String schema = rs.getString(2);
			final String tableName = rs.getString(3);
			final TableId tableId = new TableId(catelog, schema, tableName);

			final String columnName = rs.getString(4);
			final ColumnId columnId = new ColumnId(tableId, columnName);

			boolean isPrimaryKey = pkCols.contains(columnId);
			final Column column = new Column(columnId, isPrimaryKey);
			columns.put(columnId, column);
		}

		return columns;
	}

	private Set<ColumnId> pkColumns(final Connection connection, final String catelog,
			final String schema, final String table) throws SQLException {
		final Set<ColumnId> pkColumns = new HashSet<>();
		try (ResultSet rs = connection.getMetaData().getPrimaryKeys(catelog, schema, table)) {
			final String cate = rs.getString(1);
			final String schem = rs.getString(2);
			final String tableName = rs.getString(3);
			final TableId tableId = new TableId(cate, schem, tableName);
			final String columnName = rs.getString(4);
			final ColumnId columnId = new ColumnId(tableId, columnName);

			pkColumns.add(columnId);
		}

		return pkColumns;
	}

	@Override
	public String buildInsertStatement(final TableId table, final Collection<ColumnId> keyColumns,
			final Collection<ColumnId> nonKeyColumns) {
		SQLBuilder sqlBuilder = sqlBuilder();
		sqlBuilder.insertInto(table).append(SQLBuilder.Constants.LEFT_PARENTHESE).listBuilder()
				.delimitedBy(SQLBuilder.Constants.COMMA_WITH_SPACE_DELIMITER)
				.transformedBy(SQLBuilder.columnNames()).of(keyColumns, nonKeyColumns)
				.append(SQLBuilder.Constants.RIGHT_PARENTHESE).append(SQLBuilder.Constants.VALUES)
				.append(SQLBuilder.Constants.LEFT_PARENTHESE)
				.appendMultiple(SQLBuilder.Constants.COMMA_DELIMITER,
						SQLBuilder.Constants.QUESTION_MARK_DELIMITER,
						keyColumns.size() + nonKeyColumns.size())
				.append(SQLBuilder.Constants.RIGHT_PARENTHESE);

		LOG.debug("The query for this insertion is: {}", sqlBuilder.toString());
		return sqlBuilder.toString();
	}

	@Override
	public SQLBuilder sqlBuilder() {
		return getIdentifier().sqlBuilder().setQuoteSqlIdentifiers(this.quoteSqlIdentifier);
	}

	/**
	 * Constants.
	 * 
	 * @author Dorsey
	 *
	 */
	public static interface Constants {

		String PERCENTAGE = "%";
		int VERSION_THRESHOLD = 4;
		String DIALECT_STRING = "Dialect";
		String EMPTY_STRING = "";
		String USER = "user";
		String PASSWORD = "password";
	}

	// ~~~ getters
	// ===========================================================================
	private String schemaPattern() {
		return this.schemaPattern;
	}

	private String catalogPattern() {
		return this.catalogPattern;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	@Override
	public StatementBinder statementBinder(final PreparedStatement pstmt,
			final Pair<Schema, Schema> schemaPair, final FieldMetadata fieldMetadata)
			throws SQLException {
		return new PreparedStatementBinder(this, pstmt, schemaPair, fieldMetadata);
	}

	@Override
	public void bindField(final PreparedStatement pstmt, final int index, final Schema schema,
			final Object object) throws SQLException {
		if (null == object) {
			pstmt.setObject(index, null);
			return;
		}

		boolean bound = tryBindLogical(pstmt, index, schema, object);
		if (!bound) {
			bound = tryBindPrimitive(pstmt, index, schema, object);
		}

		if (!bound) {
			throw new ConnectException("Unsupported source data type: " + schema.type());
		}
	}

	private boolean tryBindPrimitive(final PreparedStatement pstmt, final int index,
			final Schema schema, final Object object) throws SQLException {
		Type type = schema.type();
		switch (type) {
		case INT8:
			pstmt.setByte(index, (Byte) object);
			return true;

		case INT16:
			pstmt.setShort(index, (Short) object);
			return true;
		case INT32:
			pstmt.setInt(index, (Integer) object);
			return true;
		case INT64:
			pstmt.setLong(index, (Long) object);
			return true;
		case FLOAT32:
			pstmt.setFloat(index, (Float) object);
			return true;
		case FLOAT64:
			pstmt.setDouble(index, (Double) object);
			return true;
		case BOOLEAN:
			pstmt.setBoolean(index, (Boolean) object);
			return true;
		case STRING:
			pstmt.setString(index, new String(((String) object).getBytes(StandardCharsets.UTF_8)));
			return true;
		case BYTES:
			final byte[] bytes;
			if (object instanceof ByteBuffer) {
				final ByteBuffer buffer = ((ByteBuffer) object).slice();
				bytes = new byte[buffer.remaining()];
				buffer.get(bytes);
			} else {
				bytes = (byte[]) object;
			}

			pstmt.setBytes(index, bytes);
			return true;
		default:
			return false;
		}

	}

	private boolean tryBindLogical(final PreparedStatement pstmt, final int index,
			final Schema schema, final Object object) throws SQLException {
		final String schemaName = schema.name();
		if (schemaName != null) {
			switch (schemaName) {
			case Date.LOGICAL_NAME:
				pstmt.setDate(index, new java.sql.Date(((java.util.Date) object).getTime()));
				return true;
			case Decimal.LOGICAL_NAME:
				pstmt.setBigDecimal(index, (BigDecimal) object);
				return true;
			case Time.LOGICAL_NAME:
				pstmt.setTime(index, new java.sql.Time(((java.util.Date) object).getTime()));
				return true;
			case org.apache.kafka.connect.data.Timestamp.LOGICAL_NAME:
				pstmt.setTimestamp(index, new Timestamp(((java.util.Date) object).getTime()));
				return true;
			default:
				return false;
			}
		}

		return false;
	}

	@Override
	public Map<ColumnId, Column> describeColumnsByQuerying(final Connection connection,
			final TableId tableId) throws SQLException {
		String queryStr = "SELECT * FROM {} LIMIT 1";
		String quotedName = sqlBuilder().append(tableId).toString();
		try (PreparedStatement stmt = connection.prepareStatement(queryStr)) {
			stmt.setString(1, quotedName);
			try (ResultSet rs = stmt.executeQuery()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				return describeColumns(rsmd);
			}
		}
	}

	@Override
	public Map<ColumnId, Column> describeColumns(final ResultSetMetaData rsMetadata)
			throws SQLException {
		Map<ColumnId, Column> result = new LinkedHashMap<>();
		for (int i = 1; i <= rsMetadata.getColumnCount(); ++i) {
			Column defn = describeColumn(rsMetadata, i);
			result.put(defn.getColumnId(), defn);
		}
		return result;
	}

	/**
	 * Create a definition for the specified column in the result set.
	 *
	 * @param rsMetadata
	 *            the result set metadata; may not be null
	 * @param column
	 *            the column number, starting at 1 for the first column
	 * @return the column definition; never null
	 * @throws SQLException
	 *             if there is an error accessing the result set metadata
	 */
	protected Column describeColumn(final ResultSetMetaData rsMetadata, final int column)
			throws SQLException {
		String catalog = rsMetadata.getCatalogName(column);
		String schema = rsMetadata.getSchemaName(column);
		String tableName = rsMetadata.getTableName(column);
		TableId tableId = new TableId(catalog, schema, tableName);
		String name = rsMetadata.getColumnName(column);
		String alias = rsMetadata.getColumnLabel(column);
		ColumnId id = new ColumnId(tableId, name, alias);
		Nullability nullability;
		switch (rsMetadata.isNullable(column)) {
		case ResultSetMetaData.columnNullable:
			nullability = Nullability.NULL;
			break;
		case ResultSetMetaData.columnNoNulls:
			nullability = Nullability.NOT_NULL;
			break;
		case ResultSetMetaData.columnNullableUnknown:
		default:
			nullability = Nullability.UNKNOWN;
			break;
		}
		Mutability mutability = Mutability.MAYBE_WRITABLE;
		if (rsMetadata.isReadOnly(column)) {
			mutability = Mutability.READ_ONLY;
		} else if (rsMetadata.isWritable(column)) {
			mutability = Mutability.MAYBE_WRITABLE;
		} else if (rsMetadata.isDefinitelyWritable(column)) {
			mutability = Mutability.WRITABLE;
		}
		return new Column(id, rsMetadata.getColumnType(column),
				rsMetadata.getColumnTypeName(column), rsMetadata.getColumnClassName(column),
				nullability, mutability, rsMetadata.getPrecision(column),
				rsMetadata.getScale(column), rsMetadata.isSigned(column),
				rsMetadata.getColumnDisplaySize(column), rsMetadata.isAutoIncrement(column),
				rsMetadata.isCaseSensitive(column), rsMetadata.isSearchable(column),
				rsMetadata.isCurrency(column), false);
	}

	@Override
	public TimeZone getTimeZone() {
		return timeZone == null ? TimeZone.getTimeZone(ZoneOffset.UTC) : timeZone;
	}

	@Override
	public NumericMapping getNumericMapping() {
		return mapNumerics == null ? NumericMapping.NONE : mapNumerics;
	}

	@Override
	public JDBCDriverInfo getJdbcDriverInfo() {
		return jdbcDriverInfo();
	}

	@Override
	public String addFieldToSchema(final Column column, final SchemaBuilder builder) {
		return addFieldToSchema(column, builder, column.getColumnId().getColumnAlias(),
				column.type(), column.isOptional());
	}

	/**
	 * Use the supplied {@link SchemaBuilder} to add a field that corresponds to
	 * the column with the specified definition. This is intended to be easily
	 * overridden by subclasses.
	 *
	 * @param columnDefn
	 *            the definition of the column; may not be null
	 * @param builder
	 *            the schema builder; may not be null
	 * @param fieldName
	 *            the name of the field and
	 *            {@link #fieldNameFor(ColumnDefinition) computed} from the
	 *            column definition; may not be null
	 * @param sqlType
	 *            the JDBC {@link java.sql.Types type} as obtained from the
	 *            column definition
	 * @param optional
	 *            true if the field is to be optional as obtained from the
	 *            column definition
	 * @return the name of the field, or null if no field was added
	 */
	protected String addFieldToSchema(final Column columnDefn, final SchemaBuilder builder,
			final String fieldName, final int sqlType, final boolean optional) {
		int precision = columnDefn.precision();
		int scale = columnDefn.scale();
		switch (sqlType) {
		case Types.NULL: {
			log.debug("JDBC type 'NULL' not currently supported for column '{}'", fieldName);
			return null;
		}

		case Types.BOOLEAN: {
			builder.field(fieldName,
					optional ? Schema.OPTIONAL_BOOLEAN_SCHEMA : Schema.BOOLEAN_SCHEMA);
			break;
		}

		// ints <= 8 bits
		case Types.BIT: {
			builder.field(fieldName, optional ? Schema.OPTIONAL_INT8_SCHEMA : Schema.INT8_SCHEMA);
			break;
		}

		case Types.TINYINT: {
			if (columnDefn.isSignedNumber()) {
				builder.field(fieldName,
						optional ? Schema.OPTIONAL_INT8_SCHEMA : Schema.INT8_SCHEMA);
			} else {
				builder.field(fieldName,
						optional ? Schema.OPTIONAL_INT16_SCHEMA : Schema.INT16_SCHEMA);
			}
			break;
		}

		// 16 bit ints
		case Types.SMALLINT: {
			if (columnDefn.isSignedNumber()) {
				builder.field(fieldName,
						optional ? Schema.OPTIONAL_INT16_SCHEMA : Schema.INT16_SCHEMA);
			} else {
				builder.field(fieldName,
						optional ? Schema.OPTIONAL_INT32_SCHEMA : Schema.INT32_SCHEMA);
			}
			break;
		}

		// 32 bit ints
		case Types.INTEGER: {
			if (columnDefn.isSignedNumber()) {
				builder.field(fieldName,
						optional ? Schema.OPTIONAL_INT32_SCHEMA : Schema.INT32_SCHEMA);
			} else {
				builder.field(fieldName,
						optional ? Schema.OPTIONAL_INT64_SCHEMA : Schema.INT64_SCHEMA);
			}
			break;
		}

		// 64 bit ints
		case Types.BIGINT: {
			builder.field(fieldName, optional ? Schema.OPTIONAL_INT64_SCHEMA : Schema.INT64_SCHEMA);
			break;
		}

		// REAL is a single precision floating point value, i.e. a Java float
		case Types.REAL: {
			builder.field(fieldName,
					optional ? Schema.OPTIONAL_FLOAT32_SCHEMA : Schema.FLOAT32_SCHEMA);
			break;
		}

		// FLOAT is, confusingly, double precision and effectively the same as
		// DOUBLE.
		// See REAL
		// for single precision
		case Types.FLOAT:
		case Types.DOUBLE: {
			builder.field(fieldName,
					optional ? Schema.OPTIONAL_FLOAT64_SCHEMA : Schema.FLOAT64_SCHEMA);
			break;
		}

		case Types.NUMERIC:
			if (mapNumerics == NumericMapping.PRECISION_ONLY) {
				log.debug("NUMERIC with precision: '{}' and scale: '{}'", precision, scale);
				if (scale == 0 && precision < 19) { // integer
					Schema schema;
					if (precision > 9) {
						schema = (optional) ? Schema.OPTIONAL_INT64_SCHEMA : Schema.INT64_SCHEMA;
					} else if (precision > 4) {
						schema = (optional) ? Schema.OPTIONAL_INT32_SCHEMA : Schema.INT32_SCHEMA;
					} else if (precision > 2) {
						schema = (optional) ? Schema.OPTIONAL_INT16_SCHEMA : Schema.INT16_SCHEMA;
					} else {
						schema = (optional) ? Schema.OPTIONAL_INT8_SCHEMA : Schema.INT8_SCHEMA;
					}
					builder.field(fieldName, schema);
					break;
				}
			} else if (mapNumerics == NumericMapping.BEST_FIT) {
				log.debug("NUMERIC with precision: '{}' and scale: '{}'", precision, scale);
				if (precision < 19) { // fits in primitive data types.
					if (scale < 1 && scale >= NUMERIC_TYPE_SCALE_LOW) { // integer
						Schema schema;
						if (precision > 9) {
							schema = (optional) ? Schema.OPTIONAL_INT64_SCHEMA
									: Schema.INT64_SCHEMA;
						} else if (precision > 4) {
							schema = (optional) ? Schema.OPTIONAL_INT32_SCHEMA
									: Schema.INT32_SCHEMA;
						} else if (precision > 2) {
							schema = (optional) ? Schema.OPTIONAL_INT16_SCHEMA
									: Schema.INT16_SCHEMA;
						} else {
							schema = (optional) ? Schema.OPTIONAL_INT8_SCHEMA : Schema.INT8_SCHEMA;
						}
						builder.field(fieldName, schema);
						break;
					} else if (scale > 0) { // floating point - use double in
											// all cases
						Schema schema = (optional) ? Schema.OPTIONAL_FLOAT64_SCHEMA
								: Schema.FLOAT64_SCHEMA;
						builder.field(fieldName, schema);
						break;
					}
				}
			}
			// fallthrough

		case Types.DECIMAL: {
			scale = decimalScale(columnDefn);
			if (scale > 0) {
				builder.field(fieldName,
						optional ? Schema.OPTIONAL_FLOAT64_SCHEMA : Schema.FLOAT64_SCHEMA);
			} else {
				builder.field(fieldName,
						optional ? Schema.OPTIONAL_INT64_SCHEMA : Schema.INT64_SCHEMA);
			}
			break;
		}

		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
		case Types.NCHAR:
		case Types.NVARCHAR:
		case Types.LONGNVARCHAR:
		case Types.CLOB:
		case Types.NCLOB:
		case Types.DATALINK:
		case Types.SQLXML: {
			// Some of these types will have fixed size, but we drop this from
			// the schema
			// conversion
			// since only fixed byte arrays can have a fixed size
			builder.field(fieldName,
					optional ? Schema.OPTIONAL_STRING_SCHEMA : Schema.STRING_SCHEMA);
			break;
		}

		// Binary == fixed bytes
		// BLOB, VARBINARY, LONGVARBINARY == bytes
		case Types.BINARY:
		case Types.BLOB:
		case Types.VARBINARY:
		case Types.LONGVARBINARY: {
			builder.field(fieldName, optional ? Schema.OPTIONAL_BYTES_SCHEMA : Schema.BYTES_SCHEMA);
			break;
		}

		// Date is day + moth + year
		case Types.DATE: {
			SchemaBuilder dateSchemaBuilder = Date.builder();
			if (optional) {
				dateSchemaBuilder.optional();
			}
			builder.field(fieldName, dateSchemaBuilder.build());
			break;
		}

		// Time is a time of day -- hour, minute, seconds, nanoseconds
		case Types.TIME: {
			SchemaBuilder timeSchemaBuilder = Time.builder();
			if (optional) {
				timeSchemaBuilder.optional();
			}
			builder.field(fieldName, timeSchemaBuilder.build());
			break;
		}

		// Timestamp is a date + time
		case Types.TIMESTAMP: {
			SchemaBuilder tsSchemaBuilder = org.apache.kafka.connect.data.Timestamp.builder();
			if (optional) {
				tsSchemaBuilder.optional();
			}
			builder.field(fieldName, tsSchemaBuilder.build());
			break;
		}

		case Types.ARRAY:
		case Types.JAVA_OBJECT:
		case Types.OTHER:
		case Types.DISTINCT:
		case Types.STRUCT:
		case Types.REF:
		case Types.ROWID:
		default: {
			log.warn("JDBC type {} ({}) not currently supported", sqlType, columnDefn.typeName());
			return null;
		}
		}
		return fieldName;
	}

	protected int decimalScale(final Column defn) {
		return defn.scale() == NUMERIC_TYPE_SCALE_UNSET ? NUMERIC_TYPE_SCALE_HIGH : defn.scale();
	}

	@Override
	public List<TableNameAndBatchNo> tableNameAndBatchNos(final Connection conn,
			final TableId affiche) throws SQLException {
		List<TableNameAndBatchNo> tableNameAndBatchNos = new ArrayList<TableNameAndBatchNo>();

		TableNameAndBatchNo tableNameAndBatchNo = null;
		final String sql = getTableNameAndBatchNosSql(affiche);
		LOG.debug("Query: {} issued for retrieving table name and batch no from affice table: {}",
				sql, affiche);

		ResultSet rs = null;
		PreparedStatement pstmt = conn.prepareStatement(sql);
		try {
			rs = pstmt.executeQuery();
			while (rs.next()) {
				final String dataTableName = rs.getString(1);
				final String batchNo = rs.getString(2);

				tableNameAndBatchNo = new TableNameAndBatchNo(dataTableName, batchNo);
				tableNameAndBatchNos.add(tableNameAndBatchNo);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}

			if (pstmt != null) {
				pstmt.close();
			}
		}

		return tableNameAndBatchNos;
	}

	@Override
	public List<String> detailTableNames(final Connection conn, final TableId relTableId,
			final String parentTableName) throws SQLException {
		final List<String> detailTableNames = new ArrayList<String>();
		final String sql = sqlBuilder().append("SELECT DETAILTABLENAME FROM ").append(relTableId)
				.append(" WHERE STATUS = 1 AND TABLENAME = ?").toString();
		LOG.debug(
				"Query: {} issued for retrieving detail table name from relation table: {} with parent: {}",
				sql, relTableId, parentTableName);

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, parentTableName);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				detailTableNames.add(rs.getString(1));
			}
		} finally {
			if (rs != null) {
				rs.close();
			}

			if (pstmt != null) {
				pstmt.close();
			}
		}

		return detailTableNames;
	}

	protected String getTableNameAndBatchNosSql(final TableId afficheTableFqn) {
		return sqlBuilder().append("SELECT TABLENAME, BATCHNO FROM ").toString();
	}

	protected int maxRowsOfAffiche() {
		return config.getInt(JDBCSourceTaskConfig.MAX_ROWS_FETCH_FROM_AFFICHE_CONFIG);
	}

	@Override
	public void updateAfficheTable(final Connection connection, final TableId afficheTableName,
			final String batchNo) throws SQLException {
		final String sql = sqlBuilder().append("UPDATE ").append(afficheTableName)
				.append(" SET UPDATEFLAG = ?, UPDATETIME = ? WHERE BATCHNO = ?").toString();
		LOG.debug("Issue {} to update table: {}", sql, afficheTableName);

		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, 1);
			pstmt.setTimestamp(2,
					currentDBTimestamp(connection, Calendar.getInstance(Locale.CHINA)));
			pstmt.setString(3, batchNo);

			pstmt.executeUpdate();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
	}

	@Override
	public void addAnnouncement(final Connection conn, final String announcement,
			final SendBroadEntity entity) throws SQLException {
		final String sql = getInsertIntoAfficheTabSql(announcement);
		LOG.debug("Issue query {} to insert one announcement", sql);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, entity.getSystemId());
			pstmt.setString(2, entity.getTableName());
			pstmt.setString(3, entity.getBatchNo());
			pstmt.setDate(4, entity.getCreateTime());
			pstmt.setInt(5, entity.getNumRecords());
			pstmt.setInt(6, entity.getUpdateFlag());
			pstmt.setDate(7, entity.getUpdateTime());

			pstmt.executeUpdate();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
	}

	protected String getInsertIntoAfficheTabSql(final String afficheTableName) {
		return null;
	}
}
