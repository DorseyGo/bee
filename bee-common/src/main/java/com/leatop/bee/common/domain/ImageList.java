/**
 * File: ImageList.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.domain;

import java.util.Date;

/**
 * @author Dorsey
 * @since 0.0.1-SNAPSHOT
 */
@Deprecated
public class ImageList implements TrafficData<String> {

	private int listNo;
	private String serialNo;
	private int flagNetRoadID;
	private int flagRoadID;
	private int flagID;
	private int deviceID;
	private int laneNo;
	private Date opTime;
	private int directionNo;
	private String vehPlate;
	private String vehColor;
	private int vehSpeed;
	private int vehBodyColorNo;
	private int vehBodyDeepNo;
	private int vehTypeNo;
	private int plateTypeNo;
	private String image;

	/**
	 * Empty constructor of {@link ImageList}.
	 */
	public ImageList() {
		super();
	}

	/**
	 * @return the listNo
	 */
	public int getListNo() {
		return listNo;
	}

	/**
	 * @param listNo
	 *            the listNo to set
	 */
	public void setListNo(final int listNo) {
		this.listNo = listNo;
	}

	/**
	 * @return the serialNo
	 */
	public String getSerialNo() {
		return serialNo;
	}

	/**
	 * @param serialNo
	 *            the serialNo to set
	 */
	public void setSerialNo(final String serialNo) {
		this.serialNo = serialNo;
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
	 * @return the roadID
	 */
	public int getFlagRoadID() {
		return flagRoadID;
	}

	/**
	 * @param roadID
	 *            the roadID to set
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
	 * @return the optime
	 */
	public Date getOpTime() {
		return opTime;
	}

	/**
	 * @param optime
	 *            the optime to set
	 */
	public void setOpTime(final Date opTime) {
		this.opTime = opTime;
	}

	/**
	 * @return the directionNo
	 */
	public int getDirectionNo() {
		return directionNo;
	}

	/**
	 * @param directionNo
	 *            the directionNo to set
	 */
	public void setDirectionNo(final int directionNo) {
		this.directionNo = directionNo;
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
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(final String image) {
		this.image = image;
	}

	public String getId() {
		return this.serialNo;
	}

	@Override
	public String toString() {
		return "ImageList [listNo=" + listNo + ", serialNo=" + serialNo + ", flagNetRoadID="
				+ flagNetRoadID + ", flagRoadID=" + flagRoadID + ", flagID=" + flagID + ", deviceID="
				+ deviceID + ", laneNo=" + laneNo + ", optime=" + opTime + ", directionNo="
				+ directionNo + ", vehPlate=" + vehPlate + ", vehColor=" + vehColor + ", vehSpeed="
				+ vehSpeed + ", vehBodyColorNo=" + vehBodyColorNo + ", vehBodyDeepNo="
				+ vehBodyDeepNo + ", vehTypeNo=" + vehTypeNo + ", plateTypeNo=" + plateTypeNo + "]";
	}

	/**
	 * Builder of {@link ImageList}.
	 * 
	 * @author Dorsey
	 *
	 */
	public static class ImageListBuilder {

		private final ImageList imageList;

		public ImageListBuilder() {
			this.imageList = new ImageList();
		}

		public ImageListBuilder withListNo(final int listNo) {
			this.imageList.listNo = listNo;
			return this;
		}

		public ImageListBuilder setSerialNo(final String serialNo) {
			this.imageList.serialNo = serialNo;
			return this;
		}

		/**
		 * @param flagNetRoadID
		 *            the flagNetRoadID to set
		 */
		public ImageListBuilder withFlagNetRoadID(final int flagNetRoadID) {
			this.imageList.flagNetRoadID = flagNetRoadID;
			return this;
		}

		/**
		 * @param roadID
		 *            the roadID to set
		 */
		public ImageListBuilder withRoadID(final int flagRoadID) {
			this.imageList.flagRoadID = flagRoadID;
			return this;
		}

		public ImageListBuilder setFlagID(final int flagID) {
			this.imageList.flagID = flagID;
			return this;
		}

		/**
		 * @param deviceID
		 *            the deviceID to set
		 */
		public ImageListBuilder setDeviceID(final int deviceID) {
			this.imageList.deviceID = deviceID;
			return this;
		}

		/**
		 * @param laneNo
		 *            the laneNo to set
		 */
		public ImageListBuilder setLaneNo(final int laneNo) {
			this.imageList.laneNo = laneNo;
			return this;
		}

		/**
		 * @param optime
		 *            the optime to set
		 */
		public ImageListBuilder withOpTime(final Date opTime) {
			this.imageList.opTime = opTime;
			return this;
		}

		/**
		 * @param directionNo
		 *            the directionNo to set
		 */
		public ImageListBuilder withDirectionNo(final int directionNo) {
			this.imageList.directionNo = directionNo;
			return this;
		}

		/**
		 * @param vehPlate
		 *            the vehPlate to set
		 */
		public ImageListBuilder withVehPlate(final String vehPlate) {
			this.imageList.vehPlate = vehPlate;
			return this;
		}

		/**
		 * @param vehColor
		 *            the vehColor to set
		 */
		public ImageListBuilder withVehColor(final String vehColor) {
			this.imageList.vehColor = vehColor;
			return this;
		}

		/**
		 * @param vehSpeed
		 *            the vehSpeed to set
		 */
		public ImageListBuilder withVehSpeed(final int vehSpeed) {
			this.imageList.vehSpeed = vehSpeed;
			return this;
		}

		/**
		 * @param vehBodyColorNo
		 *            the vehBodyColorNo to set
		 */
		public ImageListBuilder withVehBodyColorNo(final int vehBodyColorNo) {
			this.imageList.vehBodyColorNo = vehBodyColorNo;
			return this;
		}

		/**
		 * @param vehBodyDeepNo
		 *            the vehBodyDeepNo to set
		 */
		public ImageListBuilder withVehBodyDeepNo(final int vehBodyDeepNo) {
			this.imageList.vehBodyDeepNo = vehBodyDeepNo;
			return this;
		}

		/**
		 * @param vehTypeNo
		 *            the vehTypeNo to set
		 */
		public ImageListBuilder withVehTypeNo(final int vehTypeNo) {
			this.imageList.vehTypeNo = vehTypeNo;
			return this;
		}

		/**
		 * @param plateTypeNo
		 *            the plateTypeNo to set
		 */
		public ImageListBuilder withPlateTypeNo(final int plateTypeNo) {
			this.imageList.plateTypeNo = plateTypeNo;
			return this;
		}

		/**
		 * @param image
		 *            the image to set
		 */
		public ImageListBuilder withImage(final String image) {
			this.imageList.image = image;
			return this;
		}

		public ImageList build() {
			return this.imageList;
		}
	}
}
