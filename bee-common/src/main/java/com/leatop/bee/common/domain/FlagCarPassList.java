/**
 * File: FlagCarPassList.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.domain;

import java.util.Date;
import java.util.Objects;

/**
 * Container which contains desired fields for <code>FlagCarPassList</code>.
 * 
 * @author Dorsey
 *
 */
public class FlagCarPassList extends ETCTrafficData<String> {

	private int recordNo;
	private String picSerialNo;
	private int roadId;
	private int posId;
	private int laneNo;
	private int deviceID;
	private Date opTime;
	private int direction;
	private String vehPlate;
	private String vehColor;
	private int vehSpeed;
	private int vehBodyColorNo;
	private int vehBodyDeepNo;
	private int vehTypeNo;
	private int plateTypeNo;
	private byte[] images;

	/**
	 * Empty constructor.
	 */
	public FlagCarPassList() {
		super();
	}

	/**
	 * @param builder
	 */
	public FlagCarPassList(final Builder builder) {
		if (builder != null) {
			this.recordNo = builder.recordNo;
			this.picSerialNo = builder.picSerialNo;
			this.flagNetRoadID = builder.flagNetRoadID;
			this.flagRoadID = builder.flagRoadID;
			this.roadId = builder.roadId;
			this.flagID = builder.flagID;
			this.posId = builder.posId;
			this.laneNo = builder.laneNo;
			this.deviceID = builder.deviceID;
			this.opTime = builder.opTime;
			this.direction = builder.direction;
			this.vehPlate = builder.vehPlate;
			this.vehColor = builder.vehColor;
			this.vehSpeed = builder.vehSpeed;
			this.vehBodyColorNo = builder.vehBodyColorNo;
			this.vehBodyDeepNo = builder.vehBodyDeepNo;
			this.vehTypeNo = builder.vehTypeNo;
			this.plateTypeNo = builder.plateTypeNo;
			this.images = builder.images;
		}
	}

