/**
 * File: ETCPassList.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.domain;

import java.util.Date;

/**
 * 
 * @author Dorsey
 * @since 0.0.1-SNAPSHOT
 */
@Deprecated
public class ETCPassList implements TrafficData<String> {

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
	private String iccIssuer;
	private String cpuCardID;
	private int vehicleType;
	private String vehPlate;
	private String vehColor;
	private Date opTime;
	private int direction;
	private int vehStatus;
	private int spare1;
	private int spare2;
	private String spare3;
	private String spare4;

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
		return "ETCPassList [recordNo=" + recordNo + ", serialNo=" + serialNo + ", flagNetRoadID="
				+ flagNetRoadID + ", flagRoadID=" + flagRoadID + ", flagID=" + flagID + ", obuType="
				+ obuType + ", obuMacID=" + obuMacID + ", obuNum=" + obuNum + ", payCardID="
				+ payCardID + ", payCardType=" + payCardType + ", iccIssuer=" + iccIssuer
				+ ", cpuCardID=" + cpuCardID + ", vehicleType=" + vehicleType + ", vehPlate="
				+ vehPlate + ", vehColor=" + vehColor + ", opTime=" + opTime + ", direction="
				+ direction + ", vehStatus=" + vehStatus + ", spare1=" + spare1 + ", spare2="
				+ spare2 + ", spare3=" + spare3 + ", spare4=" + spare4 + "]";
	}

	/**
	 * Builder of {@link ETCPassList}.
	 * 
	 * @author Dorsey
	 *
	 */
	public static class ETCPassListBuilder {

		private final ETCPassList etcPassList;

		/**
		 * Empty constructor of {@link ETCPassListBuilder}.
		 */
		public ETCPassListBuilder() {
			this.etcPassList = new ETCPassList();
		}

		/**
		 * @param recordNo
		 *            the recordNo to set
		 */
		public ETCPassListBuilder withRecordNo(final int recordNo) {
			this.etcPassList.recordNo = recordNo;
			return this;
		}

		/**
		 * @param serialNo
		 *            the serialNo to set
		 */
		public ETCPassListBuilder withSerialNo(final String serialNo) {
			this.etcPassList.serialNo = serialNo;
			return this;
		}

		/**
		 * @param flagNetRoadID
		 *            the flagNetRoadID to set
		 */
		public ETCPassListBuilder withFlagNetRoadID(final int flagNetRoadID) {
			this.etcPassList.flagNetRoadID = flagNetRoadID;
			return this;
		}

		/**
		 * @param flagRoadID
		 *            the flagRoadID to set
		 */
		public ETCPassListBuilder withFlagRoadID(final int flagRoadID) {
			this.etcPassList.flagRoadID = flagRoadID;
			return this;
		}

		/**
		 * @param flagID
		 *            the flagID to set
		 */
		public ETCPassListBuilder withFlagID(final int flagID) {
			this.etcPassList.flagID = flagID;
			return this;
		}

		/**
		 * @param obuType
		 *            the obuType to set
		 */
		public ETCPassListBuilder withObuType(final int obuType) {
			this.etcPassList.obuType = obuType;
			return this;
		}

		/**
		 * @param obuMacID
		 *            the obuMacID to set
		 */
		public ETCPassListBuilder withObuMacID(final int obuMacID) {
			this.etcPassList.obuMacID = obuMacID;
			return this;
		}

		/**
		 * @param obuNum
		 *            the obuNum to set
		 */
		public ETCPassListBuilder withObuNum(final String obuNum) {
			this.etcPassList.obuNum = obuNum;
			return this;
		}

		/**
		 * @param payCardID
		 *            the payCardID to set
		 */
		public ETCPassListBuilder withPayCardID(final String payCardID) {
			this.etcPassList.payCardID = payCardID;
			return this;
		}

		/**
		 * @param payCardType
		 *            the payCardType to set
		 */
		public ETCPassListBuilder withPayCardType(final int payCardType) {
			this.etcPassList.payCardType = payCardType;
			return this;
		}

		/**
		 * @param iccIssuer
		 *            the iccIssuer to set
		 */
		public ETCPassListBuilder withIccIssuer(final String iccIssuer) {
			this.etcPassList.iccIssuer = iccIssuer;
			return this;
		}

		/**
		 * @param cpuCardID
		 *            the cpuCardID to set
		 */
		public ETCPassListBuilder withCpuCardID(final String cpuCardID) {
			this.etcPassList.cpuCardID = cpuCardID;
			return this;
		}

		/**
		 * @param vehicleType
		 *            the vehicleType to set
		 */
		public ETCPassListBuilder withVehicleType(final int vehicleType) {
			this.etcPassList.vehicleType = vehicleType;
			return this;
		}

		/**
		 * @param vehPlate
		 *            the vehPlate to set
		 */
		public ETCPassListBuilder withVehPlate(final String vehPlate) {
			this.etcPassList.vehPlate = vehPlate;
			return this;
		}

		/**
		 * @param vehColor
		 *            the vehColor to set
		 */
		public ETCPassListBuilder withVehColor(final String vehColor) {
			this.etcPassList.vehColor = vehColor;
			return this;
		}

		/**
		 * @param opTime
		 *            the opTime to set
		 */
		public ETCPassListBuilder withOpTime(final Date opTime) {
			this.etcPassList.opTime = opTime;
			return this;
		}

		/**
		 * @param direction
		 *            the direction to set
		 */
		public ETCPassListBuilder withDirection(final int direction) {
			this.etcPassList.direction = direction;
			return this;
		}

		/**
		 * @param vehStatus
		 *            the vehStatus to set
		 */
		public ETCPassListBuilder withVehStatus(final int vehStatus) {
			this.etcPassList.vehStatus = vehStatus;
			return this;
		}

		/**
		 * @param spare1
		 *            the spare1 to set
		 */
		public ETCPassListBuilder withSpare1(final int spare1) {
			this.etcPassList.spare1 = spare1;
			return this;
		}

		/**
		 * @param spare2
		 *            the spare2 to set
		 */
		public ETCPassListBuilder withSpare2(final int spare2) {
			this.etcPassList.spare2 = spare2;
			return this;
		}

		/**
		 * @param spare3
		 *            the spare3 to set
		 */
		public ETCPassListBuilder withSpare3(final String spare3) {
			this.etcPassList.spare3 = spare3;
			return this;
		}

		/**
		 * @param spare4
		 *            the spare4 to set
		 */
		public ETCPassListBuilder withSpare4(final String spare4) {
			this.etcPassList.spare4 = spare4;
			return this;
		}

		public ETCPassList build() {
			return this.etcPassList;
		}
	}
}
