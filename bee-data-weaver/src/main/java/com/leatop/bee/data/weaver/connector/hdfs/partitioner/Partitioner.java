/**
 * File: Partitioner.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月4日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.partitioner;

import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;

/**
 * @author Dorsey
 *
 */
public interface Partitioner {

	/**
	 * Returns the string which represents the path for the given
	 * <code>record</code> be encoded and stored.
	 * 
	 * @param record the record to be encoded and stored.
	 */
	String encodePartition(final SinkRecord record);

	/**
	 * @param topic
	 * @param encodedPartition
	 * @return
	 */
	String genPartitionedPath(String topic, String encodedPartition);

	void configure(final Map<String, Object> props);
}
