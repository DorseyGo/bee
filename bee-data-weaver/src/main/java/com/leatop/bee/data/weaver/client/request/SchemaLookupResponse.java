/**
 * File: SchemaLookupResponse.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
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
public class SchemaLookupResponse {

	private String subject;
	private Integer version;
	private Integer id;
	private String schema;

	public SchemaLookupResponse(@JsonProperty("subject") final String subject,
			@JsonProperty("version") final Integer version, @JsonProperty("id") final Integer id,
			@JsonProperty("schema") final String schema) {
		this.subject = subject;
		this.version = version;
		this.id = id;
		this.schema = schema;
	}

	/**
	 * @return the subject
	 */
	@JsonProperty("subject")
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	@JsonProperty("subject")
	public void setSubject(final String subject) {
		this.subject = subject;
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

	public SchemaLookupResponse fromJson(final String json) throws IOException {
		return new ObjectMapper().readValue(json, SchemaLookupResponse.class);
	}

	public String toJson() throws IOException {
		return new ObjectMapper().writeValueAsString(this);
	}
}
