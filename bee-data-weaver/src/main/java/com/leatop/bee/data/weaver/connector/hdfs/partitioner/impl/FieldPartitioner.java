/**
 * File: FieldPartitioner.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月11日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.partitioner.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.hdfs.config.PartitionerConfig;
import com.leatop.bee.data.weaver.connector.hdfs.config.StorageSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.partitioner.Partitioner;

/**
 * @author Dorsey
 *
 */
public class FieldPartitioner implements Partitioner {

	private static final Logger LOG = LoggerFactory.getLogger(FieldPartitioner.class);
	private List<String> fieldNames;
	private String delimiter;

	@Override
	public String encodePartition(final SinkRecord record) {
		Object value = record.value();
		if (!(value instanceof Struct)) {
			LOG.error("Unsupported type of value: {} for partition", value);
			throw new ConnectException("Failed to encode partition");
		}

		final Struct struct = (Struct) value;
		final Schema schema = record.valueSchema();
		final StringBuilder builder = new StringBuilder();
		for (Iterator<String> iter = fieldNames.iterator(); iter.hasNext(); ) {
			final String fieldName = iter.next();
			Object part = struct.get(fieldName);
			Type type = schema.field(fieldName).schema().type();

			switch (type) {
			case INT8:
			case INT16:
			case INT32:
			case INT64:
				Number partVal = (Number) part;
				builder.append(fieldName + "=" + partVal.toString());
				break;
			case STRING:
				builder.append(fieldName + "=" + (String) part);
				break;
			case BOOLEAN:
				boolean partBoolVal = (Boolean) part;
				builder.append(fieldName + "=" + String.valueOf(partBoolVal));
				break;
			default:
				LOG.error("Unsupported type: {} of field: {}", type, fieldName);
				throw new ConnectException("Failed to encode partition");
			}
			
			if (iter.hasNext()) {
				builder.append(this.delimiter);
			}
		}

		return builder.toString();
	}

	@Override
	public String genPartitionedPath(final String topic, final String encodedPartition) {
		return topic + delimiter + encodedPartition;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void configure(final Map<String, Object> props) {
		this.delimiter = (String) props.get(StorageSinkConnectorConfig.FOLDER_DELIMITER_CONFIG);
		fieldNames = (List<String>) props.get(PartitionerConfig.PARTITION_FIELD_NAME_CONFIG);
	}

}
