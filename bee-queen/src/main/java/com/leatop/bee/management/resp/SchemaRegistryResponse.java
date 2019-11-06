/**
 * File: SchemaRegistryResponse.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.resp;

import java.util.Objects;

/**
 * @author Dorsey
 *
 */
public class SchemaRegistryResponse {

	private Integer id;

	/**
	 * Empty constructor of {@link SchemaRegistryResponse}.
	 */
	public SchemaRegistryResponse() {
		super();
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof SchemaRegistryResponse))
			return false;

		SchemaRegistryResponse other = (SchemaRegistryResponse) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "SchemaRegistryResponse [id=" + id + "]";
	}

}
