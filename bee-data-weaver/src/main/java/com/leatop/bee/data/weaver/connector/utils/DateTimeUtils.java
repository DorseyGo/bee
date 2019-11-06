package com.leatop.bee.data.weaver.connector.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Utility for operation on date.
 * 
 * @author Dorsey
 *
 */
public class DateTimeUtils {

	private static final ThreadLocal<Map<TimeZone, Calendar>> TIMEZONE_CALENDARS = new ThreadLocal<Map<TimeZone, Calendar>>() {
		@Override
		protected java.util.Map<TimeZone, Calendar> initialValue() {
			return new HashMap<TimeZone, Calendar>();
		}
	};

	private static final ThreadLocal<Map<TimeZone, SimpleDateFormat>> TIMEZONE_DATE_FORMATS = new ThreadLocal<Map<TimeZone, SimpleDateFormat>>() {
		@Override
		protected java.util.Map<TimeZone, SimpleDateFormat> initialValue() {
			return new HashMap<TimeZone, SimpleDateFormat>();
		};
	};

	private static final ThreadLocal<Map<TimeZone, SimpleDateFormat>> TIMEZONE_TIME_FORMATS = new ThreadLocal<Map<TimeZone, SimpleDateFormat>>() {
		@Override
		protected Map<TimeZone, SimpleDateFormat> initialValue() {
			return new HashMap<TimeZone, SimpleDateFormat>();
		}
	};

	private static final ThreadLocal<Map<TimeZone, SimpleDateFormat>> TIMEZONE_TIMESTAMP_FORMATS = new ThreadLocal<Map<TimeZone, SimpleDateFormat>>() {
		@Override
		protected Map<TimeZone, SimpleDateFormat> initialValue() {
			return new HashMap<TimeZone, SimpleDateFormat>();
		}
	};

	public static Calendar getTimeZoneCalendar(final TimeZone timeZone) {
		Calendar cal = null;
		if ((cal = TIMEZONE_CALENDARS.get().get(timeZone)) == null) {
			GregorianCalendar calendar = new GregorianCalendar();
			TIMEZONE_CALENDARS.get().put(timeZone, calendar);

			return calendar;
		}

		return cal;
	}

	public static String formatDate(final Date date, final TimeZone timeZone) {
		return TIMEZONE_DATE_FORMATS.get().computeIfAbsent(timeZone, aTimeZone -> {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(aTimeZone);
			return sdf;
		}).format(date);
	}

	public static String formatTime(final Date date, final TimeZone timeZone) {
		return TIMEZONE_TIME_FORMATS.get().computeIfAbsent(timeZone, aTimeZone -> {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
			sdf.setTimeZone(aTimeZone);
			return sdf;
		}).format(date);
	}

	public static String formatTimestamp(final Date date, final TimeZone timeZone) {
		return TIMEZONE_TIMESTAMP_FORMATS.get().computeIfAbsent(timeZone, aTimeZone -> {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			sdf.setTimeZone(aTimeZone);
			return sdf;
		}).format(date);
	}

	private DateTimeUtils() {
	}
}
