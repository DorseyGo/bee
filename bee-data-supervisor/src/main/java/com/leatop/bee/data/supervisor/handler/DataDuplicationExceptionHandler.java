/**
 * File: DataDuplicationExceptionHandler.java
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
public class DataDuplicationExceptionHandler implements ExceptionHandler {

	/**
	 * Empty constructor of {@link DataDuplicationExceptionHandler}.
	 */
	public DataDuplicationExceptionHandler() {
		// empty for initialization.
	}

	public <ID> void handle(final TrafficData<ID> exception) {
		// discard the passed in data
	}

}
