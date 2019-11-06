/**
 * File: Objects.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月9日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.utils;

import java.util.List;

/**
 * @author Dorsey
 *
 */
public class Objects {

	/**
	 * An {@link IllegalArgumentException} will be araised if the size of
	 * <tt>objs</tt> is less than <tt>threshold</tt>.
	 * 
	 * @param objs the objects.
	 * @param threshold the threshold, to which the size of the objects is compared.
	 * @param message the message when exception raised.
	 */
	public static <T> void requiresGreaterThan(final List<T> objs, final int threshold,
			final String message) {
		if (objs.size() < threshold) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void assertionTrue(final boolean expression) {
		if (!expression) {
			throw new AssertionError();
		}
	}
	
	public static void assertionTrue(final boolean expression, final String message) {
		if (!expression) {
			throw new AssertionError(message);
		}
	}
}
