/**
 * File: SchemaRegistrationResp.java
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
public class SchemaRegistryResponse {

	private Integer id;

	public SchemaRegistryResponse fromJson(final String json) throws IOException {
		return new ObjectMapper().readValue(json, SchemaRegistryResponse.class);
	}

	public String toJson() throws IOException {
		return new ObjectMapper().writeValueAsString(this);
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

}
