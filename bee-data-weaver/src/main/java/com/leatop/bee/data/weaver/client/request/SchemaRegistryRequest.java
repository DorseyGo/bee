/**
 * File: SchemaRegistrationReq.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.client.request;

import java.io.IOException;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author Dorsey
 *
 */
public class SchemaRegistryRequest {

	// ~~~ fields
	// ==========================================================================
	private Integer version;
	private Integer id;
	private String schema;

	public static SchemaRegistryRequest fromJson(final String json) throws IOException {
		return new ObjectMapper().readValue(json, SchemaRegistryRequest.class);
	}

	/**
	 * @return the version
	 */
	@JsonProperty("version")
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	@JsonProperty("version")
	public void setVersion(final Integer version) {
		this.version = version;
	}

	/**
	 * @return the id
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@JsonProperty("id")
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @return the schema
	 */
	@JsonProperty("schema")
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	@JsonProperty("schema")
	public void setSchema(final String schema) {
		this.schema = schema;
	}

	public String toJson() throws IOException {
		return new ObjectMapper().writeValueAsString(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, version, schema);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof SchemaRegistryRequest))
			return false;

		SchemaRegistryRequest other = (SchemaRegistryRequest) obj;
		return Objects.equals(id, other.id) && Objects.equals(version, other.version)
				&& Objects.equals(schema, other.schema);
	}

}
