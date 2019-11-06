/**
 * File: StorageSchemaCompatibility.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月4日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.schema;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaProjector;
import org.apache.kafka.connect.errors.SchemaProjectorException;
import org.apache.kafka.connect.sink.SinkRecord;

import com.google.common.base.Objects;

/**
 * @author Dorsey
 *
 */
public enum StorageSchemaCompatibility implements SchemaCompatibility {

	NONE {

		@Override
		public SinkRecord project(final SinkRecord record, final Schema curKeySchema,
				final Schema curValSchema) {
			return record;
		}

		@Override
		protected boolean introspect(final Schema originalSchema, final Schema curSchema) {
			return !originalSchema.equals(curSchema);
		}
	},
	BACKWARD, FORWARD {

		@Override
		protected boolean introspect(final Schema originalSchema, final Schema curSchema) {
			return originalSchema.version().compareTo(curSchema.version()) < 0;
		}
	},
	FULL;

	private static final Map<String, StorageSchemaCompatibility> REVERSE = new HashMap<>();
	static {
		for (StorageSchemaCompatibility compatibility : StorageSchemaCompatibility.values()) {
			REVERSE.put(compatibility.name(), compatibility);
		}
	}

	public static StorageSchemaCompatibility getCompability(final String name) {
		StorageSchemaCompatibility compatibility = REVERSE.get(name);

		return (compatibility == null ? NONE : compatibility);
	}

	@Override
	public boolean needChangeSchema(final ConnectRecord record, final Schema curKeySchema,
			final Schema curValSchema) {
		return validateAndIntrospect(record.valueSchema(), curValSchema);
	}

	private boolean validateAndIntrospect(final Schema valueSchema, final Schema curSchema) {
		if (valueSchema == null && curSchema == null) {
			return false;
		}
		
		if (valueSchema == null || curSchema == null) {
			throw new SchemaProjectorException("Swithing schema is not supported");
		}
		
		if (this != NONE && (valueSchema.version() == null || curSchema.version() == null)) {
			throw new SchemaProjectorException("Schema version required for compability");
		}
		
		return introspect(valueSchema, curSchema);
	}

	protected boolean introspect(final Schema originalSchema, final Schema curSchema) {
		return originalSchema.version().compareTo(curSchema.version()) > 0;
	}

	@Override
	public SinkRecord project(final SinkRecord record, final Schema curKeySchema,
			final Schema curValSchema) {
		Entry<Object, Object> entry = project0(record, curKeySchema, curValSchema);
		final Object curKey = entry.getKey();
		final Object curValue = entry.getValue();

		return (Objects.equal(curKey, record.key()) && Objects.equal(curValue, record.value()))
				? record
				: new SinkRecord(record.topic(), record.kafkaPartition(), curKeySchema, curKey,
						curValSchema, curValSchema, record.kafkaOffset(), record.timestamp(),
						record.timestampType());
	}

	private static Map.Entry<Object, Object> project0(final ConnectRecord record,
			final Schema curKeySchema, final Schema curValSchema) {
		Object value = project0(record.valueSchema(), record.value(), curValSchema);

		return new AbstractMap.SimpleEntry<>(record.key(), value);
	}

	private static Object project0(final Schema originalSchema, final Object value,
			final Schema curSchema) {
		if (Objects.equal(originalSchema, curSchema)) {
			return value;
		}

		return SchemaProjector.project(originalSchema, value, curSchema);
	}
}
