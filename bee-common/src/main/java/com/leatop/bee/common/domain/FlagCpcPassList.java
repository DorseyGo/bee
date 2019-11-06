/**
 * File: FlagCpcPassList.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.domain;

import java.util.Date;
import java.util.Objects;

/**
 * Container which contains the desired fields for <code>FlagCpcPassList</code>.
 * 
 * @author Dorsey
 *
 */
public class FlagCpcPassList extends ETCTrafficData<String> {

	private int recordNo;
	private String serialNo;
	private int posId;
	private int laneNo;
	private int enRoadNo;
	private int enStationNo;
	private int enLaneNo;
	private Date enTime;
	private String etcTermCode;
	private String iccIssuer;
	private String cpcCardID;
	private String cpuCardID;
	private int vehicleType;
	private String vehPlate;
	private String vehColor;
	private Date opTime;
	private int direction;
	private int vehStatus;
	private int tollAmount;
	private int discountFee;
	private int tollVersion;
	private int cpcElectrica;
	private String picSerialNo;
	private int verifyCode;
	private int spare1;
	private int spare2;
	private String spare3;
	private String spare4;

	/**
	 * @param builder
	 */
	public FlagCpcPassList(final Builder builder) {
		if (builder != null) {
			this.recordNo = builder.recordNo;
			this.serialNo = builder.serialNo;
			this.flagNetRoadID = builder.flagNetRoadID;
			this.flagRoadID = builder.flagRoadID;
			this.flagID = builder.flagID;
			this.posId = builder.posId;
			this.laneNo = builder.laneNo;
			this.enRoadNo = builder.enRoadNo;
			this.enStationNo = builder.enStationNo;
			this.enLaneNo = builder.enLaneNo;
			this.enTime = builder.enTime;
			this.etcTermCode = builder.etcTermCode;
			this.iccIssuer = builder.iccIssuer;
			this.cpcCardID = builder.cpcCardID;
			this.cpuCardID = builder.cpuCardID;
			this.vehicleType = builder.vehicleType;
			this.vehPlate = builder.vehPlate;
			this.vehColor = builder.vehColor;
			this.opTime = builder.opTime;
			this.direction = builder.direction;
			this.vehStatus = builder.vehStatus;
			this.tollAmount = builder.tollAmount;
			this.discountFee = builder.discountFee;
			this.tollVersion = builder.tollVersion;
			this.cpcElectrica = builder.cpcElectrica;
			this.picSerialNo = builder.picSerialNo;
			this.verifyCode = builder.verifyCode;
			this.spare1 = builder.spare1;
			this.spare2 = builder.spare2;
			this.spare3 = builder.spare3;
			this.spare4 = builder.spare4;
		}
	}

