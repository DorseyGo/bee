/**
 * File: TimestampIncrementingOffset.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月12日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description 时间或者增量变量的offset值
 *
 */
public class TimestampIncrementingOffset {
	private static final Logger log = LoggerFactory.getLogger(JDBCSourceTask.class);

	private static final String INCREMENTING_FIELD = "incrementing";
	private static final String TIMESTAMP_FIELD = "timestamp";
	private static final String TIMESTAMP_NANOS_FIELD = "timestamp_nanos";

	private final Long incrementingOffset;
	private final Timestamp timestampOffset;

	/**
	 * @description
	 * @param timestampOffset
	 * @param incrementingOffset
	 */
	public TimestampIncrementingOffset(final Timestamp timestampOffset,
			final Long incrementingOffset) {
		this.timestampOffset = timestampOffset;
		this.incrementingOffset = incrementingOffset;
	}

	public long getIncrementingOffset() {
		return incrementingOffset == null ? -1 : incrementingOffset;
	}

	public Timestamp getTimestampOffset() {
		return timestampOffset != null ? timestampOffset : new Timestamp(0L);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>(3);
		if (incrementingOffset != null) {
			map.put(INCREMENTING_FIELD, incrementingOffset);
		}
		if (timestampOffset != null) {
			map.put(TIMESTAMP_FIELD, timestampOffset.getTime());
			map.put(TIMESTAMP_NANOS_FIELD, (long) timestampOffset.getNanos());
		}
		return map;
	}

	public static TimestampIncrementingOffset fromMap(final Map<String, ?> map) {
		if (map == null || map.isEmpty()) {
			return new TimestampIncrementingOffset(null, null);
		}

		Long incr = (Long) map.get(INCREMENTING_FIELD);
		Long millis = (Long) map.get(TIMESTAMP_FIELD);
		Timestamp ts = null;
		if (millis != null) {
			log.trace("millis is not null");
			ts = new Timestamp(millis);
			Long nanos = (Long) map.get(TIMESTAMP_NANOS_FIELD);
			if (nanos != null) {
				log.trace("Nanos is not null");
				ts.setNanos(nanos.intValue());
			}
		}
		return new TimestampIncrementingOffset(ts, incr);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TimestampIncrementingOffset that = (TimestampIncrementingOffset) o;

		if (incrementingOffset != null ? !incrementingOffset.equals(that.incrementingOffset)
				: that.incrementingOffset != null) {
			return false;
		}
		return timestampOffset != null ? timestampOffset.equals(that.timestampOffset)
				: that.timestampOffset == null;

	}

	@Override
	public int hashCode() {
		int result = incrementingOffset != null ? incrementingOffset.hashCode() : 0;
		result = 31 * result + (timestampOffset != null ? timestampOffset.hashCode() : 0);
		return result;
	}
}
