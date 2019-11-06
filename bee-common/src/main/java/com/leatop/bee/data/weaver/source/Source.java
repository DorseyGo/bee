/**
 * File: Source.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.source;

import com.leatop.bee.data.weaver.exception.DataWeaveException;

/**
 * @author Dorsey
 *
 */
public interface Source<K, V> {

	/**
	 * Send the data to the specific topic.
	 * 
	 * @param <ID>
	 * @param data the data
	 * @param topic the topic, to which the data is sent.
	 * @throws DataWeaveException if any errors detected.
	 */
	void send(final String topic, final V data) throws DataWeaveException;
	
	/**
	 * Send the data, with <code>key</code> specified, to the specific topic.
	 * 
	 * @param <ID>
	 * @param data the data
	 * @param topic the topic, to which the data is sent.
	 * @throws DataWeaveException if any errors detected.
	 */
	void send(final String topic, final K key, final V data) throws DataWeaveException;
}
