/**
 * File: SchemaMapping.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月12日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;

import com.leatop.bee.common.Pair;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.domain.Column;
import com.leatop.bee.data.weaver.connector.jdbc.domain.ColumnConverter;
import com.leatop.bee.data.weaver.connector.jdbc.domain.ColumnId;
import com.leatop.bee.data.weaver.connector.jdbc.domain.ColumnMapping;
import com.leatop.bee.data.weaver.connector.jdbc.domain.GenericColumnConverter;
import com.leatop.bee.data.weaver.connector.jdbc.source.domain.FieldTransformerInfo;
import com.leatop.bee.data.weaver.connector.jdbc.source.domain.FieldTransformerInfo.SubFieldTransformer;
import com.leatop.bee.data.weaver.connector.jdbc.source.processor.FieldTransformer;

public final class SchemaMapping {

	// ~~~ Fields
	// ===============================================
	private final Schema schema;
	private final List<FieldSetter> fieldSetters;

	// ~~~ constructors
	// ===============================================
	/**
	 * Sole constructor of {@link SchemaMapping}.
	 * 
	 * @param schema
	 * @param convertersByFieldName
	 */
	private SchemaMapping(final Schema schema,
			final Map<String, Pair<ColumnConverter, FieldTransformer>> convertersByFieldName) {
		assert schema != null;
		assert convertersByFieldName != null;
		assert !convertersByFieldName.isEmpty();
		this.schema = schema;
		List<FieldSetter> fieldSetters = new ArrayList<>(convertersByFieldName.size());
		for (Map.Entry<String, Pair<ColumnConverter, FieldTransformer>> entry : convertersByFieldName
				.entrySet()) {
			Field field = schema.field(entry.getKey());
			assert field != null;
			fieldSetters.add(
					new FieldSetter(entry.getValue().getKey(), entry.getValue().getValue(), field));
		}

		this.fieldSetters = Collections.unmodifiableList(fieldSetters);
	}

	// ~~~ methods
	// ===============================================
	/**
	 * Create an instance of {@link SchemaMapping}, with desired info set.
	 * 
	 */
	public static SchemaMapping create(final String schemaName, final ResultSetMetaData metadata,
			final Dialect dialect, final List<FieldTransformerInfo> splitters) throws SQLException {
		Map<ColumnId, Column> colDefns = dialect.describeColumns(metadata);
		Map<String, Pair<ColumnConverter, FieldTransformer>> colConvertersByFieldName = new LinkedHashMap<>();
		SchemaBuilder builder = SchemaBuilder.struct().name(schemaName);
		int columnNumber = 0;
		for (Column colDefn : colDefns.values()) {
			++columnNumber;
			String fieldName = null;
			if ((fieldName = addColToSchema(colDefn, dialect, builder, columnNumber, null,
					colConvertersByFieldName)) == null) {
				continue;
			}

			splitOrNot(fieldName, columnNumber, dialect, builder, colDefn, colConvertersByFieldName,
					splitters);
		}

		Schema schema = builder.build();
		return new SchemaMapping(schema, colConvertersByFieldName);
	}

	private static void splitOrNot(final String fieldName, final int colNo, final Dialect dialect,
			final SchemaBuilder builder, final Column originalCol,
			final Map<String, Pair<ColumnConverter, FieldTransformer>> colConvertersByFieldName,
			final List<FieldTransformerInfo> splitters) {
		for (FieldTransformerInfo splitter : splitters) {
			if (Objects.equals(fieldName, splitter.getSrcFieldName())) {
				for (SubFieldTransformer subSplitter : splitter.getChildren()) {
					Column derivedCol = new Column(
							new ColumnId(originalCol.tableId(), subSplitter.getDerivedFieldName()),
							subSplitter.getProcessor().getSqlType());

					addColToSchema(derivedCol, dialect, builder, colNo, subSplitter.getProcessor(),
							colConvertersByFieldName);
				}
			}
		}
	}

	private static String addColToSchema(final Column col, final Dialect dialect,
			final SchemaBuilder builder, final int colNo, final FieldTransformer processor,
			final Map<String, Pair<ColumnConverter, FieldTransformer>> colConvertersByFieldName) {
		final String fieldName = dialect.addFieldToSchema(col, builder);
		if (fieldName == null) {
			return null;
		}

		Field field = builder.field(fieldName);

		ColumnMapping mapping = new ColumnMapping(col, colNo, field);
		ColumnConverter converter = new GenericColumnConverter().createColumnConverter(mapping,
				dialect.getJdbcDriverInfo(), dialect.getTimeZone(), dialect.getNumericMapping());

		colConvertersByFieldName.put(fieldName,
				new Pair<ColumnConverter, FieldTransformer>(converter, processor));

		return fieldName;
	}

	public Schema schema() {
		return schema;
	}

	/**
	 * 
	 * @return
	 */
	public List<FieldSetter> fieldSetters() {
		return fieldSetters;
	}

	@Override
	public String toString() {
		return "Mapping for " + schema.name();
	}

	/**
	 * Helper class for set value to the specified field.
	 * 
	 * @author Dorsey
	 *
	 */
	public static final class FieldSetter {

		private final ColumnConverter converter;
		private final Field field;
		private final FieldTransformer processor;

		private FieldSetter(final ColumnConverter converter, final FieldTransformer processor,
				final Field field) {
			this.converter = converter;
			this.field = field;
			this.processor = processor;
		}

		/**
		 * 
		 * @return
		 */
		public Field field() {
			return field;
		}

		/**
		 * 
		 * @param struct
		 * @param resultSet
		 * @throws SQLException
		 * @throws IOException
		 */
		public void setField(final Struct struct, final ResultSet resultSet)
				throws SQLException, IOException {
			Object value = this.converter.convert(resultSet);
			if (resultSet.wasNull()) {
				struct.put(field, null);
			} else {
				struct.put(field,
						(processor != null) ? processor.setSourceFieldValue(value).process()
								: value);
			}
		}

		@Override
		public String toString() {
			return field.name();
		}
	}
}
