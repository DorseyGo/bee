/**
 * File: ExceptionHandler.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor;

import com.leatop.bee.common.domain.TrafficData;

/**
 * An interface, which is used to handle the exception of data being supervised.
 * 
 * <p>
 * It provides one method <code>handle</code>, which is used to handle the
 * exception data.
 * </p>
 * 
 * @author Dorsey
 *
 */
public interface ExceptionHandler {
	
	/**
	 * Process the given exception data <code>exceptData</code>.
	 * 
	 * @param <ID> the identifier of data.
	 * @param exception the data verified as invalid.
	 */
	public <ID> void handle(final TrafficData<ID> exception);
}
