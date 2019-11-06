/**
 * File: MetrixReportResp.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月13日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.resp;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Dorsey
 *
 */
public class MetrixReportResponse {

	// ~~~ fields
	// ====================================================
	private int statusCode;
	private String phase;
	private static final int STATUS_OK = 0;
	private static final int STATUS_FAIL = 1;

	public static final MetrixReportResponse OK = new MetrixReportResponse(STATUS_OK, null);
	public static final MetrixReportResponse FAILURE = new MetrixReportResponse(STATUS_FAIL, null);

	public MetrixReportResponse() {
		super();
	}

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
	private MetrixReportResponse(final int statusCode, final String phase) {
		this.statusCode = statusCode;
		this.phase = phase;
	}

	// ~~~ methods
	// ====================================================

	/**
	 * @return the statusCode
	 */
	@JsonProperty("statusCode")
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @return the phase
	 */
	@JsonProperty("phase")
	public String getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 *            the phase to set
	 */
	@JsonProperty("phase")
	public void setPhase(final String phase) {
		this.phase = phase;
	}

	/**
	 * @param statusCode
	 *            the statusCode to set
	 */
	@JsonProperty("statusCode")
	public void setStatusCode(final int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Set the phase, and return the current instance.
	 * 
	 * @param phase
	 *            the phase to set
	 * @return the current instance.
	 */
	public MetrixReportResponse withPhase(final String phase) {
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
		if (obj == null || !(obj instanceof MetrixReportResponse))
			return false;

		final MetrixReportResponse other = (MetrixReportResponse) obj;
		return Objects.equals(statusCode, other.statusCode) && Objects.equals(phase, other.phase);
	}
}
