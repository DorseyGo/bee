/**
 * File: FieldMetadata.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月10日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;

import com.leatop.bee.common.Pair;

/**
 * @author Dorsey
 *
 */
public class FieldMetadata {

	private final Set<String> keyFieldNames;
	private final Set<String> nonKeyFieldNames;
	private final Map<String, SinkRecordField> allFields;

	public FieldMetadata(final Set<String> keyFieldNames, final Set<String> nonKeyFieldNames,
			final Map<String, SinkRecordField> allFields) {
		boolean countsMatch = allFields.size() == (keyFieldNames.size() + nonKeyFieldNames.size());
		boolean contentsMatch = allFields.keySet().containsAll(keyFieldNames)
				&& allFields.keySet().containsAll(nonKeyFieldNames);
		if (!countsMatch || !contentsMatch) {
			throw new IllegalArgumentException(
					"keyFieldNames[" + keyFieldNames + "] + nonKeyFieldNames[" + nonKeyFieldNames
							+ "] != allFields[" + allFields + "]");
		}

		this.keyFieldNames = keyFieldNames;
		this.nonKeyFieldNames = nonKeyFieldNames;
		this.allFields = allFields;
	}

	public static FieldMetadata extract(final String tableName, final List<String> configedPkFields,
			final Pair<Schema, Schema> schemaPair, final List<String> whitelists) {
		return extract(tableName, configedPkFields, schemaPair.getKey(), schemaPair.getValue(),
				whitelists);
	}

	public static FieldMetadata extract(final String tableName, final List<String> configedPkFields,
			final Schema keySchema, final Schema valueSchema, final List<String> whitelists) {
		if (valueSchema != null && valueSchema.type() != Schema.Type.STRUCT) {
			throw new ConnectException("Value schema MUST be struct");
		}

		final Map<String, SinkRecordField> allFields = new HashMap<String, SinkRecordField>();
		final Set<String> keyFieldNames = new HashSet<>();
		extractRecordValuePk(tableName, configedPkFields, valueSchema, allFields, keyFieldNames,
				whitelists);

		final Set<String> nonKeyFieldNames = new HashSet<>();
		if (valueSchema != null) {
			for (Field field : valueSchema.fields()) {
				if (keyFieldNames.contains(field.name()) || !whitelists.contains(field.name())) {
					// only in white lists, then proceed.
					continue;
				}

				nonKeyFieldNames.add(field.name());
				allFields.put(field.name(),
						new SinkRecordField(field.schema(), field.name(), false));
			}
		}

		if (allFields.isEmpty()) {
			throw new ConnectException(
					"No fields found via using value schema for table: " + tableName);
		}

		return new FieldMetadata(keyFieldNames, nonKeyFieldNames, allFields);
	}

	private static void extractRecordValuePk(final String tableName,
			final List<String> configedPkFields, final Schema valueSchema,
			final Map<String, SinkRecordField> allFields, final Set<String> keyFieldNames,
			final List<String> whitelists) {
		if (valueSchema == null) {
			throw new ConnectException("the record value schema is missing");
		}

		if (configedPkFields.isEmpty()) {
			for (Field keyField : valueSchema.fields()) {
				if (whitelists.contains(keyField.name())) {
					// only when it's in white list, then will be sinked.
					keyFieldNames.add(keyField.name());
				}
			}
		} else {
			for (String pkFieldName : configedPkFields) {
				if (whitelists.contains(pkFieldName)) {
					// only when it's in white list, then will be sinked.
					if (valueSchema.field(pkFieldName) == null) {
						throw new ConnectException(
								"Record value schema does not contain field: " + pkFieldName);
					}

					keyFieldNames.add(pkFieldName);
				}
			}
		}

		// put them into all.
		for (String keyFieldName : keyFieldNames) {
			final Schema schema = valueSchema.field(keyFieldName).schema();
			allFields.put(keyFieldName, new SinkRecordField(schema, keyFieldName, true));
		}
	}

	public Set<String> keyFieldNames() {
		return keyFieldNames;
	}

	public Set<String> nonKeyFieldNames() {
		return nonKeyFieldNames;
	}

	public Map<String, SinkRecordField> allFields() {
		return allFields;
	}
}
