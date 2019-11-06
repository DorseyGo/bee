/**
 * File: FlagEtcPassList.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.domain;

import java.util.Date;
import java.util.Objects;

/**
 * @author Dorsey
 *
 */
public class FlagEtcPassList extends ETCTrafficData<String> {

	private int recordNo;
	private String serialNo;
	private int posId;
	private int laneNo;
	private int obuType;
	private int obuMacID;
	private String obuNum;
	private String etcTermCode;
	private int enRoadNo;
	private int enStationNo;
	private int enLaneNo;
	private Date enTime;
	private String cpuCardID;
	private int vehicleType;
	private int tollAmount;
	private int discountFee;
	private String vehPlate;
	private String vehColor;
	private Date opTime;
	private int direction;
	private int vehStatus;
	private int obuElectrical;
	/**
	 * 2 - 写标签非设备失败 3 - 写标签非设备失败 4 - 写ETC 卡非设备失败交易状态 5 - 写ETC 卡非设备失败交易状态 6 - 写
	 * CPC 卡非设写 CPC 卡非设 7 - 写 CPC 卡非设
	 */
	private int eevStatus;
	private String picSerialNo;
	private int spare1;
	private int spare2;
	private String spare3;
	private String spare4;

	/**
	 * Empty constructor.
	 */
	public FlagEtcPassList() {
		super();
	}

	/**
	 * @param builder
	 */
	public FlagEtcPassList(final Builder builder) {
		if (builder != null) {
			this.recordNo = builder.recordNo;
			this.serialNo = builder.serialNo;
			this.flagNetRoadID = builder.flagNetRoadID;
			this.flagRoadID = builder.flagRoadID;
			this.flagID = builder.flagID;
			this.posId = builder.posId;
			this.laneNo = builder.laneNo;
			this.obuType = builder.obuType;
			this.obuMacID = builder.obuMacID;
			this.obuNum = builder.obuNum;
			this.etcTermCode = builder.etcTermCode;
			this.enRoadNo = builder.enRoadNo;
			this.enStationNo = builder.enStationNo;
			this.enLaneNo = builder.enLaneNo;
			this.enTime = builder.enTime;
			this.cpuCardID = builder.cpuCardID;
			this.vehicleType = builder.vehicleType;
			this.tollAmount = builder.tollAmount;
			this.discountFee = builder.discountFee;
			this.vehPlate = builder.vehPlate;
			this.vehColor = builder.vehColor;
			this.opTime = builder.opTime;
			this.direction = builder.direction;
			this.vehStatus = builder.vehStatus;
			this.obuElectrical = builder.obuElectrical;
			this.eevStatus = builder.eevStatus;
			this.picSerialNo = builder.picSerialNo;
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
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof FlagEtcPassList))
			return false;

		FlagEtcPassList other = (FlagEtcPassList) obj;
		return super.equals(obj) && Objects.equals(serialNo, other.serialNo);
	}

	@Override
	public String toString() {
		return "FlagEtcPassList: {serialNo=" + serialNo + ", " + super.toString() + "}";
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
	 * @return the obuType
	 */
	public int getObuType() {
		return obuType;
	}

	/**
	 * @param obuType
	 *            the obuType to set
	 */
	public void setObuType(final int obuType) {
		this.obuType = obuType;
	}

	/**
	 * @return the obuMacID
	 */
	public int getObuMacID() {
		return obuMacID;
	}

	/**
	 * @param obuMacID
	 *            the obuMacID to set
	 */
	public void setObuMacID(final int obuMacID) {
		this.obuMacID = obuMacID;
	}

	/**
	 * @return the obuNum
	 */
	public String getObuNum() {
		return obuNum;
	}

	/**
	 * @param obuNum
	 *            the obuNum to set
	 */
	public void setObuNum(final String obuNum) {
		this.obuNum = obuNum;
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
	 * @return the obuElectrical
	 */
	public int getObuElectrical() {
		return obuElectrical;
	}

	/**
	 * @param obuElectrical
	 *            the obuElectrical to set
	 */
	public void setObuElectrical(final int obuElectrical) {
		this.obuElectrical = obuElectrical;
	}

	/**
	 * @return the eevStatus
	 */
	public int getEevStatus() {
		return eevStatus;
	}

	/**
	 * @param eevStatus
	 *            the eevStatus to set
	 */
	public void setEevStatus(final int eevStatus) {
		this.eevStatus = eevStatus;
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
	 * The builder for <code>FlagEtcPassList</code>.
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
		private int obuType;
		private int obuMacID;
		private String obuNum;
		private String etcTermCode;
		private int enRoadNo;
		private int enStationNo;
		private int enLaneNo;
		private Date enTime;
		private String cpuCardID;
		private int vehicleType;
		private int tollAmount;
		private int discountFee;
		private String vehPlate;
		private String vehColor;
		private Date opTime;
		private int direction;
		private int vehStatus;
		private int obuElectrical;
		private int eevStatus;
		private String picSerialNo;
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
		 * @param obuType
		 *            the obuType to set
		 */
		public Builder setObuType(final int obuType) {
			this.obuType = obuType;
			return this;
		}

		/**
		 * @param obuMacID
		 *            the obuMacID to set
		 */
		public Builder setObuMacID(final int obuMacID) {
			this.obuMacID = obuMacID;
			return this;
		}

		/**
		 * @param obuNum
		 *            the obuNum to set
		 */
		public Builder setObuNum(final String obuNum) {
			this.obuNum = obuNum;
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
		 * @param obuElectrical
		 *            the obuElectrical to set
		 */
		public Builder setObuElectrical(final int obuElectrical) {
			this.obuElectrical = obuElectrical;
			return this;
		}

		/**
		 * @param eevStatus
		 *            the eevStatus to set
		 */
		public Builder setEevStatus(final int eevStatus) {
			this.eevStatus = eevStatus;
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

		public FlagEtcPassList build() {
			return new FlagEtcPassList(this);
		}
	}
}
