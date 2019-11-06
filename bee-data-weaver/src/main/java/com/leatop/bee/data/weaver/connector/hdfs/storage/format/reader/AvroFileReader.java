/**
 * File: AvroFileReader.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format.reader;

import java.io.IOException;
import java.util.Iterator;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.DataException;

import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.SchemaFileReader;
import com.leatop.bee.data.weaver.converter.AvroData;

/**
 * @author Dorsey
 *
 */
public class AvroFileReader implements SchemaFileReader<HDFSSinkConnectorConfig, Path> {
	
	private final AvroData avroData;
	
	public AvroFileReader(final AvroData avroData) {
		this.avroData = avroData;
	}

	@Override
	public boolean hasNext() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object next() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Object> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() throws IOException {
		// no-op
	}

	@Override
	public Schema getSchema(final HDFSSinkConnectorConfig config, final Path path) {
		try {
			SeekableInput input = new FsInput(path, config.getHadoopConfiguration());
			DatumReader<Object> out = new GenericDatumReader<>();
			DataFileReader<Object> reader = new DataFileReader<>(input, out);
			org.apache.avro.Schema schem = reader.getSchema();
			reader.close();
			
			return avroData.toConnectSchema(schem);
		} catch (IOException e) {
			throw new DataException(e);
		}
	}

}
