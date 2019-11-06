/**
 * File: Introspector.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor;

import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.data.supervisor.exception.DataInvalidException;

/**
 * An interface, which is used to give an introspection on the passed by
 * <code>data</code>.
 * 
 * <p>
 * It provides only one method <code>introspect</code>. This method, will
 * perform the introspection on the passed-in data, and determine whether the
 * data is violate the rules. If any voilation detected, a DataInvalidException
 * will be thrown.
 * </p>
 * 
 * @author Dorsey
 *
 */
public interface Introspector {

	/**
	 * Give an introspection on the passed by <code>data</code>. It throws
	 * {@link DataInvalidException} if data violate the given rules.
	 * 
	 * @param <ID>
	 * @param data
	 * @throws DataInvalidException
	 */
	<ID> void introspect(final TrafficData<ID> data) throws DataInvalidException;
}
