/**
 * File: RecordCounter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月12日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.domain;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dorsey
 *
 */
public class RecordCounter {

	// ~~~~ Fields
	// =====================================================================================
	private final Map<String, AtomicLong> success = new ConcurrentHashMap<>();
	private final Map<String, AtomicLong> failed = new ConcurrentHashMap<String, AtomicLong>();
	private final Map<String, AtomicLong> all = new ConcurrentHashMap<String, AtomicLong>();
	private static RecordCounter INSTANCE;
	public static final Object lock = new Object();

	/**
	 * Constructor.
	 */
	private RecordCounter() {
		// sole
	}
	
	public static RecordCounter getInstance() {
		synchronized (lock) {
			if (INSTANCE == null) {
				INSTANCE = new RecordCounter();
			}
		}
		
		return INSTANCE;
	}

	// ~~~~ Methods
	// =====================================================================================
	public void increSuccessCount(final String subject) {
		increByOne(subject, success);
	}

	public long getSuccessCount(final String subject) {
		return get(subject, success);
	}

	public void increFailedCount(final String subject) {
		increByOne(subject, failed);
	}

	public long getFailedCount(final String subject) {
		return get(subject, failed);
	}

	private void increByOne(final String subject, final Map<String, AtomicLong> registry) {
		synchronized (lock) {
			AtomicLong counter = registry.get(subject);
			if (counter == null) {
				counter = new AtomicLong(1);
				registry.put(subject, counter);
				all.put(subject, counter);
	
				return;
			}
	
	//		counter.addAndGet(1L);
			// summary
			all.get(subject).addAndGet(1L);
			return;
		}
	}

	private long get(final String subject, final Map<String, AtomicLong> registry) {
		AtomicLong counter = registry.get(subject);

		return (counter == null) ? 0L : counter.get();
	}
	
	public Map<String, AtomicLong> unmodifiableAlls() {
		return Collections.unmodifiableMap(all);
	}
	
	public Map<String, AtomicLong> unmodifiableSuccess() {
		return Collections.unmodifiableMap(success);
	}
	
	public Map<String, AtomicLong> unmodifiableFails() {
		return Collections.unmodifiableMap(failed);
	}
	
	public void clear() {
		synchronized (lock) {
			success.clear();
			failed.clear();
			all.clear();
		}
	}
}
