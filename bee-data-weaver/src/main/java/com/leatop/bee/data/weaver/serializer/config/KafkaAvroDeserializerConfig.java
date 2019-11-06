/**
 * File: KafkaAvroDeserializerConfig.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.serializer.config;

import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

/**
 * Configuration for de-serialization of Avro message stored in Kafka cluster.
 * 
 * @author Dorsey
 *
 */
public class KafkaAvroDeserializerConfig extends KafkaAvroSerDeConfig {

	public static final String SPECIFIC_AVRO_READER_CONFIG = "specific.avro.reader";
	public static final boolean SPECIFIC_AVRO_READER_DEFAULT = false;
	public static final String SPECIFIC_AVRO_READER_DOC = "If true, tries to look up the SpecificRecord class ";

	private static ConfigDef config;

	static {
		config = basicConfig().define(SPECIFIC_AVRO_READER_CONFIG, Type.BOOLEAN,
				SPECIFIC_AVRO_READER_DEFAULT, Importance.LOW, SPECIFIC_AVRO_READER_DOC);
	}

	/**
	 * @param originals
	 */
	public KafkaAvroDeserializerConfig(final Map<?, ?> originals) {
		super(config, originals);
	}

}
