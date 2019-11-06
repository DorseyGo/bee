/**
 * File: ReflectionUtils.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.utils;

import java.lang.reflect.Constructor;

/**
 * @author Dorsey
 *
 */
public class ReflectionUtils {

	public static <T> T newInstance(final Class<T> type) {
		try {
			Constructor<T> constructor = type.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Not allowed new instance of: " + type, e);
		}
	}
	
}
