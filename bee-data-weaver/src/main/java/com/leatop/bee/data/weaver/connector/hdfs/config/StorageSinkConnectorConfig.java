/**
 * File: SinkConnectorConfig.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月3日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.config;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Width;

import com.leatop.bee.data.weaver.connector.hdfs.storage.HDFSStorage;

/**
 * @author Dorsey
 *
 */
public class StorageSinkConnectorConfig extends AbstractConfig implements ComposableConfig {

	public static final String SCHEMA_CACHE_SIZE_CONFIG = "schema.cache.size";
	public static final int SCHEMA_CACHE_SIZE_DEFAULT = 1000;
	public static final String SCHEMA_CACHE_SIZE_DOC = "the size of schema in avro converter";
	public static final String SCHEMA_CACHE_SIZE_DISPLAY = "schema cache size";

	public static final String TOPICS_FOLDER_CONFIG = "topics.folder";
	public static final String TOPICS_FOLDER_DOC = "topics folder";
	public static final String TOPICS_FOLDER_DEFAULT = "topics";
	public static final String TOPICS_FOLDER_DISPLAY = "topics folder";

	public static final String STORAGE_CLASS_CONFIG = "storage.class";
	public static final String STORAGE_CLASS_DEFAULT = HDFSStorage.class.getName();
	public static final String STORAGE_CLASS_DOC = "the uderlying storage layer";
	public static final String STORAGE_CLASS_DISPLAY = "storage class";

	public static final String FOLDER_DELIMITER_CONFIG = "folder.delimiter";
	public static final String FOLDER_DELIMITER_DOC = "Folder delimiter pattern";
	public static final String FOLDER_DELIMITER_DEFAULT = "/";
	public static final String FOLDER_DELIMITER_DISPLAY = "Folder Delimiter";

	public static final String FILE_DELIMITER_CONFIG = "file.delimiter";
	public static final String FILE_DELIMITER_DOC = "File delimiter pattern";
	public static final String FILE_DELIMITER_DEFAULT = "+";
	public static final String FILE_DELIMITER_DISPLAY = "File Delimiter";

	public static final String SCHEMA_COMPABILITY_CONFIG = "schema.compability";
	public static final String SCHEMA_COMPABILITY_DOC = "The schema compability";
	public static final String SCHEMA_COMPABILITY_DEFAULT = "NONE";
	public static final String SCHEMA_COMPABILITY_DISPLAY = "schema compability";

	public static final String FILENAME_OFFSET_ZERO_PAD_WIDTH_CONFIG = "filename.offset.zero.pad.width";
	public static final String FILENAME_OFFSET_ZERO_PAD_WIDTH_DOC = "";
	public static final int FILENAME_OFFSET_ZERO_PAD_WIDTH_DEFAULT = 10;
	public static final String FILENAME_OFFSET_ZERO_PAD_WIDTH_DISPLAY = "";

	public static final String BATCH_SIZE_CONFIG = "batch.size";
	public static final int BATCH_SIZE_DEFAULT = 3000;
	public static final String BATCH_SIZE_DOC = "Number of records before to commit to underlying HDFS. 3000, by default.";
	public static final String BATCH_SIZE_DISPLAY = "batch size";

	public static final String TIMEOUT_IN_MS_CONFIG = "timeout.ms";
	public static final long TIMEOUT_IN_MS_DEFAULT = 5000L;
	public static final String TIMEOUT_IN_MS_DOC = "Retry back off in milliseconds.";
	public static final String TIMEOUT_IN_MS_DISPLAY = "time out";

	public static final String FORMATTER_CLASS_CONFIG = "formatter.class";
	public static final String FORMATTER_CLASS_DOC = "indicating in which format the data will be written";
	public static final String FORMATTER_CLASS_DISPLAY = "formatter class";

	public static final String AVRO_CODEC_CONFIG = "avro.codec";
	public static final String AVRO_CODEC_DEFAULT = "null";
	public static final String AVRO_CODEC_DISPLAY = "Avro Compression Codec";
	public static final String AVRO_CODEC_DOC = "The Avro compression codec (null, deflate, snappy and bzip2)";

	// ~~~ constructors
	// =========================================================================================
	public StorageSinkConnectorConfig(final ConfigDef definition, final Map<?, ?> originals) {
		super(definition, originals);
	}

