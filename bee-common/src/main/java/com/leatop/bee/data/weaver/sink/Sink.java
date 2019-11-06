/**
 * File: Sink.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.sink;

import java.util.Map;

import com.leatop.bee.data.weaver.exception.DataWeaveException;

/**
 * @author Dorsey
 *
 */
public interface Sink {
	
	/**
	 * Gives configuration.
	 * 
	 * @param props
	 */
	void doConfig(Map<String, String> props);
	
	/**
	 * Fetch the data from repository and processing them.
	 * 
	 * @throws DataWeaveException
	 */
	void doProcess() throws DataWeaveException;
	
	/**
	 * Start the component, prepare for receive the input.
	 */
	void start();
	
	/**
	 * Destroy the current <code>Source</code>. Releases all the resource it
	 * holds.
	 */
	void stop();
}
