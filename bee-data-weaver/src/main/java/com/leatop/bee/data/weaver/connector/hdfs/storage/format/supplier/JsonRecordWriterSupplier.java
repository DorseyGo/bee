/**
 * File: JsonRecordWriterSupplier.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format.supplier;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.hadoop.fs.Path;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.sink.SinkRecord;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.RecordWriter;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.RecordWriterSupplier;

/**
 * An implementation of {@link RecordWriterSupplier}, which provides writer to
 * write <code>Json</code> data to the underlying file system.
 * 
 * @author Dorsey
 *
 */
public class JsonRecordWriterSupplier implements RecordWriterSupplier<HDFSSinkConnectorConfig> {

	private static final Logger LOG = LoggerFactory.getLogger(JsonRecordWriterSupplier.class);
	private static final String EXTENSION = ".json";
	private static final String LINE_SEPARATOR = System.lineSeparator();
	private static final byte[] LINE_SEPARATOR_BYTES = LINE_SEPARATOR
			.getBytes(StandardCharsets.UTF_8);
	private final ObjectMapper mapper;
	private final JsonConverter converter;

	/**
	 * Constructor.
	 * 
	 */
	public JsonRecordWriterSupplier(final JsonConverter converter) {
		this.converter = converter;
		this.mapper = new ObjectMapper();
	}

	@Override
	public String getExtension() {
		return EXTENSION;
	}

	@Override
	public RecordWriter getWriter(final HDFSSinkConnectorConfig config, final String filename) {

		try {
			return new RecordWriter() {

				final Path path = new Path(filename);
				final OutputStream os = path.getFileSystem(config.getHadoopConfiguration())
						.create(path);
				final JsonGenerator writer = mapper.getJsonFactory().createJsonGenerator(os);

				@Override
				public void write(final SinkRecord record) {
					LOG.debug("Writing sink record: {}", record.toString());

					try {
						Object value = record.value();
						if (value instanceof Struct) {
							byte[] json = converter.fromConnectData(record.topic(),
									record.valueSchema(), value);
							os.write(json);
							os.write(LINE_SEPARATOR_BYTES);

							return;
						}

						writer.writeObject(value);
						writer.writeRaw(LINE_SEPARATOR);
					} catch (IOException e) {
						throw new ConnectException(e);
					}
				}

				@Override
				public void commit() {
					// no-op
				}

				@Override
				public void close() {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException e) {
							throw new ConnectException(e);
						}
					}
				}
			};
		} catch (IOException e) {
			throw new ConnectException(e);
		}
	}

	/**
	 * @return the mapper
	 */
	public ObjectMapper getMapper() {
		return mapper;
	}
}
