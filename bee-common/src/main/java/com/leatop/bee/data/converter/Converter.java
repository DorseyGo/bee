/**
 * File: Serializer.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月30日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.converter;

import com.leatop.bee.common.domain.TrafficData;

/**
 * An interface, which delcares to serialize the given object into an array of
 * bytes.
 * 
 * @author Dorsey
 *
 */
public interface Converter<T> {

	/**
	 * Serialize the given <code>data</code> to an array of bytes.
	 * 
	 * @param <ID>
	 * @param data
	 *            the given data.
	 * @return the array of bytes.
	 */
	<ID> T convertFrom(final TrafficData<ID> data);
}