	// ~~~ methods
	// =========================================================================================
	public static ConfigDef newConfigDef(final ConfigDef.Recommender formatterClassRecommender,
			final ConfigDef.Recommender storageClassRecommender) {
		ConfigDef configDef = new ConfigDef();
		{
			final String group = "Connector";
			int orderInGroup = 0;

			configDef.define(FORMATTER_CLASS_CONFIG, Type.CLASS, Importance.HIGH,
					FORMATTER_CLASS_DOC, group, ++orderInGroup, Width.NONE, FORMATTER_CLASS_DISPLAY,
					formatterClassRecommender);

			configDef.define(BATCH_SIZE_CONFIG, Type.INT, BATCH_SIZE_DEFAULT, Importance.LOW,
					BATCH_SIZE_DOC, group, ++orderInGroup, Width.LONG, BATCH_SIZE_DISPLAY);

			configDef.define(SCHEMA_CACHE_SIZE_CONFIG, Type.INT, SCHEMA_CACHE_SIZE_DEFAULT,
					Importance.LOW, SCHEMA_CACHE_SIZE_DOC, group, ++orderInGroup, Width.LONG,
					SCHEMA_CACHE_SIZE_DISPLAY);

			configDef.define(TIMEOUT_IN_MS_CONFIG, Type.LONG, TIMEOUT_IN_MS_DEFAULT, Importance.LOW,
					TIMEOUT_IN_MS_DOC, group, ++orderInGroup, Width.MEDIUM, TIMEOUT_IN_MS_DISPLAY);

			configDef.define(FILENAME_OFFSET_ZERO_PAD_WIDTH_CONFIG, Type.INT,
					FILENAME_OFFSET_ZERO_PAD_WIDTH_DEFAULT, ConfigDef.Range.atLeast(0),
					Importance.LOW, FILENAME_OFFSET_ZERO_PAD_WIDTH_DOC, group, ++orderInGroup,
					Width.LONG, FILENAME_OFFSET_ZERO_PAD_WIDTH_DISPLAY);
		}

		// configs for storage.
		{
			final String group = "Storage";
			int orderInGroup = 0;

			configDef.define(STORAGE_CLASS_CONFIG, Type.CLASS, Importance.HIGH, STORAGE_CLASS_DOC,
					group, ++orderInGroup, Width.NONE, STORAGE_CLASS_DISPLAY,
					storageClassRecommender);

			configDef.define(TOPICS_FOLDER_CONFIG, Type.STRING, TOPICS_FOLDER_DEFAULT,
					Importance.HIGH, TOPICS_FOLDER_DOC, group, ++orderInGroup, Width.NONE,
					TOPICS_FOLDER_DISPLAY);

			configDef.define(FOLDER_DELIMITER_CONFIG, Type.STRING, FOLDER_DELIMITER_DEFAULT,
					Importance.MEDIUM, FOLDER_DELIMITER_DOC, group, ++orderInGroup, Width.LONG,
					FOLDER_DELIMITER_DISPLAY);

			configDef.define(FILE_DELIMITER_CONFIG, Type.STRING, FILE_DELIMITER_DEFAULT,
					Importance.MEDIUM, FILE_DELIMITER_DOC, group, ++orderInGroup, Width.LONG,
					FILE_DELIMITER_DISPLAY);
		}

		{
			// for avro
			final String group = "avro";
			int orderInGroup = 0;

			configDef.define(AVRO_CODEC_CONFIG, Type.STRING, AVRO_CODEC_DEFAULT, Importance.MEDIUM,
					AVRO_CODEC_DOC, group, ++orderInGroup, Width.LONG, AVRO_CODEC_DISPLAY);
		}

		{
			// for avro
			final String group = "Schema";
			int orderInGroup = 0;

			configDef.define(SCHEMA_COMPABILITY_CONFIG, Type.STRING, SCHEMA_COMPABILITY_DEFAULT,
					Importance.HIGH, SCHEMA_COMPABILITY_DOC, group, ++orderInGroup, Width.SHORT,
					SCHEMA_COMPABILITY_DISPLAY);
		}

		return configDef;
	}

	@Override
	public Object get(final String key) {
		return super.get(key);
	}

	public String getAvroCodec() {
		return getString(AVRO_CODEC_CONFIG);
	}
}
