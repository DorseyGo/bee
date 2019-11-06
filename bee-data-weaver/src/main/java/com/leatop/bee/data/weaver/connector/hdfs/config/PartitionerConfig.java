/**
 * File: PartitionerConfig.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月11日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.config;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Width;

import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig.GenericRecommender;
import com.leatop.bee.data.weaver.connector.hdfs.partitioner.impl.DefaultPartitioner;

/**
 * Configuration for purpose of partition.
 * 
 * @author Dorsey
 *
 */
public class PartitionerConfig extends AbstractConfig implements ComposableConfig {

	// Partitioner group
	public static final String PARTITIONER_CLASS_CONFIG = "partitioner.class";
	public static final String PARTITIONER_CLASS_DOC = "The partitioner to use when writing data to the store.";
	public static final String PARTITIONER_CLASS_DEFAULT = DefaultPartitioner.class.getName();
	public static final String PARTITIONER_CLASS_DISPLAY = "Partitioner Class";

	public static final String PARTITION_FIELD_NAME_CONFIG = "partition.field.name";
	public static final String PARTITION_FIELD_NAME_DOC = "The name of the partitioning field when FieldPartitioner is used.";
	public static final String PARTITION_FIELD_NAME_DEFAULT = "";
	public static final String PARTITION_FIELD_NAME_DISPLAY = "Partition Field Name";

	/**
	 * Constructor.
	 */
	public PartitionerConfig(final ConfigDef definition, final Map<?, ?> originals) {
		super(definition, originals);
	}

	// ~~~ methods
	// ======================================================================================
	@Override
	public Object get(final String key) {
		return super.get(key);
	}

	public static ConfigDef newConfigDef(final GenericRecommender partitionerClassRecommender) {
		ConfigDef config = new ConfigDef();
		// segment of HDFS configuration.
		final String group = "PARTITIONER";
		int orderInGroup = 0;

		config.define(PARTITIONER_CLASS_CONFIG, Type.CLASS, PARTITIONER_CLASS_DEFAULT,
				Importance.HIGH, PARTITIONER_CLASS_DOC, group, ++orderInGroup, Width.MEDIUM,
				PARTITIONER_CLASS_DISPLAY);
		config.define(PARTITION_FIELD_NAME_CONFIG, Type.STRING, PARTITION_FIELD_NAME_DEFAULT,
				Importance.HIGH, PARTITION_FIELD_NAME_DOC, group, ++orderInGroup, Width.SHORT,
				PARTITION_FIELD_NAME_DISPLAY);

		return config;
	}
}
