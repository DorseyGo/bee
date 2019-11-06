/**
 * File: Introspectors.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.introspector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.data.supervisor.Introspector;
import com.leatop.bee.data.supervisor.exception.DataInvalidException;

/**
 * An implementation of {@link Introspector}, which maintains a list of
 * {@link Introspector}s.
 * 
 * @author Dorsey
 *
 */
public class Introspectors implements Introspector {

	/**
	 * An array of {@link Introspector}s.
	 */
	private final List<Introspector> introspectors;

	/**
	 * The exception thrown.
	 */
	private DataInvalidException exception;

	/**
	 * Empty constructor of {@link Introspectors}.
	 */
	public Introspectors() {
		this.exception = null;
		this.introspectors = new ArrayList<Introspector>();
	}

	public <ID> void introspect(final TrafficData<ID> data) throws DataInvalidException {
		if (!introspectors.isEmpty()) {
			try {
				for (Introspector introspector : introspectors) {
					introspector.introspect(data);
				}
			} catch (DataInvalidException e) {
				exception = e;
				throw e;
			}
		}
	}

	/**
	 * Register the given <code>introspector</code> to this introspector house.
	 * 
	 * @param introspector
	 */
	public void register(final Introspector introspector) {
		introspectors.add(introspector);
	}

	/**
	 * De-register the given <code>introspector</code> from this introspector
	 * house.
	 * 
	 * @param introspector
	 */
	public void deregister(final Introspector introspector) {
		introspectors.remove(introspector);
	}

	/**
	 * Return an unmodifiable list of {@link Introspector}s.
	 * 
	 * @return the introspectors
	 */
	public List<Introspector> unmodifiableIntrospectors() {
		return Collections.unmodifiableList(introspectors);
	}

	/**
	 * @return the exception
	 */
	public DataInvalidException getException() {
		return exception;
	}
}
