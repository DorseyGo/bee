/**
 * File: TableNameGeneratorFactory.java
 * Author: DORSEy Q F TANG
 * Created: 2019年7月9日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.generator;

import java.lang.reflect.Constructor;

import org.apache.kafka.common.config.ConfigException;

import com.leatop.bee.data.weaver.connector.jdbc.sink.JDBCSinkConfig;

/**
 * @author Dorsey
 *
 */
public class TableNameGeneratorFactory {

	/**
	 * Factory method, which produces the generator which is responsible for
	 * generating table name accordingly.
	 * 
	 * @param <T>
	 * @param clazz
	 * @param config
	 * @return
	 */
	public static <T extends TableNameGenerator> T getGenerator(final Class<T> clazz,
			final JDBCSinkConfig config) {
		try {
			final Constructor<T> constructor = clazz.getConstructor(JDBCSinkConfig.class);
			constructor.setAccessible(true);
			return constructor.newInstance(config);
		} catch (Exception e) {
			throw new ConfigException("Can't instantiate " + clazz.getName());
		}
	}
}
