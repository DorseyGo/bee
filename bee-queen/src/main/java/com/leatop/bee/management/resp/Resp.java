/**
 * File: Resp.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.resp;

import java.util.Objects;

/**
 * Acts as a container which contains the fields for response representation.
 * 
 * @author Dorsey
 *
 */
public class Resp {

	// ~~~ fields
	// ====================================================
	private final int statusCode;
	private String phase;
	private static final int STATUS_OK = 0;
	private static final int STATUS_FAIL = 1;

	public static final Resp OK = new Resp(STATUS_OK, null);
	public static final Resp FAILURE = new Resp(STATUS_FAIL, null);

	// ~~~ constructors
	// ====================================================
	/**
	 * Sole constructor of {@link Resp}, with status code and phase specified.
	 * 
	 * @param statusCode
	 *            the status code.
	 * @param phase
	 *            the phase.
	 */
	private Resp(final int statusCode, final String phase) {
		this.statusCode = statusCode;
		this.phase = phase;
	}

	// ~~~ methods
	// ====================================================

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @return the phase
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * Set the phase, and return the current instance.
	 * 
	 * @param phase
	 *            the phase to set
	 * @return the current instance.
	 */
	public Resp withPhase(final String phase) {
		this.phase = phase;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(statusCode, phase);
	}

	@Override
	public String toString() {
		return "Resp [statusCode=" + statusCode + ", phase=" + phase + "]";
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Resp))
			return false;

		final Resp other = (Resp) obj;
		return Objects.equals(statusCode, other.statusCode) && Objects.equals(phase, other.phase);
	}

}