	public String getId() {
		return this.picSerialNo;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + Objects.hash(picSerialNo);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof FlagCarPassList))
			return false;

		FlagCarPassList other = (FlagCarPassList) obj;
		return super.equals(obj) && Objects.equals(picSerialNo, other.picSerialNo);
	}

	@Override
	public String toString() {
		return "FlagCarPassList:{picSerialNo=" + picSerialNo + ", " + super.toString() + "}";
	}

	/**
	 * @return the recordNo
	 */
	public int getRecordNo() {
		return recordNo;
	}

	/**
	 * @param recordNo
	 *            the recordNo to set
	 */
	public void setRecordNo(final int recordNo) {
		this.recordNo = recordNo;
	}

	/**
	 * @return the picSerialNo
	 */
	public String getPicSerialNo() {
		return picSerialNo;
	}

	/**
	 * @param picSerialNo
	 *            the picSerialNo to set
	 */
	public void setPicSerialNo(final String picSerialNo) {
		this.picSerialNo = picSerialNo;
	}

	/**
	 * @return the posId
	 */
	public int getPosId() {
		return posId;
	}

	/**
	 * @param posId
	 *            the posId to set
	 */
	public void setPosId(final int posId) {
		this.posId = posId;
	}

	/**
	 * @return the laneNo
	 */
	public int getLaneNo() {
		return laneNo;
	}

	/**
	 * @param laneNo
	 *            the laneNo to set
	 */
	public void setLaneNo(final int laneNo) {
		this.laneNo = laneNo;
	}

	/**
	 * @return the deviceID
	 */
	public int getDeviceID() {
		return deviceID;
	}

	/**
	 * @param deviceID
	 *            the deviceID to set
	 */
	public void setDeviceID(final int deviceID) {
		this.deviceID = deviceID;
	}

	/**
	 * @return the opTime
	 */
	public Date getOpTime() {
		return opTime;
	}

	/**
	 * @param opTime
	 *            the opTime to set
	 */
	public void setOpTime(final Date opTime) {
		this.opTime = opTime;
	}

	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(final int direction) {
		this.direction = direction;
	}

	/**
	 * @return the vehPlate
	 */
	public String getVehPlate() {
		return vehPlate;
	}

	/**
	 * @param vehPlate
	 *            the vehPlate to set
	 */
	public void setVehPlate(final String vehPlate) {
		this.vehPlate = vehPlate;
	}

	/**
	 * @return the vehColor
	 */
	public String getVehColor() {
		return vehColor;
	}

	/**
	 * @param vehColor
	 *            the vehColor to set
	 */
	public void setVehColor(final String vehColor) {
		this.vehColor = vehColor;
	}

	/**
	 * @return the vehSpeed
	 */
	public int getVehSpeed() {
		return vehSpeed;
	}

	/**
	 * @param vehSpeed
	 *            the vehSpeed to set
	 */
	public void setVehSpeed(final int vehSpeed) {
		this.vehSpeed = vehSpeed;
	}

	/**
	 * @return the vehBodyColorNo
	 */
	public int getVehBodyColorNo() {
		return vehBodyColorNo;
	}

	/**
	 * @param vehBodyColorNo
	 *            the vehBodyColorNo to set
	 */
	public void setVehBodyColorNo(final int vehBodyColorNo) {
		this.vehBodyColorNo = vehBodyColorNo;
	}

	/**
	 * @return the vehBodyDeepNo
	 */
	public int getVehBodyDeepNo() {
		return vehBodyDeepNo;
	}

	/**
	 * @param vehBodyDeepNo
	 *            the vehBodyDeepNo to set
	 */
	public void setVehBodyDeepNo(final int vehBodyDeepNo) {
		this.vehBodyDeepNo = vehBodyDeepNo;
	}

	/**
	 * @return the vehTypeNo
	 */
	public int getVehTypeNo() {
		return vehTypeNo;
	}

	/**
	 * @param vehTypeNo
	 *            the vehTypeNo to set
	 */
	public void setVehTypeNo(final int vehTypeNo) {
		this.vehTypeNo = vehTypeNo;
	}

	/**
	 * @return the plateTypeNo
	 */
	public int getPlateTypeNo() {
		return plateTypeNo;
	}

	/**
	 * @param plateTypeNo
	 *            the plateTypeNo to set
	 */
	public void setPlateTypeNo(final int plateTypeNo) {
		this.plateTypeNo = plateTypeNo;
	}

	/**
	 * @return the images
	 */
	public byte[] getImages() {
		return images;
	}

	/**
	 * @param images
	 *            the images to set
	 */
	public void setImages(final byte[] images) {
		this.images = images;
	}

	/**
	 * @return the roadId
	 */
	public int getRoadId() {
		return roadId;
	}

	/**
	 * @param roadId
	 *            the roadId to set
	 */
	public void setRoadId(final int roadId) {
		this.roadId = roadId;
	}

	/**
	 * The builder for <code>FlagCarPassList</code>.
	 * 
	 * @author Dorsey
	 *
	 */
	public static class Builder {

		private int recordNo;
		private String picSerialNo;
		private int flagNetRoadID;
		private int flagRoadID;
		private int roadId;
		private int flagID;
		private int posId;
		private int laneNo;
		private int deviceID;
		private Date opTime;
		private int direction;
		private String vehPlate;
		private String vehColor;
		private int vehSpeed;
		private int vehBodyColorNo;
		private int vehBodyDeepNo;
		private int vehTypeNo;
		private int plateTypeNo;
		private byte[] images;

		/**
		 * @param recordNo
		 *            the recordNo to set
		 */
		public Builder setRecordNo(final int recordNo) {
			this.recordNo = recordNo;
			return this;
		}

		/**
		 * @param picSerialNo
		 *            the picSerialNo to set
		 */
		public Builder setPicSerialNo(final String picSerialNo) {
			this.picSerialNo = picSerialNo;
			return this;
		}

		/**
		 * @param flagNetRoadID
		 *            the flagNetRoadID to set
		 */
		public Builder setFlagNetRoadID(final int flagNetRoadID) {
			this.flagNetRoadID = flagNetRoadID;
			return this;
		}

		/**
		 * @param flagRoadID
		 *            the flagRoadID to set
		 */
		public Builder setFlagRoadID(final int flagRoadID) {
			this.flagRoadID = flagRoadID;
			return this;
		}

		/**
		 * @param flagID
		 *            the flagID to set
		 */
		public Builder setFlagID(final int flagID) {
			this.flagID = flagID;
			return this;
		}

		/**
		 * @param posId
		 *            the posId to set
		 */
		public Builder setPosId(final int posId) {
			this.posId = posId;
			return this;
		}

		/**
		 * @param laneNo
		 *            the laneNo to set
		 */
		public Builder setLaneNo(final int laneNo) {
			this.laneNo = laneNo;
			return this;
		}

		/**
		 * @param deviceID
		 *            the deviceID to set
		 */
		public Builder setDeviceID(final int deviceID) {
			this.deviceID = deviceID;
			return this;
		}

		/**
		 * @param opTime
		 *            the opTime to set
		 */
		public Builder setOpTime(final Date opTime) {
			this.opTime = opTime;
			return this;
		}

		/**
		 * @param direction
		 *            the direction to set
		 */
		public Builder setDirection(final int direction) {
			this.direction = direction;
			return this;
		}

		/**
		 * @param vehPlate
		 *            the vehPlate to set
		 */
		public Builder setVehPlate(final String vehPlate) {
			this.vehPlate = vehPlate;
			return this;
		}

		/**
		 * @param vehColor
		 *            the vehColor to set
		 */
		public Builder setVehColor(final String vehColor) {
			this.vehColor = vehColor;
			return this;
		}

		/**
		 * @param vehSpeed
		 *            the vehSpeed to set
		 */
		public Builder setVehSpeed(final int vehSpeed) {
			this.vehSpeed = vehSpeed;
			return this;
		}

		/**
		 * @param vehBodyColorNo
		 *            the vehBodyColorNo to set
		 */
		public Builder setVehBodyColorNo(final int vehBodyColorNo) {
			this.vehBodyColorNo = vehBodyColorNo;
			return this;
		}

		/**
		 * @param vehBodyDeepNo
		 *            the vehBodyDeepNo to set
		 */
		public Builder setVehBodyDeepNo(final int vehBodyDeepNo) {
			this.vehBodyDeepNo = vehBodyDeepNo;
			return this;
		}

		/**
		 * @param vehTypeNo
		 *            the vehTypeNo to set
		 */
		public Builder setVehTypeNo(final int vehTypeNo) {
			this.vehTypeNo = vehTypeNo;
			return this;
		}

		/**
		 * @param plateTypeNo
		 *            the plateTypeNo to set
		 */
		public Builder setPlateTypeNo(final int plateTypeNo) {
			this.plateTypeNo = plateTypeNo;
			return this;
		}

		/**
		 * @param images
		 *            the images to set
		 */
		public Builder setImages(final byte[] images) {
			this.images = images;
			return this;
		}
		
		
		/**
		 * @param roadId the roadId to set
		 */
		public Builder setRoadId(final int roadId) {
			this.roadId = roadId;
			return this;
		}

		public FlagCarPassList build() {
			return new FlagCarPassList(this);
		}
	}
}
