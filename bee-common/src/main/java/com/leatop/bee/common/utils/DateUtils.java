/**
 * File: DateUtils.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 	日期工具类
 * 
 * @author Dorsey
 *
 */
public class DateUtils {

	private static final ThreadLocal<DateFormat> DATE_FORMATTERS = new ThreadLocal<DateFormat>();
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_DATE_IN_YEAR_PATTERN = "yyyy";
	public static final String DEFAULT_DATE_IN_MONTH_PATTERN = "yyyyMM";
	public static final String DEFAULT_DATE_IN_DAY_PATTERN = "yyyyMMdd";
	
	/**
	 * 	日期类型转化为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String format(final Date date) {
		DateFormat dateFormat = DATE_FORMATTERS.get();
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
			DATE_FORMATTERS.set(dateFormat);
		}

		return dateFormat.format(date);
	}
	
	/**
	 * 	将日期类型按指定格式转换为字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(final Date date, final String pattern) {
		final DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	
	/**
	 * Parse the date string via the default date pattern (<tt>yyyy-MM-dd HH:mm:ss</tt>).
	 * 
	 * @throws ParseException if the given date string not comply to date pattern.
	 */
	public static Date parseByDefault(final String src) throws ParseException {
		DateFormat dateFormat = DATE_FORMATTERS.get();
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
			DATE_FORMATTERS.set(dateFormat);
		}
		
		return dateFormat.parse(src);
	}
}
