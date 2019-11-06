package com.leatop.bee.data.weaver.serializer.domain;

import java.util.Objects;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.kafka.common.errors.SerializationException;

public class NonRecordContainer implements GenericContainer {

	private final Object value;
	private final Schema schema;

	public NonRecordContainer(final Object value, final Schema schema) {
		if (schema == null) {
			throw new SerializationException("Schema MUST not be null");
		}
		
		this.value = value;
		this.schema = schema;
	}

	@Override
	public Schema getSchema() {
		return schema;
	}
	
	public Object getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof NonRecordContainer)) return false;
		
		NonRecordContainer other = (NonRecordContainer) obj;
		return Objects.equals(value, other.value) && Objects.equals(schema, other.schema);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(schema, value);
	}
}
