/**
 * File: AvroFormatter.java
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
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.reader.AvroFileReader;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.supplier.AvroRecordWriterSupplier;
import com.leatop.bee.data.weaver.converter.AvroData;

/**
 * @author Dorsey
 *
 */
public class AvroFormatter implements Formatter<HDFSSinkConnectorConfig, Path> {

	private final AvroData avroData;

	public AvroFormatter(final HDFSStorage storage) {
		this.avroData = new AvroData(
				storage.getConfig().getInt(HDFSSinkConnectorConfig.SCHEMA_CACHE_SIZE_CONFIG));
	}

	@Override
	public RecordWriterSupplier<HDFSSinkConnectorConfig> getRecordWriterSupplier() {
		return new AvroRecordWriterSupplier(avroData);
	}

	@Override
	public SchemaFileReader<HDFSSinkConnectorConfig, Path> getSchemaFileReader() {
		return new AvroFileReader(avroData);
	}

}
