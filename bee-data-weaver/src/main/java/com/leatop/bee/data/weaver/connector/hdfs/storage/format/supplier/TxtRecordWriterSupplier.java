/**
 * File: TxtRecordWriterSupplier.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format.supplier;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.apache.hadoop.fs.Path;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.storage.HDFSStorage;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.RecordWriter;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.RecordWriterSupplier;

/**
 * Writer supplier, which producer writer to write data to the plain-text file.
 * 
 * @author Dorsey
 *
 */
public class TxtRecordWriterSupplier implements RecordWriterSupplier<HDFSSinkConnectorConfig> {
	
	private static final Logger LOG = LoggerFactory.getLogger(TxtRecordWriterSupplier.class);
	private static final String EXTENSION = ".txt";
	private static final int BUFFER_SIZE = 128 * 1024;
	private final HDFSStorage storage;
	
	/**
	 * Constructor of {@link TxtRecordWriterSupplier}.
	 * 
	 * @param storage
	 */
	public TxtRecordWriterSupplier(final HDFSStorage storage) {
		this.storage = storage;
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
				final OutputStream os = path.getFileSystem(config.getHadoopConfiguration()).create(path);
				final OutputStreamWriter out = new OutputStreamWriter(os, StandardCharsets.UTF_8);
				final BufferedWriter writer = new BufferedWriter(out, BUFFER_SIZE);
				
				@Override
				public void write(final SinkRecord record) {
					LOG.debug("Writing record: {}", record.toString());
					try {
						final String value = (String) record.value();
						writer.write(value);
						writer.newLine();
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
					try {
						if (writer != null) {
							writer.close();
						}
					} catch (IOException e) {
						throw new ConnectException(e);
					}
				}
			};
		} catch (IOException e) {
			throw new ConnectException(e);
		}
	}
	
	/**
	 * @return the storage
	 */
	public HDFSStorage getStorage() {
		return storage;
	}
}
