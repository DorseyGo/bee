/**
 * File: StorageFactory.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月4日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage;

import java.lang.reflect.Constructor;

import org.apache.kafka.connect.errors.ConnectException;

/**
 * @author Dorsey
 *
 */
public class StorageFactory {

	public static <STORAGE extends Storage<CONFIG, ?>, CONFIG> STORAGE getStorage(
			final Class<STORAGE> storageClass, final Class<CONFIG> confClass, final CONFIG config,
			final String url) {
		try {
			Constructor<STORAGE> constr = storageClass
					.getConstructor(new Class<?>[] { confClass, String.class });
			constr.setAccessible(true);
			
			return constr.newInstance(config, url);
		} catch (Exception e) {
			throw new ConnectException(e);
		}
	}
}
