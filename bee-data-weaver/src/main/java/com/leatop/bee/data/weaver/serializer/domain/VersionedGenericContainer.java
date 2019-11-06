/**
 * File: GenericContainerWithVersion.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月13日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.serializer.domain;

import java.util.Objects;

import org.apache.avro.generic.GenericContainer;

/**
 * @author Dorsey
 *
 */
public class VersionedGenericContainer {

	private final GenericContainer container;
	private final Integer version;

	public VersionedGenericContainer(final GenericContainer container, final Integer version) {
		this.container = container;
		this.version = version;
	}

	public GenericContainer container() {
		return container;
	}

	public Integer version() {
		return version;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof VersionedGenericContainer)) return false;

		VersionedGenericContainer that = (VersionedGenericContainer) obj;
		return Objects.equals(container, that.container) 
				&& Objects.equals(version, that.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(container, version);
	}
}
