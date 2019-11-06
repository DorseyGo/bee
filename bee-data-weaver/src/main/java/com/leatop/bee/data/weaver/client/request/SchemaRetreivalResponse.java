/**
 * File: SchemaRetreivalResponse.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.client.request;

import java.io.IOException;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author Dorsey
 *
 */
public class SchemaRetreivalResponse {

	private String schema;

	/**
	 * Empty constructor of {@link SchemaRetreivalResponse}.
	 */
	public SchemaRetreivalResponse() {
		// empty for initialization.
	}

	/**
	 * @param schema
	 */
	public SchemaRetreivalResponse(final String schema) {
		this.schema = schema;
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
	
	public SchemaRetreivalResponse fromJson(final String json) throws IOException {
		return new ObjectMapper().readValue(json, SchemaRetreivalResponse.class);
	}
	
	public String toJson() throws IOException {
		return new ObjectMapper().writeValueAsString(this);
	}
}
