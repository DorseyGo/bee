/**
 * File: DataInvalidExceptionHandler.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.handler;

import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.data.supervisor.ExceptionHandler;

/**
 * @author Dorsey
 *
 */
public class DataInvalidExceptionHandler implements ExceptionHandler {
	
	/**
	 * Empty constructor of {@link DataInvalidExceptionHandler}.
	 */
	public DataInvalidExceptionHandler() {
		// empty for initialization.
	}

	public <ID> void handle(final TrafficData<ID> exception) {
		// TODO, put the data into the database as desired
	}

}
