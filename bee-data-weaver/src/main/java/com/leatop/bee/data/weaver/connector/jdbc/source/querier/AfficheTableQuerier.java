/**
 * File: AfficheTableQuerier.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月12日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source.querier;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.source.AfficheOffset;
import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceTaskConfig;
import com.leatop.bee.data.weaver.connector.jdbc.source.OffsetProtocols;
import com.leatop.bee.data.weaver.connector.jdbc.source.SchemaMapping.FieldSetter;
import com.leatop.bee.data.weaver.connector.jdbc.source.TableQuerier;
import com.leatop.bee.data.weaver.connector.jdbc.source.querier.AfficheCriteria.AfficheCriteriaValues;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;

/**
 * An implementation of {@link AfficheTableQuerier}, with retrieving data
 * according to Affiche table.
 * 
 * @author DORSEy
 *
 */
public class AfficheTableQuerier extends TableQuerier implements AfficheCriteriaValues {

	private static final Logger LOG = LoggerFactory.getLogger(AfficheTableQuerier.class);
	private AfficheCriteria criteria;
	private final Map<String, String> partition;
	private final AfficheOffset offset;
	private final String topic;
	private final String batchNo;

	/**
	 * Constructor of {@link AfficheTableQuerier}.
	 */
	public AfficheTableQuerier(final Dialect dialect, final QueryMode mode,
			final String nameOrQuery, final String batchNo, final JDBCSourceTaskConfig config,
			final Map<String, String> originals) {
		super(dialect, mode, nameOrQuery, config, originals);
		this.topic = topicPrefix + tableId.getTableName();
		this.batchNo = batchNo;
		if (this.batchNo == null) {
			throw new ConnectException("Batch no in Affiche table MUST NOT be null");
		}

		partition = OffsetProtocols.sourcePartitionForProtocolV1(tableId);
		this.offset = new AfficheOffset(tableId.getTableName(), this.batchNo);
	}


	@Override
	protected void createPreparedStatement(final Connection conn) throws SQLException {
		SQLBuilder builder = dialect.sqlBuilder();
		builder.append("SELECT * FROM ");
		builder.append(tableId);

		criteria = new AfficheCriteria();
		criteria.whereClause(builder);

		final String query = builder.toString();
		recordQuery(query);
		stmt = dialect.createPreparedStatement(conn, query);
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
				LOG.warn("Ignoring record because processing failed:", e);
			} catch (SQLException e) {
				LOG.warn("Ignoring record due to SQL error:", e);
			}
		}

		return new SourceRecord(partition, offset.toMap(), topic, record.schema(), record);
	}

	@Override
	public String batchNo() {
		return batchNo;
	}

	@Override
	public String lastBatchNo() throws SQLException {
		return batchNo;
	}

	@Override
	public String tableName() throws SQLException {
		return dialect.sqlBuilder().append(tableId).toString();
	}

	@Override
	public String toString() {
		return "AfficheTableQuerier:{table=" + tableId + ", batchNo=" + batchNo + "}";
	}
}
