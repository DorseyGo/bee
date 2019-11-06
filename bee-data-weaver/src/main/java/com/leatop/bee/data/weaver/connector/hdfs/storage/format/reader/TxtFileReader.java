/**
 * File: TxtFileReader.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format.reader;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.kafka.connect.data.Schema;

import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.SchemaFileReader;

/**
 * @author Dorsey
 *
 */
public class TxtFileReader implements SchemaFileReader<HDFSSinkConnectorConfig, Path> {

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
		return null;
	}

}
