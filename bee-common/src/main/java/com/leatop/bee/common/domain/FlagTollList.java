/**
 * File: FlagTollList.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.domain;

import java.util.Date;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Container which contains desired fields for <code>FlagTollList</code>.
 * 
 * @author Dorsey
 *
 */
public class FlagTollList extends ETCTrafficData<String> {

	@JsonProperty("RecordNo")
	private int recordNo;
	
	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("PosId")
	private int posId;
	
	@JsonProperty("LaneNo")
	private int laneNo;
	
	@JsonProperty("EnRoadNo")
	private int enRoadNo;
	
	@JsonProperty("EnStationNo")
	private int enStationNo;
	
	@JsonProperty("EnLaneNo")
	private int enLaneNo;
	
	@JsonProperty("enTime")
	private Date enTime;
	
	@JsonProperty("OBUType")
	private int obuType;
	
	@JsonProperty("OBUMacID")
	private int obuMacID;
	
	@JsonProperty("OBUNum")
	private String obuNum;
	
	@JsonProperty("PayCardId")
	private String payCardId;
	
	@JsonProperty("PayCardType")
	private int payCardType;
	
	@JsonProperty("PayCardIssuer")
	private String payCardIssuer;
	
	@JsonProperty("CPUCardID")
	private String cpuCardID;
	
	@JsonProperty("ETCTermCode")
	private String etcTermCode;
	
	@JsonProperty("ETCTermTradeNo")
	private int etcTermTradeNo;
	
	@JsonProperty("TransacType")
	private int transacType;
	
	@JsonProperty("KeyVersion")
	private int keyVersion;
	
	@JsonProperty("AlgFlag")
	private int algFlag;
	
	@JsonProperty("TacCode")
	private String tacCode;
	
	@JsonProperty("VehicleType")
	private int vehicleType;
	
	@JsonProperty("VehPlate")
	private String vehPlate;
	
	@JsonProperty("VehColor")
	private String vehColor;
	
	@JsonProperty("OpTime")
	private Date opTime;
	
	@JsonProperty("Direction")
	private int direction;
	
	@JsonProperty("PayCardBalanceBefore")
	private int payCardBalanceBefore;
	
	@JsonProperty("PayCardBalanceAfter")
	private int payCardBalanceAfter;
	
	@JsonProperty("TollAmount")
	private int tollAmount;
	
	@JsonProperty("discountFee")
	private int discountFee;
	
	@JsonProperty("TollVersion")
	private int tollVersion;
	
	@JsonProperty("ObuElectrical")
	private int obuElectrical;
	
	@JsonProperty("DealStatus")
	private int dealStatus;
	
	@JsonProperty("PicSerialNo")
	private String picSerialNo;
	
	@JsonProperty("VerifyCode")
	private int verifyCode;
	
	@JsonProperty("Spare1")
	private int spare1;
	
	@JsonProperty("Spare2")
	private int spare2;
	
	@JsonProperty("Spare3")
	private String spare3;
	
	@JsonProperty("Spare4")
	private String spare4;

	/**
	 * Empty constructor.
	 */
	public FlagTollList() {
		super();
	}