	public String getId() {
		return this.serialNo;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + Objects.hash(serialNo);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof FlagCpcPassList))
			return false;

		FlagCpcPassList other = (FlagCpcPassList) obj;
		return super.equals(obj) && Objects.equals(serialNo, other.serialNo);
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
	 * @return the enRoadNo
	 */
	public int getEnRoadNo() {
		return enRoadNo;
	}

	/**
	 * @param enRoadNo
	 *            the enRoadNo to set
	 */
	public void setEnRoadNo(final int enRoadNo) {
		this.enRoadNo = enRoadNo;
	}

	/**
	 * @return the enStationNo
	 */
	public int getEnStationNo() {
		return enStationNo;
	}

	/**
	 * @param enStationNo
	 *            the enStationNo to set
	 */
	public void setEnStationNo(final int enStationNo) {
		this.enStationNo = enStationNo;
	}

	/**
	 * @return the enLaneNo
	 */
	public int getEnLaneNo() {
		return enLaneNo;
	}

	/**
	 * @param enLaneNo
	 *            the enLaneNo to set
	 */
	public void setEnLaneNo(final int enLaneNo) {
		this.enLaneNo = enLaneNo;
	}

	/**
	 * @return the enTime
	 */
	public Date getEnTime() {
		return enTime;
	}

	/**
	 * @param enTime
	 *            the enTime to set
	 */
	public void setEnTime(final Date enTime) {
		this.enTime = enTime;
	}

	/**
	 * @return the etcTermCode
	 */
	public String getEtcTermCode() {
		return etcTermCode;
	}

	/**
	 * @param etcTermCode
	 *            the etcTermCode to set
	 */
	public void setEtcTermCode(final String etcTermCode) {
		this.etcTermCode = etcTermCode;
	}

	/**
	 * @return the iccIssuer
	 */
	public String getIccIssuer() {
		return iccIssuer;
	}

	/**
	 * @param iccIssuer
	 *            the iccIssuer to set
	 */
	public void setIccIssuer(final String iccIssuer) {
		this.iccIssuer = iccIssuer;
	}

	/**
	 * @return the cpcCardID
	 */
	public String getCpcCardID() {
		return cpcCardID;
	}

	/**
	 * @param cpcCardID
	 *            the cpcCardID to set
	 */
	public void setCpcCardID(final String cpcCardID) {
		this.cpcCardID = cpcCardID;
	}

	/**
	 * @return the cpuCardID
	 */
	public String getCpuCardID() {
		return cpuCardID;
	}

	/**
	 * @param cpuCardID
	 *            the cpuCardID to set
	 */
	public void setCpuCardID(final String cpuCardID) {
		this.cpuCardID = cpuCardID;
	}

	/**
	 * @return the vehicleType
	 */
	public int getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType
	 *            the vehicleType to set
	 */
	public void setVehicleType(final int vehicleType) {
		this.vehicleType = vehicleType;
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
	 * @return the vehStatus
	 */
	public int getVehStatus() {
		return vehStatus;
	}

	/**
	 * @param vehStatus
	 *            the vehStatus to set
	 */
	public void setVehStatus(final int vehStatus) {
		this.vehStatus = vehStatus;
	}

	/**
	 * @return the tollAmount
	 */
	public int getTollAmount() {
		return tollAmount;
	}

	/**
	 * @param tollAmount
	 *            the tollAmount to set
	 */
	public void setTollAmount(final int tollAmount) {
		this.tollAmount = tollAmount;
	}

	/**
	 * @return the discountFee
	 */
	public int getDiscountFee() {
		return discountFee;
	}

	/**
	 * @param discountFee
	 *            the discountFee to set
	 */
	public void setDiscountFee(final int discountFee) {
		this.discountFee = discountFee;
	}

	/**
	 * @return the tollVersion
	 */
	public int getTollVersion() {
		return tollVersion;
	}

	/**
	 * @param tollVersion
	 *            the tollVersion to set
	 */
	public void setTollVersion(final int tollVersion) {
		this.tollVersion = tollVersion;
	}

	/**
	 * @return the cpcElectrica
	 */
	public int getCpcElectrica() {
		return cpcElectrica;
	}

	/**
	 * @param cpcElectrica
	 *            the cpcElectrica to set
	 */
	public void setCpcElectrica(final int cpcElectrica) {
		this.cpcElectrica = cpcElectrica;
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
	 * @return the verifyCode
	 */
	public int getVerifyCode() {
		return verifyCode;
	}

	/**
	 * @param verifyCode
	 *            the verifyCode to set
	 */
	public void setVerifyCode(final int verifyCode) {
		this.verifyCode = verifyCode;
	}

	/**
	 * @return the spare1
	 */
	public int getSpare1() {
		return spare1;
	}

	/**
	 * @param spare1
	 *            the spare1 to set
	 */
	public void setSpare1(final int spare1) {
		this.spare1 = spare1;
	}

	/**
	 * @return the spare2
	 */
	public int getSpare2() {
		return spare2;
	}

	/**
	 * @param spare2
	 *            the spare2 to set
	 */
	public void setSpare2(final int spare2) {
		this.spare2 = spare2;
	}

	/**
	 * @return the spare3
	 */
	public String getSpare3() {
		return spare3;
	}

	/**
	 * @param spare3
	 *            the spare3 to set
	 */
	public void setSpare3(final String spare3) {
		this.spare3 = spare3;
	}

	/**
	 * @return the spare4
	 */
	public String getSpare4() {
		return spare4;
	}

	/**
	 * @param spare4
	 *            the spare4 to set
	 */
	public void setSpare4(final String spare4) {
		this.spare4 = spare4;
	}

	/**
	 * The builder for <code>FlagCpcPassList</code>.
	 * 
	 * @author Dorsey
	 *
	 */
	public static class Builder {

		private int recordNo;
		private String serialNo;
		private int flagNetRoadID;
		private int flagRoadID;
		private int flagID;
		private int posId;
		private int laneNo;
		private int enRoadNo;
		private int enStationNo;
		private int enLaneNo;
		private Date enTime;
		private String etcTermCode;
		private String iccIssuer;
		private String cpcCardID;
		private String cpuCardID;
		private int vehicleType;
		private String vehPlate;
		private String vehColor;
		private Date opTime;
		private int direction;
		private int vehStatus;
		private int tollAmount;
		private int discountFee;
		private int tollVersion;
		private int cpcElectrica;
		private String picSerialNo;
		private int verifyCode;
		private int spare1;
		private int spare2;
		private String spare3;
		private String spare4;

		/**
		 * @param recordNo
		 *            the recordNo to set
		 */
		public Builder setRecordNo(final int recordNo) {
			this.recordNo = recordNo;
			return this;
		}

		/**
		 * @param serialNo
		 *            the serialNo to set
		 */
		public Builder setSerialNo(final String serialNo) {
			this.serialNo = serialNo;
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
		 * @param enRoadNo
		 *            the enRoadNo to set
		 */
		public Builder setEnRoadNo(final int enRoadNo) {
			this.enRoadNo = enRoadNo;
			return this;
		}

		/**
		 * @param enStationNo
		 *            the enStationNo to set
		 */
		public Builder setEnStationNo(final int enStationNo) {
			this.enStationNo = enStationNo;
			return this;
		}

		/**
		 * @param enLaneNo
		 *            the enLaneNo to set
		 */
		public Builder setEnLaneNo(final int enLaneNo) {
			this.enLaneNo = enLaneNo;
			return this;
		}

		/**
		 * @param enTime
		 *            the enTime to set
		 */
		public Builder setEnTime(final Date enTime) {
			this.enTime = enTime;
			return this;
		}

		/**
		 * @param etcTermCode
		 *            the etcTermCode to set
		 */
		public Builder setEtcTermCode(final String etcTermCode) {
			this.etcTermCode = etcTermCode;
			return this;
		}

		/**
		 * @param iccIssuer
		 *            the iccIssuer to set
		 */
		public Builder setIccIssuer(final String iccIssuer) {
			this.iccIssuer = iccIssuer;
			return this;
		}

		/**
		 * @param cpcCardID
		 *            the cpcCardID to set
		 */
		public Builder setCpcCardID(final String cpcCardID) {
			this.cpcCardID = cpcCardID;
			return this;
		}

		/**
		 * @param cpuCardID
		 *            the cpuCardID to set
		 */
		public Builder setCpuCardID(final String cpuCardID) {
			this.cpuCardID = cpuCardID;
			return this;
		}

		/**
		 * @param vehicleType
		 *            the vehicleType to set
		 */
		public Builder setVehicleType(final int vehicleType) {
			this.vehicleType = vehicleType;
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
		 * @param vehStatus
		 *            the vehStatus to set
		 */
		public Builder setVehStatus(final int vehStatus) {
			this.vehStatus = vehStatus;
			return this;
		}

		/**
		 * @param tollAmount
		 *            the tollAmount to set
		 */
		public Builder setTollAmount(final int tollAmount) {
			this.tollAmount = tollAmount;
			return this;
		}

		/**
		 * @param discountFee
		 *            the discountFee to set
		 */
		public Builder setDiscountFee(final int discountFee) {
			this.discountFee = discountFee;
			return this;
		}

		/**
		 * @param tollVersion
		 *            the tollVersion to set
		 */
		public Builder setTollVersion(final int tollVersion) {
			this.tollVersion = tollVersion;
			return this;
		}

		/**
		 * @param cpcElectrica
		 *            the cpcElectrical to set
		 */
		public Builder setCpcElectrica(final int cpcElectrica) {
			this.cpcElectrica = cpcElectrica;
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
		 * @param verifyCode
		 *            the verifyCode to set
		 */
		public Builder setVerifyCode(final int verifyCode) {
			this.verifyCode = verifyCode;
			return this;
		}

		/**
		 * @param spare1
		 *            the spare1 to set
		 */
		public Builder setSpare1(final int spare1) {
			this.spare1 = spare1;
			return this;
		}

		/**
		 * @param spare2
		 *            the spare2 to set
		 */
		public Builder setSpare2(final int spare2) {
			this.spare2 = spare2;
			return this;
		}

		/**
		 * @param spare3
		 *            the spare3 to set
		 */
		public Builder setSpare3(final String spare3) {
			this.spare3 = spare3;
			return this;
		}

		/**
		 * @param spare4
		 *            the spare4 to set
		 */
		public Builder setSpare4(final String spare4) {
			this.spare4 = spare4;
			return this;
		}

		public FlagCpcPassList build() {
			return new FlagCpcPassList(this);
		}
	}
}
