/**
 * File: KafkaAvroSerializer.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.leatop.bee.data.weaver.client.exception.HttpClientException;
import com.leatop.bee.data.weaver.serializer.config.KafkaAvroSerializerConfig;

/**
 * @author Dorsey
 *
 */
public class KafkaAvroSerializer extends AbstractKafkaAvroSerDe implements Serializer<Object> {

	private final EncoderFactory factory = EncoderFactory.get();
	protected boolean autoRegister = false;
	private boolean isKey = false;

	/**
	 * Empty constructor of {@link KafkaAvroSerializer}.
	 */
	public KafkaAvroSerializer() {
		// empty for Kafka producer.
	}

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
		this.isKey = isKey;
		KafkaAvroSerializerConfig serializerConfigs = new KafkaAvroSerializerConfig(configs);
		configureClientProps(serializerConfigs);
		autoRegister = serializerConfigs.isAutoRegisterSchemas();
	}

	@Override
	public byte[] serialize(final String topic, final Object data) {
		return serializeImpl(getSubjectName(topic, isKey, AvroSchemaUtils.getSchema(data)), data);
	}
	
	protected byte[] serializeImpl(final String subject, final Object data) {
		if (data == null) {
			// no need serialize null object.
			return null;
		}
		
		Schema schema = null;
		final ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			schema = AvroSchemaUtils.getSchema(data);
			registrySchema(subject, schema, out);

			if (data instanceof byte[]) {
				out.write((byte[]) data);
			} else {
				BinaryEncoder encoder = factory.directBinaryEncoder(out, null);
				DatumWriter<Object> writer = (data instanceof SpecificRecord)
						? new SpecificDatumWriter<Object>(schema)
						: new GenericDatumWriter<Object>(schema);

				writer.write(data, encoder);
				encoder.flush();
			}

			byte[] result = out.toByteArray();
			return result;
		} catch (IOException e) {
			throw new SerializationException("Error serializing Avro message", e);
		} catch (HttpClientException e) {
			throw new SerializationException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ignore) {
					// ignore
				}
			}
		}
	}

	/**
	 * register schema of the specific <tt>subject</tt>, and write the
	 * identifier to the output array.
	 * 
	 * @throws HttpClientException
	 * @throws IOException if any I/O error detected.
	 */
	private void registrySchema(final String subject, final Schema schema,
			final ByteArrayOutputStream out) throws IOException, HttpClientException {
		int id = (autoRegister) ? client.register(subject, schema) : client.getId(subject, schema);
		out.write(MAGIC_BYTE);
		try {
			out.write(ByteBuffer.allocate(idSize).putInt(id).array());
		} catch (IOException e) {
			throw new SerializationException("Error serializing the Avro message", e);
		}
	}

	@Override
	public void close() {
		// nothing to destroy.
	}

}