	/**
	 * Constructor of {@link FlagTollList}.
	 * 
	 * @param builder
	 */
	public FlagTollList(final Builder builder) {
		if (builder != null) {
			this.recordNo = builder.recordNo;
			this.serialNo = builder.serialNo;
			this.flagID = builder.flagID;
			this.flagNetRoadID = builder.flagNetRoadID;
			this.flagRoadID = builder.flagRoadID;
			this.posId = builder.posId;
			this.laneNo = builder.laneNo;
			this.enRoadNo = builder.enRoadNo;
			this.enStationNo = builder.enStationNo;
			this.enLaneNo = builder.enLaneNo;
			this.enTime = builder.enTime;
			this.obuType = builder.obuType;
			this.obuMacID = builder.obuMacID;
			this.obuNum = builder.obuNum;
			this.payCardId = builder.payCardId;
			this.payCardType = builder.payCardType;
			this.payCardIssuer = builder.payCardIssuer;
			this.cpuCardID = builder.cpuCardID;
			this.etcTermCode = builder.etcTermCode;
			this.etcTermTradeNo = builder.etcTermTradeNo;
			this.transacType = builder.transacType;
			this.keyVersion = builder.keyVersion;
			this.algFlag = builder.algFlag;
			this.tacCode = builder.tacCode;
			this.vehicleType = builder.vehicleType;
			this.vehPlate = builder.vehPlate;
			this.vehColor = builder.vehColor;
			this.opTime = builder.opTime;
			this.direction = builder.direction;
			this.payCardBalanceBefore = builder.payCardBalanceBefore;
			this.payCardBalanceAfter = builder.payCardBalanceAfter;
			this.tollAmount = builder.tollAmount;
			this.discountFee = builder.discountFee;
			this.tollVersion = builder.tollVersion;
			this.obuElectrical = builder.obuElectrical;
			this.dealStatus = builder.dealStatus;
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
		return super.hashCode() + Objects.hash(this.serialNo);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof FlagTollList))
			return false;

