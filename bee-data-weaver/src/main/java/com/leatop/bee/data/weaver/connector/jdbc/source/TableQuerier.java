/**
 * File: TableQuerier.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月12日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.jdbc.source.domain.FieldTransformerInfo;
import com.leatop.bee.data.weaver.connector.jdbc.source.domain.FieldTransformerInfo.SubFieldTransformer;
import com.leatop.bee.data.weaver.connector.jdbc.source.processor.FieldTransformer;
import com.leatop.bee.data.weaver.connector.jdbc.source.processor.FieldTransformerFactory;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder.Constants;

/**
 * @description 表查询的基础类
 */
public abstract class TableQuerier implements Comparable<TableQuerier> {
	public enum QueryMode {
		TABLE, QUERY
	}

	private final Logger log = LoggerFactory.getLogger(getClass());
	protected final Dialect dialect;
	protected final QueryMode mode;
	protected final String query;
	protected final String topicPrefix;
	protected final TableId tableId;
	protected long lastUpdate;
	protected PreparedStatement stmt;
	protected ResultSet resultSet;
	protected SchemaMapping schemaMapping;
	private String loggedQueryString;
	protected final JDBCSourceTaskConfig config;
	protected final List<FieldTransformerInfo> fieldSplitters;

	public TableQuerier(final Dialect dialect, final QueryMode mode, final String nameOrQuery,
			final JDBCSourceTaskConfig config, final Map<String, String> originals) {
		this.dialect = dialect;
		this.mode = mode;
		this.tableId = mode.equals(QueryMode.TABLE) ? dialect.parseTableId(nameOrQuery) : null;
		this.query = mode.equals(QueryMode.QUERY) ? nameOrQuery : null;
		this.config = config;
		this.topicPrefix = this.config.getString(JDBCSourceTaskConfig.TOPIC_PREFIX_CONFIG);
		this.lastUpdate = 0;

		// added for splitting the field to others.
		fieldSplitters = initSplitConfigs(originals);
	}

	/**
	 * Initialization.
	 * 
	 * @param originals
	 */
	@SuppressWarnings("unchecked")
	private List<FieldTransformerInfo> initSplitConfigs(final Map<String, String> originals) {
		final List<FieldTransformerInfo> fieldSplitters = new ArrayList<FieldTransformerInfo>();

		FieldTransformerInfo splitter = null;
		SubFieldTransformer subSplitter = null;
		FieldTransformer processor = null;
		List<String> columnsToBeSplitted = config
				.getList(JDBCSourceTaskConfig.COLUMNS_TO_BE_SPLITTED_CONFIG);
		final String colsDerivedFromFormat = config
				.getString(JDBCSourceTaskConfig.COLUMNS_DERIVED_FROM_FORMAT_CONFIG);
		final String splitterFormat = config
				.getString(JDBCSourceTaskConfig.TABLE_FROM_TABLE_SPLITTER_FORMAT_CONFIG);
		if (columnsToBeSplitted != null && !columnsToBeSplitted.isEmpty()) {
			for (String columnToBeSplitted : columnsToBeSplitted) {
				final String key = String.format(colsDerivedFromFormat, columnToBeSplitted);
				if (!originals.containsKey(key)) {
					// no corresponding configuration
					continue;
				}

				final String columnsDerivedStr = originals.get(key);
				final Iterator<String> columnsDerived = Splitter.on(Constants.COMMA_DELIMITER)
						.split(columnsDerivedStr).iterator();
				List<SubFieldTransformer> subSplitters = new ArrayList<FieldTransformerInfo.SubFieldTransformer>();
				while (columnsDerived.hasNext()) {
					final String columnDerived = columnsDerived.next();
					final String splitterKey = String.format(splitterFormat, columnDerived,
							columnToBeSplitted);

					String splitterClassName = null;
					if ((splitterClassName = originals.get(splitterKey)) == null) {
						throw new ConfigException("No splitter configured to split field: "
								+ columnToBeSplitted + " to: " + columnDerived);
					}

					try {
						Class<? extends FieldTransformer> clazz = (Class<? extends FieldTransformer>) Class
								.forName(splitterClassName, true,
										Utils.getContextOrKafkaClassLoader());
						processor = FieldTransformerFactory.getFieldProcessor(clazz, columnToBeSplitted,
								columnDerived, config, originals);

					} catch (ClassNotFoundException e) {
						throw new ConfigException(
								"Splitter class " + splitterClassName + " not found");
					}

					subSplitter = new SubFieldTransformer(columnDerived, processor);
					subSplitters.add(subSplitter);
				}

				splitter = new FieldTransformerInfo(tableId, columnToBeSplitted, subSplitters);
				fieldSplitters.add(splitter);
			}
		}

		return fieldSplitters;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public PreparedStatement getOrCreatePreparedStatement(final Connection db) throws SQLException {
		if (stmt != null) {
			return stmt;
		}
		createPreparedStatement(db);
		return stmt;
	}

	protected abstract void createPreparedStatement(Connection db) throws SQLException;

	public boolean querying() {
		return resultSet != null;
	}

	public void maybeStartQuery(final Connection db) throws SQLException {
		if (resultSet == null) {
			stmt = getOrCreatePreparedStatement(db);
			resultSet = executeQuery();
			String schemaName = tableId != null ? tableId.getTableName() : null;
			schemaMapping = SchemaMapping.create(schemaName, resultSet.getMetaData(), dialect,
					fieldSplitters);
		}
	}

	protected abstract ResultSet executeQuery() throws SQLException;

	public boolean next() throws SQLException {
		return resultSet.next();
	}

	public abstract SourceRecord extractRecord() throws SQLException;

	public void reset(final long now) {
		closeResultSetQuietly();
		closeStatementQuietly();
		schemaMapping = null;
		lastUpdate = now;
	}

	/**
	 * Release the resource it holds.
	 */
	public void close() {
		closeResultSetQuietly();
		closeStatementQuietly();
	}

	private void closeStatementQuietly() {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ignored) {
				log.error(ignored.getMessage(), ignored);
			}
		}

		stmt = null;
	}

	private void closeResultSetQuietly() {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException ignored) {
			}
		}

		resultSet = null;
	}

	protected void recordQuery(final String query) {
		if (query != null && !query.equals(loggedQueryString)) {
			log.info("Begin using SQL query: {}", query);
			loggedQueryString = query;
		}
	}

	@Override
	public int compareTo(final TableQuerier other) {
		if (this.lastUpdate < other.lastUpdate) {
			return -1;
		} else if (this.lastUpdate > other.lastUpdate) {
			return 1;
		} else {
			return this.tableId.compareTo(other.tableId);
		}
	}
}
