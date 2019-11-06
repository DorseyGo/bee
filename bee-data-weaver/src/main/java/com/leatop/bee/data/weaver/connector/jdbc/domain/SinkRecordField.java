/**
 * File: SinkRecordField.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月10日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

import java.util.Objects;

import org.apache.kafka.connect.data.Schema;

/**
 * @author Dorsey
 *
 */
public class SinkRecordField {

	private final Schema schema;
	private final String name;
	private final boolean isPrimaryKey;

	public SinkRecordField(final Schema schema, final String name, final boolean isPrimaryKey) {
		this.schema = schema;
		this.name = name;
		this.isPrimaryKey = isPrimaryKey;
	}

	/**
	 * @return the schema
	 */
	public Schema getSchema() {
		return schema;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the isPrimaryKey
	 */
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	@Override
	public int hashCode() {
		return Objects.hash(schema, name, isPrimaryKey);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof SinkRecordField))
			return false;

		SinkRecordField other = (SinkRecordField) obj;
		return (isPrimaryKey == other.isPrimaryKey) && Objects.equals(schema, other.schema)
				&& Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "SinkRecordField [schema=" + schema + ", name=" + name + ", isPrimaryKey="
				+ isPrimaryKey + "]";
	}

}
