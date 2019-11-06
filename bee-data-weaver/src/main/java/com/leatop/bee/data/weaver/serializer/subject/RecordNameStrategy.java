/**
 * File: RecordNameStrategy.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.serializer.subject;

import java.util.Map;

import org.apache.avro.Schema;
import org.apache.kafka.common.errors.SerializationException;

import com.leatop.bee.data.weaver.serializer.config.KafkaAvroSerDeConfig;

/**
 * @author Dorsey
 *
 */
public class RecordNameStrategy implements SubjectNameStrategy<Schema> {

	@Override
	public void configure(final Map<String, ?> configs) {
		// empty for initialization.
	}

	@Override
	public String subjectNameOf(final String topic, final boolean isKey, final Schema schema) {
		return getRecordName(isKey, schema);
	}

	private String getRecordName(final boolean isKey, final Schema schema) {
		if (schema != null && schema.getType() == Schema.Type.RECORD) {
			return schema.getFullName();
		}

		throw expectionOf(isKey);
	}

	private SerializationException expectionOf(final boolean isKey) {
		if (isKey) {
			return new SerializationException(
					KafkaAvroSerDeConfig.KEY_SUBJECT_NAME_STRATEGY.getClass().getName()
							+ ", the message key must only be an Avro record schema");
		}
		
		return new SerializationException(
				KafkaAvroSerDeConfig.VALUE_SUBJECT_NAME_STRATEGY.getClass().getName()
				+ ", the message value must only be an Avro record schema");
	}

}
