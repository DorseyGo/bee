/**
 * File: AbstractKafkaAvroSerDe.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.serializer;

import java.util.Map;

import org.apache.avro.Schema;

import com.leatop.bee.data.weaver.client.CachedSchemaRegistryClient;
import com.leatop.bee.data.weaver.client.SchemaRegistryClient;
import com.leatop.bee.data.weaver.serializer.config.KafkaAvroSerDeConfig;
import com.leatop.bee.data.weaver.serializer.subject.SubjectNameStrategy;
import com.leatop.bee.data.weaver.serializer.subject.TopicNameStrategy;

/**
 * @author Dorsey
 *
 */
public abstract class AbstractKafkaAvroSerDe {

	protected static final byte MAGIC_BYTE = 0x0;
	protected static final int idSize = 4;

	protected SchemaRegistryClient client;
	protected Object keySubjectNameStrategy = new TopicNameStrategy();
	protected Object valueSubjectNameStrategy = new TopicNameStrategy();

	protected void configureClientProps(final KafkaAvroSerDeConfig config) {
		String basic = config.getSchemaRegistryUrl();
		int maxSchemaPerSubject = config.getMaxSchemaPerSubject();
		Map<String, Object> originals = config.originalsWithPrefix("");
		if (null == client) {
			client = new CachedSchemaRegistryClient(basic, maxSchemaPerSubject, originals,
					config.getRequestHeaders());
		}

		keySubjectNameStrategy = config.keySubjectNameStrategy();
		valueSubjectNameStrategy = config.valueSubjectNameStrategy();
	}

	/**
	 * Get the subject name for the given topic and value type.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String getSubjectName(final String topic, final boolean isKey, final Schema schema) {
		Object subjectNameStrategy = subjectNameStrategy(isKey);
		if (subjectNameStrategy instanceof SubjectNameStrategy) {
			return ((SubjectNameStrategy) subjectNameStrategy).subjectNameOf(topic, isKey, schema);
		}

		return "topic";
	}

	private Object subjectNameStrategy(final boolean isKey) {
		return isKey ? keySubjectNameStrategy : valueSubjectNameStrategy;
	}
}
