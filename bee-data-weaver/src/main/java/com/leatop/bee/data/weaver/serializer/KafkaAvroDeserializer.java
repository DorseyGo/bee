/**
 * File: KafkaAvroDeserializer.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.client.exception.HttpClientException;
import com.leatop.bee.data.weaver.serializer.config.KafkaAvroDeserializerConfig;
import com.leatop.bee.data.weaver.serializer.domain.NonRecordContainer;
import com.leatop.bee.data.weaver.serializer.domain.VersionedGenericContainer;

/**
 * De-serialize the Avro message stored in Kafka cluster.
 * 
 * @author Dorsey
 *
 */
public class KafkaAvroDeserializer extends AbstractKafkaAvroSerDe implements Deserializer<Object> {

	private final DecoderFactory decoderFactory = DecoderFactory.get();
	private final Map<String, Schema> readerSchemaCache = new ConcurrentHashMap<String, Schema>();
	private boolean isKey;
	private boolean useSpecificAvroReader = false;
	private static final Logger LOG = LoggerFactory.getLogger(KafkaAvroDeserializer.class);

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
		this.isKey = isKey;
		KafkaAvroDeserializerConfig deserializerConfig = new KafkaAvroDeserializerConfig(configs);
		configureClientProps(deserializerConfig);
		useSpecificAvroReader = deserializerConfig
				.getBoolean(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG);
	}

	@Override
	public Object deserialize(final String topic, final byte[] data) {
		return deserialize(data);
	}

	/**
	 * @param data
	 * @return
	 */
	private Object deserialize(final byte[] data) {
		return deserialize(false, null, isKey, data, null);
	}

	private Object deserialize(final boolean includeSchemaAndVersion, final String topic,
			final boolean isKey, final byte[] data, final Schema readerSchema) {
		if (data == null) {
			return null;
		}

		int id = -1;
		try {
			ByteBuffer buffer = getByteBufferAndVerify(data);
			id = buffer.getInt();
			Schema schema = client.getById(id);
			String subject = null;
			if (includeSchemaAndVersion) {
				subject = getSubjectName(topic, isKey, schema);
				schema = schemaForDeserialize(id, schema, subject, isKey);
			}

			LOG.debug("Schema: {} for id: {} of the subject: {}", schema.toString(), id, subject);
			
			int length = buffer.limit() - 1 - idSize;
			final Object result;
			if (schema.getType().equals(Schema.Type.BYTES)) {
				byte[] bytes = new byte[length];
				buffer.get(bytes, 0, length);

				result = bytes;
			} else {
				int start = buffer.position() + buffer.arrayOffset();
				DatumReader<Object> reader = getDatumReader(schema, readerSchema);
				Object obj = reader.read(null,
						decoderFactory.binaryDecoder(buffer.array(), start, length, null));

				if (schema.getType().equals(Schema.Type.STRING)) {
					obj = Objects.toString(obj);
				}

				result = obj;
			}

			if (includeSchemaAndVersion) {
				Integer version = schemaVersion(topic, isKey, id, subject, schema, result);
				LOG.debug("Should include schema and version: {}", version);

				if (schema.getType().equals(Schema.Type.RECORD)) {
					return new VersionedGenericContainer((GenericContainer) result, version);
				} else {
					return new VersionedGenericContainer(new NonRecordContainer(result, schema),
							version);
				}
			}

			return result;
		} catch (IOException e) {
			throw new SerializationException("Error deserializing AVRO message for id: " + id, e);
		} catch (HttpClientException e) {
			throw new SerializationException("Error retrieving AVRO schema for id: " + id, e);
		}
	}

	private Integer schemaVersion(final String topic, final boolean isKey2, final int id,
			final String subject, final Schema schema, final Object result)
			throws IOException, HttpClientException {
		return client.getVersion(subject, schema);
	}

	private Schema schemaForDeserialize(final int id, final Schema schema, final String subject,
			final boolean isKey2) throws IOException, HttpClientException {
		return client.getBySubjectAndId(subject, id);
	}

	protected VersionedGenericContainer deserializeWithSchemaAndVersion(final String topic,
			final boolean isKey, final byte[] payload) throws SerializationException {
		return (VersionedGenericContainer) deserialize(true, topic, isKey, payload, null);
	}

	/**
	 * @param data
	 * @return
	 */
	private ByteBuffer getByteBufferAndVerify(final byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		if (buffer.get() != MAGIC_BYTE) {
			throw new SerializationException("Unknown magic bytes");
		}

		return buffer;
	}

	private DatumReader<Object> getDatumReader(final Schema writerSchema, Schema readerSchema) {
		boolean writerSchemaIsPrimitive = AvroSchemaUtils.getPrimitiveSchemas().values()
				.contains(writerSchema);
		// do not use SpecificDatumReader if writerSchema is a primitive
		if (useSpecificAvroReader && !writerSchemaIsPrimitive) {
			if (readerSchema == null) {
				readerSchema = getReaderSchema(writerSchema);
			}

			return new SpecificDatumReader<>(writerSchema, readerSchema);
		} else {
			if (readerSchema == null) {
				return new GenericDatumReader<>(writerSchema);
			}
			return new GenericDatumReader<>(writerSchema, readerSchema);
		}
	}

	/**
	 * @param writerSchema
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Schema getReaderSchema(final Schema writerSchema) {
		Schema readerSchema = readerSchemaCache.get(writerSchema.getFullName());
		if (readerSchema == null) {
			final String errorMessage = writerSchema.getFullName()
					+ " specified could not be instantiated.";
			Class<SpecificRecord> readerClass = SpecificData.get().getClass(writerSchema);
			if (readerClass != null) {
				try {
					readerSchema = readerClass.newInstance().getSchema();
				} catch (InstantiationException e) {
					throw new SerializationException(errorMessage);
				} catch (IllegalAccessException e) {
					throw new SerializationException(errorMessage);
				}

				readerSchemaCache.put(writerSchema.getFullName(), readerSchema);
				return readerSchema;
			}

			throw new SerializationException("Could not find class " + writerSchema.getFullName()
					+ " specified in writer's schema whilst finding reader's "
					+ "schema for a SpecificRecord.");
		}

		return readerSchema;
	}

	@Override
	public void close() {
		// no-op.
	}

}
