/**
 * File: Time.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author DORSEy
 *
 */
public interface Time {

	/**
	 * Return the current time in miliseconds.
	 * 
	 * @return the current time in miliseconds.
	 */
	long milliseconds();

	/**
	 * @param untilNext
	 */
	void sleep(long untilNext) throws InterruptedException;

	/**
	 * An implementation of {@link Time}, which fetches the local system time.
	 * 
	 * @author DORSEy
	 *
	 */
	public static class SysTime implements Time {

		/**
		 * Empty constructor of {@link SysTime}.
		 */
		public SysTime() {
			// no-op
		}

		@Override
		public long milliseconds() {
			return System.currentTimeMillis();
		}

		@Override
		public void sleep(final long untilNext) throws InterruptedException {
			TimeUnit.MILLISECONDS.sleep(untilNext);
		}

	}

}
