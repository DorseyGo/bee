/**
 * File: JsonFormatter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format.formatters;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.fs.Path;
import org.apache.kafka.connect.json.JsonConverter;

import com.leatop.bee.data.weaver.connector.hdfs.config.Constants;
import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.storage.HDFSStorage;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.Formatter;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.RecordWriterSupplier;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.SchemaFileReader;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.supplier.JsonRecordWriterSupplier;

/**
 * @author Dorsey
 *
 */
public class JsonFormatter implements Formatter<HDFSSinkConnectorConfig, Path> {

	private final JsonConverter converter;

	public JsonFormatter(final HDFSStorage storage) {
		this.converter = new JsonConverter();
		Map<String, Object> config = new HashMap<>();
		config.put(Constants.SCHEMAS_ENABLE_CONFIG, Constants.FALSE_STR);
		config.put(Constants.SCHEMAS_CACHE_SIZE_CONFIG, String.valueOf(
				storage.getConfig().getInt(HDFSSinkConnectorConfig.SCHEMA_CACHE_SIZE_CONFIG)));
		
		converter.configure(config, false);
	}

	@Override
	public RecordWriterSupplier<HDFSSinkConnectorConfig> getRecordWriterSupplier() {
		return new JsonRecordWriterSupplier(converter);
	}

	@Override
	public SchemaFileReader<HDFSSinkConnectorConfig, Path> getSchemaFileReader() {
		return null;
	}

}