		FlagTollList other = (FlagTollList) obj;
		return super.equals(obj) && Objects.equals(serialNo, other.serialNo);
	}

	@Override
	public String toString() {
		return "FlagTollList:{serialNo=" + serialNo + ", " + super.toString() + "}";
	}

	/**
	 * @return the recordNo
	 */
	@JsonProperty("RecordNo")
	public int getRecordNo() {
		return recordNo;
	}

	/**
	 * @param recordNo
	 *            the recordNo to set
	 */
	@JsonProperty("RecordNo")
	public void setRecordNo(final int recordNo) {
		this.recordNo = recordNo;
	}

	/**
	 * @return the serialNo
	 */
	@JsonProperty("SerialNo")
	public String getSerialNo() {
		return serialNo;
	}

	/**
	 * @param serialNo
	 *            the serialNo to set
	 */
	@JsonProperty("SerialNo")
	public void setSerialNo(final String serialNo) {
		this.serialNo = serialNo;
	}

	/**
	 * @return the posId
	 */
	@JsonProperty("PosId")
	public int getPosId() {
		return posId;
	}

	/**
	 * @param posId
	 *            the posId to set
	 */
	@JsonProperty("PosId")
	public void setPosId(final int posId) {
		this.posId = posId;
	}

	/**
	 * @return the laneNo
	 */
	@JsonProperty("LaneNo")
	public int getLaneNo() {
		return laneNo;
	}

	/**
	 * @param laneNo
	 *            the laneNo to set
	 */
	@JsonProperty("LaneNo")
	public void setLaneNo(final int laneNo) {
		this.laneNo = laneNo;
	}

	/**
	 * @return the enRoadNo
	 */
	@JsonProperty("EnRoadNo")
	public int getEnRoadNo() {
		return enRoadNo;
	}

	/**
	 * @param enRoadNo
	 *            the enRoadNo to set
	 */
	@JsonProperty("EnRoadNo")
	public void setEnRoadNo(final int enRoadNo) {
		this.enRoadNo = enRoadNo;
	}

	/**
	 * @return the enStationNo
	 */
	@JsonProperty("EnStationNo")
	public int getEnStationNo() {
		return enStationNo;
	}

	/**
	 * @param enStationNo
	 *            the enStationNo to set
	 */
	@JsonProperty("EnStationNo")
	public void setEnStationNo(final int enStationNo) {
		this.enStationNo = enStationNo;
	}

	/**
	 * @return the enLaneNo
	 */
	@JsonProperty("EnLaneNo")
	public int getEnLaneNo() {
		return enLaneNo;
	}

	/**
	 * @param enLaneNo
	 *            the enLaneNo to set
	 */
	@JsonProperty("EnLaneNo")
	public void setEnLaneNo(final int enLaneNo) {
		this.enLaneNo = enLaneNo;
	}

	/**
	 * @return the enTime
	 */
	@JsonProperty("enTime")
	public Date getEnTime() {
		return enTime;
	}

	/**
	 * @param enTime
	 *            the enTime to set
	 */
	@JsonProperty("enTime")
	public void setEnTime(final Date enTime) {
		this.enTime = enTime;
	}

	/**
	 * @return the obuType
	 */
	@JsonProperty("OBUType")
	public int getObuType() {
		return obuType;
	}

	/**
	 * @param obuType
	 *            the obuType to set
	 */
	@JsonProperty("OBUType")
	public void setObuType(final int obuType) {
		this.obuType = obuType;
	}

	/**
	 * @return the obuMacID
	 */
	@JsonProperty("OBUMacID")
	public int getObuMacID() {
		return obuMacID;
	}

	/**
	 * @param obuMacID
	 *            the obuMacID to set
	 */
	@JsonProperty("OBUMacID")
	public void setObuMacID(final int obuMacID) {
		this.obuMacID = obuMacID;
	}

	/**
	 * @return the obuNum
	 */
	@JsonProperty("OBUNum")
	public String getObuNum() {
		return obuNum;
	}

	/**
	 * @param obuNum
	 *            the obuNum to set
	 */
	@JsonProperty("OBUNum")
	public void setObuNum(final String obuNum) {
		this.obuNum = obuNum;
	}

	/**
	 * @return the payCardId
	 */
	@JsonProperty("PayCardId")
	public String getPayCardId() {
		return payCardId;
	}

	/**
	 * @param payCardId
	 *            the payCardId to set
	 */
	@JsonProperty("PayCardId")
	public void setPayCardId(final String payCardId) {
		this.payCardId = payCardId;
	}

	/**
	 * @return the payCardType
	 */
	@JsonProperty("PayCardType")
	public int getPayCardType() {
		return payCardType;
	}

	/**
	 * @param payCardType
	 *            the payCardType to set
	 */
	@JsonProperty("PayCardType")
	public void setPayCardType(final int payCardType) {
		this.payCardType = payCardType;
	}

	/**
	 * @return the payCardIssuer
	 */
	@JsonProperty("PayCardIssuer")
	public String getPayCardIssuer() {
		return payCardIssuer;
	}

	/**
	 * @param payCardIssuer
	 *            the payCardIssuer to set
	 */
	@JsonProperty("PayCardIssuer")
	public void setPayCardIssuer(final String payCardIssuer) {
		this.payCardIssuer = payCardIssuer;
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
	 * @return the etcTermTradeNo
	 */
	public int getEtcTermTradeNo() {
		return etcTermTradeNo;
	}

	/**
	 * @param etcTermTradeNo
	 *            the etcTermTradeNo to set
	 */
	public void setEtcTermTradeNo(final int etcTermTradeNo) {
		this.etcTermTradeNo = etcTermTradeNo;
	}

	/**
	 * @return the transacType
	 */
	public int getTransacType() {
		return transacType;
	}

	/**
	 * @param transacType
	 *            the transacType to set
	 */
	public void setTransacType(final int transacType) {
		this.transacType = transacType;
	}

	/**
	 * @return the keyVersion
	 */
	public int getKeyVersion() {
		return keyVersion;
	}

	/**
	 * @param keyVersion
	 *            the keyVersion to set
	 */
	public void setKeyVersion(final int keyVersion) {
		this.keyVersion = keyVersion;
	}

	/**
	 * @return the algFlag
	 */
	public int getAlgFlag() {
		return algFlag;
	}

	/**
	 * @param algFlag
	 *            the algFlag to set
	 */
	public void setAlgFlag(final int algFlag) {
		this.algFlag = algFlag;
	}

	/**
	 * @return the tacCode
	 */
	public String getTacCode() {
		return tacCode;
	}

	/**
	 * @param tacCode
	 *            the tacCode to set
	 */
	public void setTacCode(final String tacCode) {
		this.tacCode = tacCode;
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
	 * @return the payCardBalanceBefore
	 */
	public int getPayCardBalanceBefore() {
		return payCardBalanceBefore;
	}

	/**
	 * @param payCardBalanceBefore
	 *            the payCardBalanceBefore to set
	 */
	public void setPayCardBalanceBefore(final int payCardBalanceBefore) {
		this.payCardBalanceBefore = payCardBalanceBefore;
	}

	/**
	 * @return the payCardBalanceAfter
	 */
	public int getPayCardBalanceAfter() {
		return payCardBalanceAfter;
	}

	/**
	 * @param payCardBalanceAfter
	 *            the payCardBalanceAfter to set
	 */
	public void setPayCardBalanceAfter(final int payCardBalanceAfter) {
		this.payCardBalanceAfter = payCardBalanceAfter;
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
	 * @return the dealStatus
	 */
	public int getDealStatus() {
		return dealStatus;
	}

	/**
	 * @param dealStatus
	 *            the dealStatus to set
	 */
	public void setDealStatus(final int dealStatus) {
		this.dealStatus = dealStatus;
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
	 * The builder for {@link FlagTollList}.
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
		private int obuType;
		private int obuMacID;
		private String obuNum;
		private String payCardId;
		private int payCardType;
		private String payCardIssuer;
		private String cpuCardID;
		private String etcTermCode;
		private int etcTermTradeNo;
		private int transacType;
		private int keyVersion;
		private int algFlag;
		private String tacCode;
		private int vehicleType;
		private String vehPlate;
		private String vehColor;
		private Date opTime;
		private int direction;
		private int payCardBalanceBefore;
		private int payCardBalanceAfter;
		private int tollAmount;
		private int discountFee;
		private int tollVersion;
		private int obuElectrical;
		private int dealStatus;
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
		 * @param payCardId
		 *            the payCardId to set
		 */
		public Builder setPayCardId(final String payCardId) {
			this.payCardId = payCardId;
			return this;
		}

		/**
		 * @param payCardType
		 *            the payCardType to set
		 */
		public Builder setPayCardType(final int payCardType) {
			this.payCardType = payCardType;
			return this;
		}

		/**
		 * @param payCardIssuer
		 *            the payCardIssuer to set
		 */
		public Builder setPayCardIssuer(final String payCardIssuer) {
			this.payCardIssuer = payCardIssuer;
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
		 * @param etcTermCode
		 *            the etcTermCode to set
		 */
		public Builder setEtcTermCode(final String etcTermCode) {
			this.etcTermCode = etcTermCode;
			return this;
		}

		/**
		 * @param etcTermTradeNo
		 *            the etcTermTradeNo to set
		 */
		public Builder setEtcTermTradeNo(final int etcTermTradeNo) {
			this.etcTermTradeNo = etcTermTradeNo;
			return this;
		}

		/**
		 * @param transacType
		 *            the transacType to set
		 */
		public Builder setTransacType(final int transacType) {
			this.transacType = transacType;
			return this;
		}

		/**
		 * @param keyVersion
		 *            the keyVersion to set
		 */
		public Builder setKeyVersion(final int keyVersion) {
			this.keyVersion = keyVersion;
			return this;
		}

		/**
		 * @param algFlag
		 *            the algFlag to set
		 */
		public Builder setAlgFlag(final int algFlag) {
			this.algFlag = algFlag;
			return this;
		}

		/**
		 * @param tacCode
		 *            the tacCode to set
		 */
		public Builder setTacCode(final String tacCode) {
			this.tacCode = tacCode;
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
		 * @param payCardBalanceBefore
		 *            the payCardBalanceBefore to set
		 */
		public Builder setPayCardBalanceBefore(final int payCardBalanceBefore) {
			this.payCardBalanceBefore = payCardBalanceBefore;
			return this;
		}

		/**
		 * @param payCardBalanceAfter
		 *            the payCardBalanceAfter to set
		 */
		public Builder setPayCardBalanceAfter(final int payCardBalanceAfter) {
			this.payCardBalanceAfter = payCardBalanceAfter;
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
		 * @param obuElectrical
		 *            the obuElectrical to set
		 */
		public Builder setObuElectrical(final int obuElectrical) {
			this.obuElectrical = obuElectrical;
			return this;
		}

		/**
		 * @param dealStatus
		 *            the dealStatus to set
		 */
		public Builder setDealStatus(final int dealStatus) {
			this.dealStatus = dealStatus;
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

		public FlagTollList build() {
			return new FlagTollList(this);
		}
	}
}
