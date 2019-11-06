/**
 * File: AvroRecordWriterSupplier.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format.supplier;

import java.io.IOException;

import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.RecordWriter;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.RecordWriterSupplier;
import com.leatop.bee.data.weaver.converter.AvroData;
import com.leatop.bee.data.weaver.serializer.domain.NonRecordContainer;

/**
 * An implementation of {@link RecordWriterSupplier}, provides writer to write
 * data via <tt>AVRO</tt> format.
 * 
 * @author Dorsey
 *
 */
public class AvroRecordWriterSupplier implements RecordWriterSupplier<HDFSSinkConnectorConfig> {

	private static final Logger LOG = LoggerFactory.getLogger(AvroRecordWriterSupplier.class);
	private static final String EXTENSION = ".avro";
	private final AvroData avroData;

	/**
	 * Constructor.
	 * 
	 * @param avroData
	 */
	public AvroRecordWriterSupplier(final AvroData avroData) {
		this.avroData = avroData;
	}

	@Override
	public String getExtension() {
		return EXTENSION;
	}

	@Override
	public RecordWriter getWriter(final HDFSSinkConnectorConfig config, final String filename) {
		return new RecordWriter() {

			final DataFileWriter<Object> writer = new DataFileWriter<>(new GenericDatumWriter<>());
			final Path path = new Path(filename);
			Schema schema = null;

			@Override
			public void write(final SinkRecord record) {
				if (schema == null) {
					schema = record.valueSchema();

					try {
						FSDataOutputStream os = path.getFileSystem(config.getHadoopConfiguration())
								.create(path);
						org.apache.avro.Schema schem = avroData.fromConnectSchema(schema);
						writer.setCodec(CodecFactory.fromString(config.getAvroCodec()));
						writer.create(schem, os);
					} catch (IOException e) {
						throw new ConnectException(e);
					}
				}

				LOG.debug("Writing record: {}", record.toString());
				final Object value = avroData.fromConnectData(schema, record.value());
				try {
					if (value instanceof NonRecordContainer) {
						writer.append(((NonRecordContainer) value).getValue());
						return;
					}

					writer.append(value);
				} catch (IOException e) {
					throw new DataException(e);
				}
			}

			@Override
			public void commit() {
				// no-op
			}

			@Override
			public void close() {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (IOException e) {
					throw new DataException(e);
				}
			}
		};
	}

}
