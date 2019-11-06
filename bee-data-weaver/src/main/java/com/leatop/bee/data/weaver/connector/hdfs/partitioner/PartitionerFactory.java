/**
 * File: PartitionerFactory.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月11日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.partitioner;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.apache.kafka.common.config.ConfigException;

/**
 * The factory class, which produces the {@link Partitioner} as required.
 * 
 * @author Dorsey
 *
 */
public class PartitionerFactory {

	public static <T extends Partitioner> T getPartitioner(final Class<T> clazz,
			final Map<String, Object> config) {
		try {
			Constructor<T> constrctor = clazz.getConstructor();
			T instance = constrctor.newInstance();
			instance.configure(config);

			return instance;
		} catch (Exception e) {
			throw new ConfigException(
					"Error in instantiate the partition class: " + clazz.getName());
		}
	}
}
