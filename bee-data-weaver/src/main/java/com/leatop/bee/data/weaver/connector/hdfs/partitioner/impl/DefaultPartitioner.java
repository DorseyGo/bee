/**
 * File: DefaultPartitioner.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月11日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.partitioner.impl;

import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;

import com.leatop.bee.data.weaver.connector.hdfs.config.StorageSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.partitioner.Partitioner;

/**
 * Default implementation of {@link Partitioner}, with partition directory like
 * this partition=<code>offset</code>.
 * 
 * @author Dorsey
 *
 */
public class DefaultPartitioner implements Partitioner {

	private static final String PARTITION_FIELD = "partition";
	private String delimiter;
	private Map<String, Object> config;

	@Override
	public String encodePartition(final SinkRecord record) {
		return PARTITION_FIELD + "=" + String.valueOf(record.kafkaOffset());
	}

	@Override
	public String genPartitionedPath(final String topic, final String encodedPartition) {
		return topic + delimiter + encodedPartition;
	}

	@Override
	public void configure(final Map<String, Object> props) {
		this.config = props;
		this.delimiter = (String) config.get(StorageSinkConnectorConfig.FOLDER_DELIMITER_CONFIG);
	}

}
