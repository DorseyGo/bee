/**
 * File: PreparedStatementBinder.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月11日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.sink.SinkRecord;

import com.leatop.bee.common.Pair;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;

/**
 * An implementation of {@link Dialect.StatementBinder}.
 * 
 * @author Dorsey
 *
 */
public class PreparedStatementBinder implements Dialect.StatementBinder {

	private final PreparedStatement pstmt;
	private final Dialect dialect;
	private final Pair<Schema, Schema> schemaPair;
	private final FieldMetadata metadata;

	public PreparedStatementBinder(final Dialect dialect, final PreparedStatement pstmt,
			final Pair<Schema, Schema> schemaPair, final FieldMetadata metadata) {
		this.dialect = dialect;
		this.pstmt = pstmt;
		this.schemaPair = schemaPair;
		this.metadata = metadata;
	}

	@Override
	public void bindRecord(final SinkRecord record) throws SQLException {
		Struct valStruct = (Struct) record.value();

		int index = 1;
		index = bindKeyFields(record, index);
		bindNonKeyFields(record, valStruct, index);

		pstmt.addBatch();
	}

	private void bindNonKeyFields(final SinkRecord record, final Struct valStruct,
			final int index) throws SQLException {
		int start = index;
		for (String nonKeyFieldName : metadata.nonKeyFieldNames()) {
			final Field nonKeyField = record.valueSchema().field(nonKeyFieldName);
			bindField(start++, nonKeyField.schema(), valStruct.get(nonKeyField));
		}
	}

	private int bindKeyFields(final SinkRecord record, final int index) throws SQLException {
		int start = index;
		for (String fieldName : metadata.keyFieldNames()) {
			final Field field = schemaPair.getValue().field(fieldName);
			bindField(start++, field.schema(), ((Struct) record.value()).get(field));
		}

		return start;
	}

	private void bindField(final int index, final Schema schema, final Object object)
			throws SQLException {
		dialect.bindField(pstmt, index, schema, object);
	}

}
