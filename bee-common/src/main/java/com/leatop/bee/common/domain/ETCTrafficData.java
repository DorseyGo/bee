/**
 * File: ETCTrafficData.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.domain;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Dorsey
 *
 */
abstract class ETCTrafficData<ID> implements TrafficData<ID> {

	@JsonProperty("FlagNetRoadID")
	protected int flagNetRoadID;
	
	@JsonProperty("FlagRoadID")
	protected int flagRoadID;
	
	@JsonProperty("FlagID")
	protected int flagID;

	/**
	 * Empty constructor.
	 */
	protected ETCTrafficData() {
		// empty for initialization
	}

	/**
	 * @return the flagNetRoadID
	 */
	public int getFlagNetRoadID() {
		return flagNetRoadID;
	}

	/**
	 * @param flagNetRoadID
	 *            the flagNetRoadID to set
	 */
	public void setFlagNetRoadID(final int flagNetRoadID) {
		this.flagNetRoadID = flagNetRoadID;
	}

	/**
	 * @return the flagRoadID
	 */
	public int getFlagRoadID() {
		return flagRoadID;
	}

	/**
	 * @param flagRoadID
	 *            the flagRoadID to set
	 */
	public void setFlagRoadID(final int flagRoadID) {
		this.flagRoadID = flagRoadID;
	}

	/**
	 * @return the flagID
	 */
	public int getFlagID() {
		return flagID;
	}

	/**
	 * @param flagID
	 *            the flagID to set
	 */
	public void setFlagID(final int flagID) {
		this.flagID = flagID;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;

		result = PRIME * result + flagID;
		result = PRIME * result + flagNetRoadID;
		result = PRIME * result + flagRoadID;

		return result;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof ETCTrafficData))
			return false;

		ETCTrafficData other = (ETCTrafficData) obj;

		return (flagID == other.flagID && flagNetRoadID == other.flagNetRoadID
				&& flagRoadID == other.flagRoadID);
	}

	@Override
	public String toString() {
		return "flagNetRoadID=" + flagNetRoadID + ", flagRoadID=" + flagRoadID + ", flagID="
				+ flagID;
	}

}
