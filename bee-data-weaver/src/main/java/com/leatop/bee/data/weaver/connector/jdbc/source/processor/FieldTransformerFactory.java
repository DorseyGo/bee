/**
 * File: FieldTransformerFactory.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月14日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source.processor;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.apache.kafka.connect.errors.ConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceTaskConfig;

/**
 * Factory class, which produces kinds of {@link FieldTransformer}.
 * 
 * @author Dorsey
 *
 */
public class FieldTransformerFactory {
	private static final Logger LOG = LoggerFactory.getLogger(FieldTransformerFactory.class);

	/**
	 * Produce the instance of {@link FieldProcess}
	 * 
	 * @return
	 */
	public static <T extends FieldTransformer> T getFieldProcessor(final Class<T> clazz,
			final String srcFieldName, final String derivedFieldName,
			final JDBCSourceTaskConfig config,
			final Map<String, String> originals) {
		try {
			Constructor<T> constrctor = clazz.getDeclaredConstructor(String.class, String.class);
			constrctor.setAccessible(true);
			T instance = constrctor.newInstance(srcFieldName, derivedFieldName);
			instance.configure(config, originals);

			return instance;
		} catch (Exception e) {
			LOG.error("Failed to instantiate the class: {}", clazz.getName(), e);
			throw new ConnectException("Failed to instantiate the class: " + clazz.getName());
		}
	}
}
