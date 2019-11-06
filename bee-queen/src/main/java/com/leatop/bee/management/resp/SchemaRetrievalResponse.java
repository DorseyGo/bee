/**
 * File: SchemaRetrievalResponse.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.resp;

import java.util.Objects;

/**
 * @author Dorsey
 *
 */
public class SchemaRetrievalResponse {

	private String schema;

	/**
	 * Constructor of {@link SchemaRetrievalResponse}, with schema specified.
	 * 
	 * @param schema
	 */
	public SchemaRetrievalResponse(final String schema) {
		super();
		this.schema = schema;
	}

	/**
	 * Empty constructor of {@link SchemaRetrievalResponse}.
	 */
	public SchemaRetrievalResponse() {
		super();
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(final String schema) {
		this.schema = schema;
	}

	@Override
	public int hashCode() {
		return Objects.hash(schema);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof SchemaRetrievalResponse))
			return false;

		SchemaRetrievalResponse other = (SchemaRetrievalResponse) obj;
		return Objects.equals(schema, other.schema);
	}

	@Override
	public String toString() {
		return "SchemaRetrievalResponse [schema=" + schema + "]";
	}

}
