/**
 * File: TxtFormatter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format.formatters;

import org.apache.hadoop.fs.Path;

import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.storage.HDFSStorage;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.Formatter;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.RecordWriterSupplier;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.SchemaFileReader;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.reader.TxtFileReader;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.supplier.TxtRecordWriterSupplier;

/**
 * @author Dorsey
 *
 */
public class TxtFormatter implements Formatter<HDFSSinkConnectorConfig, Path> {

	private final HDFSStorage storage;

	/**
	 * @param storage
	 */
	public TxtFormatter(final HDFSStorage storage) {
		this.storage = storage;
	}

	@Override
	public RecordWriterSupplier<HDFSSinkConnectorConfig> getRecordWriterSupplier() {
		return new TxtRecordWriterSupplier(storage);
	}

	@Override
	public SchemaFileReader<HDFSSinkConnectorConfig, Path> getSchemaFileReader() {
		return new TxtFileReader();
	}

}
