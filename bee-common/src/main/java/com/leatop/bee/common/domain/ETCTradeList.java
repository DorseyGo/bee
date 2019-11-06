/**
 * File: ETCTradeList.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.domain;

import java.util.Date;

import com.leatop.bee.data.supervisor.introspector.annotation.Duplicational;

/**
 * @author Dorsey
 * @since 0.0.1-SNAPSHOT
 * @see FlagTollList
 */
@Deprecated
@Duplicational
public class ETCTradeList implements TrafficData<String> {

	private int recordNo;
	private String serialNo;
	private int flagNetRoadID;
	private int flagRoadID;
	private int flagID;
	private int obuType;
	private int obuMacID;
	private String obuNum;
	private String payCardID;
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
	private int payCardBabkabceBefore;
	private int payCardBabkabceAfter;
	private int tollAmount;
	private int tollVersion;
	private int dealStatu;
	private int verifyCode;
	private int spare1;
	private int spare2;
	private String spare3;
	private String spare4;

	/**
	 * Empty constructor of {@link ETCTradeList}.
	 */
	public ETCTradeList() {
		// empty
	}

	public String getId() {
		return this.serialNo;
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
	 * @return the payCardID
	 */
	public String getPayCardID() {
		return payCardID;
	}

	/**
	 * @param payCardID
	 *            the payCardID to set
	 */
	public void setPayCardID(final String payCardID) {
		this.payCardID = payCardID;
	}

	/**
	 * @return the payCardType
	 */
	public int getPayCardType() {
		return payCardType;
	}

	/**
	 * @param payCardType
	 *            the payCardType to set
	 */
	public void setPayCardType(final int payCardType) {
		this.payCardType = payCardType;
	}

	/**
	 * @return the payCardIssuer
	 */
	public String getPayCardIssuer() {
		return payCardIssuer;
	}

	/**
	 * @param payCardIssuer
	 *            the payCardIssuer to set
	 */
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
	 * @return the payCardBabkabceBefore
	 */
	public int getPayCardBabkabceBefore() {
		return payCardBabkabceBefore;
	}

	/**
	 * @param payCardBabkabceBefore
	 *            the payCardBabkabceBefore to set
	 */
	public void setPayCardBabkabceBefore(final int payCardBabkabceBefore) {
		this.payCardBabkabceBefore = payCardBabkabceBefore;
	}

	/**
	 * @return the payCardBabkabceAfter
	 */
	public int getPayCardBabkabceAfter() {
		return payCardBabkabceAfter;
	}

	/**
	 * @param payCardBabkabceAfter
	 *            the payCardBabkabceAfter to set
	 */
	public void setPayCardBabkabceAfter(final int payCardBabkabceAfter) {
		this.payCardBabkabceAfter = payCardBabkabceAfter;
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
	 * @return the dealStatu
	 */
	public int getDealStatu() {
		return dealStatu;
	}

	/**
	 * @param dealStatu
	 *            the dealStatu to set
	 */
	public void setDealStatu(final int dealStatu) {
		this.dealStatu = dealStatu;
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

	@Override
	public String toString() {
		return "ETCTradeList [recordNo=" + recordNo + ", serialNo=" + serialNo + ", flagNetRoadID="
				+ flagNetRoadID + ", flagRoadID=" + flagRoadID + ", flagID=" + flagID + ", obuType="
				+ obuType + ", obuMacID=" + obuMacID + ", obuNum=" + obuNum + ", payCardID="
				+ payCardID + ", payCardType=" + payCardType + ", payCardIssuer=" + payCardIssuer
				+ ", cpuCardID=" + cpuCardID + ", etcTermCode=" + etcTermCode + ", etcTermTradeNo="
				+ etcTermTradeNo + ", transacType=" + transacType + ", keyVersion=" + keyVersion
				+ ", algFlag=" + algFlag + ", tacCode=" + tacCode + ", vehicleType=" + vehicleType
				+ ", vehPlate=" + vehPlate + ", vehColor=" + vehColor + ", opTime=" + opTime
				+ ", direction=" + direction + ", payCardBabkabceBefore=" + payCardBabkabceBefore
				+ ", payCardBabkabceAfter=" + payCardBabkabceAfter + ", tollAmount=" + tollAmount
				+ ", tollVersion=" + tollVersion + ", dealStatu=" + dealStatu + ", verifyCode="
				+ verifyCode + ", spare1=" + spare1 + ", spare2=" + spare2 + ", spare3=" + spare3
				+ ", spare4=" + spare4 + "]";
	}

	/**
	 * Builder of {@link ETCTradeList}.
	 * 
	 * @author Dorsey
	 *
	 */
	public static class ETCTradeListBuilder {

		private final ETCTradeList etcTradeList;

		public ETCTradeListBuilder() {
			this.etcTradeList = new ETCTradeList();
		}

		public ETCTradeListBuilder withRecordNo(final int recordNo) {
			this.etcTradeList.recordNo = recordNo;
			return this;
		}

		public ETCTradeListBuilder withSerialNo(final String serialNo) {
			this.etcTradeList.serialNo = serialNo;
			return this;
		}

		/**
		 * @param flagNetRoadID
		 *            the flagNetRoadID to set
		 */
		public ETCTradeListBuilder withFlagNetRoadID(final int flagNetRoadID) {
			this.etcTradeList.flagNetRoadID = flagNetRoadID;
			return this;
		}

		/**
		 * @param flagRoadID
		 *            the flagRoadID to set
		 */
		public ETCTradeListBuilder withFlagRoadID(final int flagRoadID) {
			this.etcTradeList.flagRoadID = flagRoadID;
			return this;
		}

		/**
		 * @param flagID
		 *            the flagID to set
		 */
		public ETCTradeListBuilder withFlagID(final int flagID) {
			this.etcTradeList.flagID = flagID;
			return this;
		}

		/**
		 * @param obuType
		 *            the obuType to set
		 */
		public ETCTradeListBuilder withObuType(final int obuType) {
			this.etcTradeList.obuType = obuType;
			return this;
		}

		/**
		 * @param obuMacID
		 *            the obuMacID to set
		 */
		public ETCTradeListBuilder withObuMacID(final int obuMacID) {
			this.etcTradeList.obuMacID = obuMacID;
			return this;
		}

		/**
		 * @param obuNum
		 *            the obuNum to set
		 */
		public ETCTradeListBuilder withObuNum(final String obuNum) {
			this.etcTradeList.obuNum = obuNum;
			return this;
		}

		/**
		 * @param payCardID
		 *            the payCardID to set
		 */
		public ETCTradeListBuilder withPayCardID(final String payCardID) {
			this.etcTradeList.payCardID = payCardID;
			return this;
		}

		/**
		 * @param payCardType
		 *            the payCardType to set
		 */
		public ETCTradeListBuilder withPayCardType(final int payCardType) {
			this.etcTradeList.payCardType = payCardType;
			return this;
		}

		/**
		 * @param payCardIssuer
		 *            the payCardIssuer to set
		 */
		public ETCTradeListBuilder withPayCardIssuer(final String payCardIssuer) {
			this.etcTradeList.payCardIssuer = payCardIssuer;
			return this;
		}

		/**
		 * @param cpuCardID
		 *            the cpuCardID to set
		 */
		public ETCTradeListBuilder withCpuCardID(final String cpuCardID) {
			this.etcTradeList.cpuCardID = cpuCardID;
			return this;
		}

		/**
		 * @param etcTermCode
		 *            the etcTermCode to set
		 */
		public void withEtcTermCode(final String etcTermCode) {
			this.etcTradeList.etcTermCode = etcTermCode;
		}

		/**
		 * @param etcTermTradeNo
		 *            the etcTermTradeNo to set
		 */
		public ETCTradeListBuilder withEtcTermTradeNo(final int etcTermTradeNo) {
			this.etcTradeList.etcTermTradeNo = etcTermTradeNo;
			return this;
		}

		/**
		 * @param transacType
		 *            the transacType to set
		 */
		public ETCTradeListBuilder withTransacType(final int transacType) {
			this.etcTradeList.transacType = transacType;
			return this;
		}

		/**
		 * @param keyVersion
		 *            the keyVersion to set
		 */
		public ETCTradeListBuilder withKeyVersion(final int keyVersion) {
			this.etcTradeList.keyVersion = keyVersion;
			return this;
		}

		/**
		 * @param algFlag
		 *            the algFlag to set
		 */
		public ETCTradeListBuilder withAlgFlag(final int algFlag) {
			this.etcTradeList.algFlag = algFlag;
			return this;
		}

		/**
		 * @param tacCode
		 *            the tacCode to set
		 */
		public ETCTradeListBuilder withTacCode(final String tacCode) {
			this.etcTradeList.tacCode = tacCode;
			return this;
		}

		/**
		 * @param vehicleType
		 *            the vehicleType to set
		 */
		public ETCTradeListBuilder withVehicleType(final int vehicleType) {
			this.etcTradeList.vehicleType = vehicleType;
			return this;
		}

		/**
		 * @param vehPlate
		 *            the vehPlate to set
		 */
		public ETCTradeListBuilder withVehPlate(final String vehPlate) {
			this.etcTradeList.vehPlate = vehPlate;
			return this;
		}

		/**
		 * @param vehColor
		 *            the vehColor to set
		 */
		public ETCTradeListBuilder withVehColor(final String vehColor) {
			this.etcTradeList.vehColor = vehColor;
			return this;
		}

		/**
		 * @param opTime
		 *            the opTime to set
		 */
		public ETCTradeListBuilder withOpTime(final Date opTime) {
			this.etcTradeList.opTime = opTime;
			return this;
		}

		/**
		 * @param direction
		 *            the direction to set
		 */
		public ETCTradeListBuilder withDirection(final int direction) {
			this.etcTradeList.direction = direction;
			return this;
		}

		/**
		 * @param payCardBabkabceBefore
		 *            the payCardBabkabceBefore to set
		 */
		public ETCTradeListBuilder withPayCardBabkabceBefore(final int payCardBabkabceBefore) {
			this.etcTradeList.payCardBabkabceBefore = payCardBabkabceBefore;
			return this;
		}

		/**
		 * @param payCardBabkabceAfter
		 *            the payCardBabkabceAfter to set
		 */
		public ETCTradeListBuilder withPayCardBabkabceAfter(final int payCardBabkabceAfter) {
			this.etcTradeList.payCardBabkabceAfter = payCardBabkabceAfter;
			return this;
		}

		/**
		 * @param tollAmount
		 *            the tollAmount to set
		 */
		public ETCTradeListBuilder withTollAmount(final int tollAmount) {
			this.etcTradeList.tollAmount = tollAmount;
			return this;
		}

		/**
		 * @param tollVersion
		 *            the tollVersion to set
		 */
		public ETCTradeListBuilder withTollVersion(final int tollVersion) {
			this.etcTradeList.tollVersion = tollVersion;
			return this;
		}

		/**
		 * @param dealStatu
		 *            the dealStatu to set
		 */
		public ETCTradeListBuilder withDealStatu(final int dealStatu) {
			this.etcTradeList.dealStatu = dealStatu;
			return this;
		}

		/**
		 * @param verifyCode
		 *            the verifyCode to set
		 */
		public ETCTradeListBuilder withVerifyCode(final int verifyCode) {
			this.etcTradeList.verifyCode = verifyCode;
			return this;
		}

		/**
		 * @param spare1
		 *            the spare1 to set
		 */
		public ETCTradeListBuilder withSpare1(final int spare1) {
			this.etcTradeList.spare1 = spare1;
			return this;
		}

		/**
		 * @param spare2
		 *            the spare2 to set
		 */
		public ETCTradeListBuilder withSpare2(final int spare2) {
			this.etcTradeList.spare2 = spare2;
			return this;
		}

		/**
		 * @param spare3
		 *            the spare3 to set
		 */
		public ETCTradeListBuilder withSpare3(final String spare3) {
			this.etcTradeList.spare3 = spare3;
			return this;
		}

		/**
		 * @param spare4
		 *            the spare4 to set
		 */
		public ETCTradeListBuilder withSpare4(final String spare4) {
			this.etcTradeList.spare4 = spare4;
			return this;
		}

		public ETCTradeList build() {
			return this.etcTradeList;
		}
	}
}
