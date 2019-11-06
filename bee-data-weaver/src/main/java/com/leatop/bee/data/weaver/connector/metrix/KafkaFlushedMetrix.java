/**
 * File: KafkaFlushedMetrix.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月13日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.metrix;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dorsey
 *
 */
public class KafkaFlushedMetrix {

	public final Map<String, AtomicLong> connectorsWithSucceeds = new HashMap<>();
	private static KafkaFlushedMetrix INSTANCE;
	public static final Object lock = new Object();

	private KafkaFlushedMetrix() {
		// empty constructor.
	}
	
	public static KafkaFlushedMetrix getInstance() {
		synchronized (lock) {
			if (INSTANCE == null) {
				INSTANCE = new KafkaFlushedMetrix();
			}
		}
		
		return INSTANCE;
	}
	
	public void increByDelta(final String connectorName, final long delta) {
		synchronized (lock) {
			AtomicLong succeeds = connectorsWithSucceeds.get(connectorName);
			if (succeeds == null) {
				succeeds = new AtomicLong(delta);
				connectorsWithSucceeds.put(connectorName, succeeds);
				
				return;
			}
			
			succeeds.addAndGet(delta);
		}
	}
	
	public Map<String, AtomicLong> unmodifiableMap() {
		return Collections.unmodifiableMap(connectorsWithSucceeds);
	}
	
	public void clear() {
		synchronized (lock) {
			connectorsWithSucceeds.clear();
		}
	}
}
