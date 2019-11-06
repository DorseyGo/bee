/**
 * File: AvroConverter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月13日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.converter;

import java.util.Map;

import org.apache.avro.generic.GenericContainer;
import org.apache.avro.generic.IndexedRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.storage.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.client.CachedSchemaRegistryClient;
import com.leatop.bee.data.weaver.client.SchemaRegistryClient;
import com.leatop.bee.data.weaver.serializer.AvroSchemaUtils;
import com.leatop.bee.data.weaver.serializer.KafkaAvroDeserializer;
import com.leatop.bee.data.weaver.serializer.KafkaAvroSerializer;
import com.leatop.bee.data.weaver.serializer.domain.NonRecordContainer;
import com.leatop.bee.data.weaver.serializer.domain.VersionedGenericContainer;

/**
 * @author Dorsey
 *
 */
public class AvroConverter implements Converter {

	private static final Logger LOG = LoggerFactory.getLogger(AvroConverter.class);
	private SchemaRegistryClient client;
	private Serializer serializer;
	private Deserializer deserializer;

	private boolean isKey;
	private AvroData avroData;
	
	/**
	 * Empty constructor of {@link AvroConverter}.
	 */
	public AvroConverter() {
		// empty for initialization.
	}

	public AvroConverter(final SchemaRegistryClient client) {
		this.client = client;
	}

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
		LOG.debug("Config Avro converter via config: {}", configs);
		this.isKey = isKey;
		AvroConverterConfig config = new AvroConverterConfig(configs);

		if (this.client == null) {
			this.client = new CachedSchemaRegistryClient(config.getSchemaRegistryUrl(),
					config.getMaxSchemaPerSubject(), configs, config.getRequestHeaders());
		}

		serializer = new Serializer(configs, client);
		deserializer = new Deserializer(configs, client);
		avroData = new AvroData(new AvroDataConfig(configs));
	}

	@Override
	public byte[] fromConnectData(final String topic, final Schema schema, final Object value) {
		try {
			return serializer.serialize(topic, isKey, avroData.fromConnectData(schema, value));
		} catch (SerializationException e) {
			throw new ConnectException("Failed to serialize avro data from topic: " + topic, e);
		}
	}

	@Override
	public SchemaAndValue toConnectData(final String topic, final byte[] value) {
		try {
			VersionedGenericContainer container = deserializer.deserialize(topic, isKey, value);
			if (container == null) {
				return SchemaAndValue.NULL;
			}

			GenericContainer desiredContainer = container.container();
			Integer version = container.version();
			LOG.debug("Gotcha desired container: {}, with version: {}", desiredContainer, version);
			if (desiredContainer instanceof IndexedRecord) {
				return avroData.toConnectData(desiredContainer.getSchema(), desiredContainer,
						version);
			}

			if (desiredContainer instanceof NonRecordContainer) {
				return avroData.toConnectData(desiredContainer.getSchema(),
						((NonRecordContainer) desiredContainer).getValue(), version);
			}

			throw new DataException(
					"Unsupported type returned during deserializing topic: " + topic);
		} catch (SerializationException e) {
			throw new DataException("Failed to deserialize topic: " + topic + " to AVRO", e);
		}
	}

	private static class Serializer extends KafkaAvroSerializer {

		public Serializer(final SchemaRegistryClient client, final boolean autoRegisterSchema) {
			this.client = client;
			this.autoRegister = autoRegisterSchema;
		}

		public Serializer(final Map<String, ?> configs, final SchemaRegistryClient client) {
			this(client, false);
			configure(configs, false);
		}

		public byte[] serialize(final String topic, final boolean isKey, final Object value) {
			return serializeImpl(getSubjectName(topic, isKey, AvroSchemaUtils.getSchema(value)),
					value);
		}
	}

	private static class Deserializer extends KafkaAvroDeserializer {

		public Deserializer(final SchemaRegistryClient client) {
			this.client = client;
		}

		public Deserializer(final Map<String, ?> configs, final SchemaRegistryClient client) {
			this(client);
			configure(configs, false);
		}

		public VersionedGenericContainer deserialize(final String topic, final boolean isKey,
				final byte[] payload) {
			return deserializeWithSchemaAndVersion(topic, isKey, payload);
		}
	}
}
