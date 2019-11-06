/**
 * File: KafkaAvroSerializerConfig.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.serializer.config;

import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;

/**
 * Configurations for serializer.
 * 
 * @author Dorsey
 *
 */
public class KafkaAvroSerializerConfig extends KafkaAvroSerDeConfig {

	private static ConfigDef config;

	static {
		config = basicConfig();
	}

	/**
	 * @param originals
	 */
	public KafkaAvroSerializerConfig(final Map<?, ?> originals) {
		super(config, originals);
	}

}
