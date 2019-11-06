/**
 * File: LookupSchemaResponse.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.resp;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Dorsey
 *
 */
public class LookupSchemaResponse {

	private String subject;
	private Integer version;
	private Integer id;
	private String schema;
	
	/**
	 * Empty constructor of {@link LookupSchemaResponse}.
	 */
	public LookupSchemaResponse() {
		super();
	}

	/**
	 * @param subject
	 * @param version
	 * @param id
	 * @param schema
	 */
	public LookupSchemaResponse(final String subject, final Integer version, final Integer id,
			final String schema) {
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

}
