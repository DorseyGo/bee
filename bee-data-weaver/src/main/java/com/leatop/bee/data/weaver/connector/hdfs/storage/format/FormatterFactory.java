/**
 * File: FormatterFactory.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月11日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format;

import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.common.config.ConfigException;

import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.storage.Storage;

/**
 * @author Dorsey
 *
 */
public class FormatterFactory {

	public static <T extends Formatter<HDFSSinkConnectorConfig, Path>> T getFormatter(
			final Class<T> clazz,
			final Storage<HDFSSinkConnectorConfig, List<FileStatus>> storage) {
		try {
			Constructor<T> constrctor = clazz.getConstructor(storage.getClass());
			return constrctor.newInstance(storage);
		} catch (Exception e) {
			throw new ConfigException(
					"Failed to instantiate the formatter with name: " + clazz.getName());
		}
	}
}
