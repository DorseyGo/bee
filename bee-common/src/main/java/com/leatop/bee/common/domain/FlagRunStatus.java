/**
 * File: FlagRunStatus.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.domain;

import java.util.Date;
import java.util.Objects;

import com.leatop.bee.common.domain.FlagRunStatus.FlagRunStatusID;

/**
 * Container which contains the <code>FlagRunStatus</code>.
 * 
 * @author Dorsey
 *
 */
public class FlagRunStatus extends ETCTrafficData<FlagRunStatusID> {

	private int recordNo;
	private int posId;
	private int direction;
	private String dirDescription;
	private Date catchTime;
	private int computer;
	private int diskCapacity;
	private int diskUsage;
	private int powerType;
	private int backupPower;
	private int batteryRemain;
	private int dbState;
	private int cableNetState;
	private int wireLessState;
	private int software;
	private String softVersion;
	private int camerCount;
	private int vehPlate1;
	private int vehPlate2;
	private int vehPlate3;
	private int vehPlate4;
	private int vehPlate5;
	private int vehPlate6;
	private int vehPlate7;
	private int vehPlate8;
	private int vehPlate9;
	private int vehPlate10;
	private int vehPlate11;
	private int vehPlate12;
	private int vehPlate13;
	private int vehPlate14;
	private int vehPlate15;
	private int vehPlate16;
	private int rsuCount;
	private int rsu1;
	private int rsu1Power;
	private int rsu1Channel;
	private int rsu1Switch;
	private int rsu1ErrInfo;
	private int rsu2;
	private int rsu2Power;
	private int rsu2Channel;
	private int rsu2Switch;
	private int rsu2ErrInfo;
	private int rsu3;
	private int rsu3Power;
	private int rsu3Channel;
	private int rsu3Switch;
	private int rsu3ErrInfo;
	private int rsu4;
	private int rsu4Power;
	private int rsu4Channel;
	private int rsu4Switch;
	private int rsu4ErrInfo;
	private int rsu5;
	private int rsu5Power;
	private int rsu5Channel;
	private int rsu5Switch;
	private int rsu5ErrInfo;
	private int rsu6;
	private int rsu6Power;
	private int rsu6Channel;
	private int rsu6Switch;
	private int rsu6ErrInfo;
	private int rsu7;
	private int rsu7Power;
	private int rsu7Channel;
	private int rsu7Switch;
	private int rsu7ErrInfo;
	private int rsu8;
	private int rsu8Power;
	private int rsu8Channel;
	private int rsu8Switch;
	private int rsu8ErrInfo;
	private int rsu9;
	private int rsu9Power;
	private int rsu9Channel;
	private int rsu9Switch;
	private int rsu9ErrInfo;
	private int rsu10;
	private int rsu10Power;
	private int rsu10Channel;
	private int rsu10Switch;
	private int rsu10ErrInfo;
	private int rsu11;
	private int rsu11Power;
	private int rsu11Channel;
	private int rsu11Switch;
	private int rsu11ErrInfo;
	private int rsu12;
	private int rsu12Power;
	private int rsu12Channel;
	private int rsu12Switch;
	private int rsu12ErrInfo;
	private int rsu13;
	private int rsu13Power;
	private int rsu13Channel;
	private int rsu13Switch;
	private int rsu13ErrInfo;
	private int rsu14;
	private int rsu14Power;
	private int rsu14Channel;
	private int rsu14Switch;
	private int rsu14ErrInfo;
	private int rsu15;
	private int rsu15Power;
	private int rsu15Channel;
	private int rsu15Switch;
	private int rsu15ErrInfo;
	private int rsu16;
	private int rsu16Power;
	private int rsu16Channel;
	private int rsu16Switch;
	private int rsu16ErrInfo;
	private String backup1;
	private String backup2;
	private String backup3;
	private String backup4;
	private Date timeFlag;

	/**
	 * Empty constructor.
	 */
	public FlagRunStatus() {
		super();
	}
	
	public FlagRunStatusID getId() {
		return new FlagRunStatusID(flagNetRoadID, flagRoadID, flagID, posId, direction, catchTime);
	}

	/**
	 * @return the recordNO
	 */
	public int getRecordNo() {
		return recordNo;
	}

	/**
	 * @param recordNo
	 *            the recordNO to set
	 */
	public void setRecordNo(final int recordNo) {
		this.recordNo = recordNo;
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
	 * @return the dirDescription
	 */
	public String getDirDescription() {
		return dirDescription;
	}

	/**
	 * @param dirDescription
	 *            the dirDescription to set
	 */
	public void setDirDescription(final String dirDescription) {
		this.dirDescription = dirDescription;
	}

	/**
	 * @return the catchTime
	 */
	public Date getCatchTime() {
		return catchTime;
	}

	/**
	 * @param catchTime
	 *            the catchTime to set
	 */
	public void setCatchTime(final Date catchTime) {
		this.catchTime = catchTime;
	}

	/**
	 * @return the computer
	 */
	public int getComputer() {
		return computer;
	}

	/**
	 * @param computer
	 *            the computer to set
	 */
	public void setComputer(final int computer) {
		this.computer = computer;
	}

	/**
	 * @return the diskCapacity
	 */
	public int getDiskCapacity() {
		return diskCapacity;
	}

	/**
	 * @param diskCapacity
	 *            the diskCapacity to set
	 */
	public void setDiskCapacity(final int diskCapacity) {
		this.diskCapacity = diskCapacity;
	}

	/**
	 * @return the diskUsage
	 */
	public int getDiskUsage() {
		return diskUsage;
	}

	/**
	 * @param diskUsage
	 *            the diskUsage to set
	 */
	public void setDiskUsage(final int diskUsage) {
		this.diskUsage = diskUsage;
	}

	/**
	 * @return the powerType
	 */
	public int getPowerType() {
		return powerType;
	}

	/**
	 * @param powerType
	 *            the powerType to set
	 */
	public void setPowerType(final int powerType) {
		this.powerType = powerType;
	}

	/**
	 * @return the backupPower
	 */
	public int getBackupPower() {
		return backupPower;
	}

	/**
	 * @param backupPower
	 *            the backupPower to set
	 */
	public void setBackupPower(final int backupPower) {
		this.backupPower = backupPower;
	}

	/**
	 * @return the batteryRemain
	 */
	public int getBatteryRemain() {
		return batteryRemain;
	}

	/**
	 * @param batteryRemain
	 *            the batteryRemain to set
	 */
	public void setBatteryRemain(final int batteryRemain) {
		this.batteryRemain = batteryRemain;
	}

	/**
	 * @return the dbState
	 */
	public int getDbState() {
		return dbState;
	}

	/**
	 * @param dbState
	 *            the dbState to set
	 */
	public void setDbState(final int dbState) {
		this.dbState = dbState;
	}

	/**
	 * @return the cableNetState
	 */
	public int getCableNetState() {
		return cableNetState;
	}

	/**
	 * @param cableNetState
	 *            the cableNetState to set
	 */
	public void setCableNetState(final int cableNetState) {
		this.cableNetState = cableNetState;
	}

	/**
	 * @return the wireLessState
	 */
	public int getWireLessState() {
		return wireLessState;
	}

	/**
	 * @param wireLessState
	 *            the wireLessState to set
	 */
	public void setWireLessState(final int wireLessState) {
		this.wireLessState = wireLessState;
	}

	/**
	 * @return the software
	 */
	public int getSoftware() {
		return software;
	}

	/**
	 * @param software
	 *            the software to set
	 */
	public void setSoftware(final int software) {
		this.software = software;
	}

	/**
	 * @return the softVersion
	 */
	public String getSoftVersion() {
		return softVersion;
	}

	/**
	 * @param softVersion
	 *            the softVersion to set
	 */
	public void setSoftVersion(final String softVersion) {
		this.softVersion = softVersion;
	}

	/**
	 * @return the camerCount
	 */
	public int getCamerCount() {
		return camerCount;
	}

	/**
	 * @param camerCount
	 *            the camerCount to set
	 */
	public void setCamerCount(final int camerCount) {
		this.camerCount = camerCount;
	}

	/**
	 * @return the vehPlate1
	 */
	public int getVehPlate1() {
		return vehPlate1;
	}

	/**
	 * @param vehPlate1
	 *            the vehPlate1 to set
	 */
	public void setVehPlate1(final int vehPlate1) {
		this.vehPlate1 = vehPlate1;
	}

	/**
	 * @return the vehPlate2
	 */
	public int getVehPlate2() {
		return vehPlate2;
	}

	/**
	 * @param vehPlate2
	 *            the vehPlate2 to set
	 */
	public void setVehPlate2(final int vehPlate2) {
		this.vehPlate2 = vehPlate2;
	}

	/**
	 * @return the vehPlate3
	 */
	public int getVehPlate3() {
		return vehPlate3;
	}

	/**
	 * @param vehPlate3
	 *            the vehPlate3 to set
	 */
	public void setVehPlate3(final int vehPlate3) {
		this.vehPlate3 = vehPlate3;
	}

	/**
	 * @return the vehPlate4
	 */
	public int getVehPlate4() {
		return vehPlate4;
	}

	/**
	 * @param vehPlate4
	 *            the vehPlate4 to set
	 */
	public void setVehPlate4(final int vehPlate4) {
		this.vehPlate4 = vehPlate4;
	}

	/**
	 * @return the vehPlate5
	 */
	public int getVehPlate5() {
		return vehPlate5;
	}

	/**
	 * @param vehPlate5
	 *            the vehPlate5 to set
	 */
	public void setVehPlate5(final int vehPlate5) {
		this.vehPlate5 = vehPlate5;
	}

	/**
	 * @return the vehPlate6
	 */
	public int getVehPlate6() {
		return vehPlate6;
	}

	/**
	 * @param vehPlate6
	 *            the vehPlate6 to set
	 */
	public void setVehPlate6(final int vehPlate6) {
		this.vehPlate6 = vehPlate6;
	}

	/**
	 * @return the vehPlate7
	 */
	public int getVehPlate7() {
		return vehPlate7;
	}

	/**
	 * @param vehPlate7
	 *            the vehPlate7 to set
	 */
	public void setVehPlate7(final int vehPlate7) {
		this.vehPlate7 = vehPlate7;
	}

	/**
	 * @return the vehPlate8
	 */
	public int getVehPlate8() {
		return vehPlate8;
	}

	/**
	 * @param vehPlate8
	 *            the vehPlate8 to set
	 */
	public void setVehPlate8(final int vehPlate8) {
		this.vehPlate8 = vehPlate8;
	}

	/**
	 * @return the vehPlate9
	 */
	public int getVehPlate9() {
		return vehPlate9;
	}

	/**
	 * @param vehPlate9
	 *            the vehPlate9 to set
	 */
	public void setVehPlate9(final int vehPlate9) {
		this.vehPlate9 = vehPlate9;
	}

	/**
	 * @return the vehPlate10
	 */
	public int getVehPlate10() {
		return vehPlate10;
	}

	/**
	 * @param vehPlate10
	 *            the vehPlate10 to set
	 */
	public void setVehPlate10(final int vehPlate10) {
		this.vehPlate10 = vehPlate10;
	}

	/**
	 * @return the vehPlate11
	 */
	public int getVehPlate11() {
		return vehPlate11;
	}

	/**
	 * @param vehPlate11
	 *            the vehPlate11 to set
	 */
	public void setVehPlate11(final int vehPlate11) {
		this.vehPlate11 = vehPlate11;
	}

	/**
	 * @return the vehPlate12
	 */
	public int getVehPlate12() {
		return vehPlate12;
	}

	/**
	 * @param vehPlate12
	 *            the vehPlate12 to set
	 */
	public void setVehPlate12(final int vehPlate12) {
		this.vehPlate12 = vehPlate12;
	}

	/**
	 * @return the vehPlate13
	 */
	public int getVehPlate13() {
		return vehPlate13;
	}

	/**
	 * @param vehPlate13
	 *            the vehPlate13 to set
	 */
	public void setVehPlate13(final int vehPlate13) {
		this.vehPlate13 = vehPlate13;
	}

	/**
	 * @return the vehPlate14
	 */
	public int getVehPlate14() {
		return vehPlate14;
	}

	/**
	 * @param vehPlate14
	 *            the vehPlate14 to set
	 */
	public void setVehPlate14(final int vehPlate14) {
		this.vehPlate14 = vehPlate14;
	}

	/**
	 * @return the vehPlate15
	 */
	public int getVehPlate15() {
		return vehPlate15;
	}

	/**
	 * @param vehPlate15
	 *            the vehPlate15 to set
	 */
	public void setVehPlate15(final int vehPlate15) {
		this.vehPlate15 = vehPlate15;
	}

	/**
	 * @return the vehPlate16
	 */
	public int getVehPlate16() {
		return vehPlate16;
	}

	/**
	 * @param vehPlate16
	 *            the vehPlate16 to set
	 */
	public void setVehPlate16(final int vehPlate16) {
		this.vehPlate16 = vehPlate16;
	}

	/**
	 * @return the rsuCount
	 */
	public int getRsuCount() {
		return rsuCount;
	}

	/**
	 * @param rsuCount
	 *            the rsuCount to set
	 */
	public void setRsuCount(final int rsuCount) {
		this.rsuCount = rsuCount;
	}

	/**
	 * @return the rsu1
	 */
	public int getRsu1() {
		return rsu1;
	}

	/**
	 * @param rsu1
	 *            the rsu1 to set
	 */
	public void setRsu1(final int rsu1) {
		this.rsu1 = rsu1;
	}

	/**
	 * @return the rsu1Power
	 */
	public int getRsu1Power() {
		return rsu1Power;
	}

	/**
	 * @param rsu1Power
	 *            the rsu1Power to set
	 */
	public void setRsu1Power(final int rsu1Power) {
		this.rsu1Power = rsu1Power;
	}

	/**
	 * @return the rsu1Channel
	 */
	public int getRsu1Channel() {
		return rsu1Channel;
	}

	/**
	 * @param rsu1Channel
	 *            the rsu1Channel to set
	 */
	public void setRsu1Channel(final int rsu1Channel) {
		this.rsu1Channel = rsu1Channel;
	}

	/**
	 * @return the rsu1Switch
	 */
	public int getRsu1Switch() {
		return rsu1Switch;
	}

	/**
	 * @param rsu1Switch
	 *            the rsu1Switch to set
	 */
	public void setRsu1Switch(final int rsu1Switch) {
		this.rsu1Switch = rsu1Switch;
	}

	/**
	 * @return the rsu1ErrInfo
	 */
	public int getRsu1ErrInfo() {
		return rsu1ErrInfo;
	}

	/**
	 * @param rsu1ErrInfo
	 *            the rsu1ErrInfo to set
	 */
	public void setRsu1ErrInfo(final int rsu1ErrInfo) {
		this.rsu1ErrInfo = rsu1ErrInfo;
	}

	/**
	 * @return the rsu2
	 */
	public int getRsu2() {
		return rsu2;
	}

	/**
	 * @param rsu2
	 *            the rsu2 to set
	 */
	public void setRsu2(final int rsu2) {
		this.rsu2 = rsu2;
	}

	/**
	 * @return the rsu2Power
	 */
	public int getRsu2Power() {
		return rsu2Power;
	}

	/**
	 * @param rsu2Power
	 *            the rsu2Power to set
	 */
	public void setRsu2Power(final int rsu2Power) {
		this.rsu2Power = rsu2Power;
	}

	/**
	 * @return the rsu2Channel
	 */
	public int getRsu2Channel() {
		return rsu2Channel;
	}

	/**
	 * @param rsu2Channel
	 *            the rsu2Channel to set
	 */
	public void setRsu2Channel(final int rsu2Channel) {
		this.rsu2Channel = rsu2Channel;
	}

	/**
	 * @return the rsu2Switch
	 */
	public int getRsu2Switch() {
		return rsu2Switch;
	}

	/**
	 * @param rsu2Switch
	 *            the rsu2Switch to set
	 */
	public void setRsu2Switch(final int rsu2Switch) {
		this.rsu2Switch = rsu2Switch;
	}

	/**
	 * @return the rsu2ErrInfo
	 */
	public int getRsu2ErrInfo() {
		return rsu2ErrInfo;
	}

	/**
	 * @param rsu2ErrInfo
	 *            the rsu2ErrInfo to set
	 */
	public void setRsu2ErrInfo(final int rsu2ErrInfo) {
		this.rsu2ErrInfo = rsu2ErrInfo;
	}

	/**
	 * @return the rsu3
	 */
	public int getRsu3() {
		return rsu3;
	}

	/**
	 * @param rsu3
	 *            the rsu3 to set
	 */
	public void setRsu3(final int rsu3) {
		this.rsu3 = rsu3;
	}

	/**
	 * @return the rsu3Power
	 */
	public int getRsu3Power() {
		return rsu3Power;
	}

	/**
	 * @param rsu3Power
	 *            the rsu3Power to set
	 */
	public void setRsu3Power(final int rsu3Power) {
		this.rsu3Power = rsu3Power;
	}

	/**
	 * @return the rsu3Channel
	 */
	public int getRsu3Channel() {
		return rsu3Channel;
	}

	/**
	 * @param rsu3Channel
	 *            the rsu3Channel to set
	 */
	public void setRsu3Channel(final int rsu3Channel) {
		this.rsu3Channel = rsu3Channel;
	}

	/**
	 * @return the rsu3Switch
	 */
	public int getRsu3Switch() {
		return rsu3Switch;
	}

	/**
	 * @param rsu3Switch
	 *            the rsu3Switch to set
	 */
	public void setRsu3Switch(final int rsu3Switch) {
		this.rsu3Switch = rsu3Switch;
	}

	/**
	 * @return the rsu3ErrInfo
	 */
	public int getRsu3ErrInfo() {
		return rsu3ErrInfo;
	}

	/**
	 * @param rsu3ErrInfo
	 *            the rsu3ErrInfo to set
	 */
	public void setRsu3ErrInfo(final int rsu3ErrInfo) {
		this.rsu3ErrInfo = rsu3ErrInfo;
	}

	/**
	 * @return the rsu4
	 */
	public int getRsu4() {
		return rsu4;
	}

	/**
	 * @param rsu4
	 *            the rsu4 to set
	 */
	public void setRsu4(final int rsu4) {
		this.rsu4 = rsu4;
	}

	/**
	 * @return the rsu4Power
	 */
	public int getRsu4Power() {
		return rsu4Power;
	}

	/**
	 * @param rsu4Power
	 *            the rsu4Power to set
	 */
	public void setRsu4Power(final int rsu4Power) {
		this.rsu4Power = rsu4Power;
	}

	/**
	 * @return the rsu4Channel
	 */
	public int getRsu4Channel() {
		return rsu4Channel;
	}

	/**
	 * @param rsu4Channel
	 *            the rsu4Channel to set
	 */
	public void setRsu4Channel(final int rsu4Channel) {
		this.rsu4Channel = rsu4Channel;
	}

	/**
	 * @return the rsu4Switch
	 */
	public int getRsu4Switch() {
		return rsu4Switch;
	}

	/**
	 * @param rsu4Switch
	 *            the rsu4Switch to set
	 */
	public void setRsu4Switch(final int rsu4Switch) {
		this.rsu4Switch = rsu4Switch;
	}

	/**
	 * @return the rsu4ErrInfo
	 */
	public int getRsu4ErrInfo() {
		return rsu4ErrInfo;
	}

	/**
	 * @param rsu4ErrInfo
	 *            the rsu4ErrInfo to set
	 */
	public void setRsu4ErrInfo(final int rsu4ErrInfo) {
		this.rsu4ErrInfo = rsu4ErrInfo;
	}

	/**
	 * @return the rsu5
	 */
	public int getRsu5() {
		return rsu5;
	}

	/**
	 * @param rsu5
	 *            the rsu5 to set
	 */
	public void setRsu5(final int rsu5) {
		this.rsu5 = rsu5;
	}

	/**
	 * @return the rsu5Power
	 */
	public int getRsu5Power() {
		return rsu5Power;
	}

	/**
	 * @param rsu5Power
	 *            the rsu5Power to set
	 */
	public void setRsu5Power(final int rsu5Power) {
		this.rsu5Power = rsu5Power;
	}

	/**
	 * @return the rsu5Channel
	 */
	public int getRsu5Channel() {
		return rsu5Channel;
	}

	/**
	 * @param rsu5Channel
	 *            the rsu5Channel to set
	 */
	public void setRsu5Channel(final int rsu5Channel) {
		this.rsu5Channel = rsu5Channel;
	}

	/**
	 * @return the rsu5Switch
	 */
	public int getRsu5Switch() {
		return rsu5Switch;
	}

	/**
	 * @param rsu5Switch
	 *            the rsu5Switch to set
	 */
	public void setRsu5Switch(final int rsu5Switch) {
		this.rsu5Switch = rsu5Switch;
	}

	/**
	 * @return the rsu5ErrInfo
	 */
	public int getRsu5ErrInfo() {
		return rsu5ErrInfo;
	}

	/**
	 * @param rsu5ErrInfo
	 *            the rsu5ErrInfo to set
	 */
	public void setRsu5ErrInfo(final int rsu5ErrInfo) {
		this.rsu5ErrInfo = rsu5ErrInfo;
	}

	/**
	 * @return the rsu6
	 */
	public int getRsu6() {
		return rsu6;
	}

	/**
	 * @param rsu6
	 *            the rsu6 to set
	 */
	public void setRsu6(final int rsu6) {
		this.rsu6 = rsu6;
	}

	/**
	 * @return the rsu6Power
	 */
	public int getRsu6Power() {
		return rsu6Power;
	}

	/**
	 * @param rsu6Power
	 *            the rsu6Power to set
	 */
	public void setRsu6Power(final int rsu6Power) {
		this.rsu6Power = rsu6Power;
	}

	/**
	 * @return the rsu6Channel
	 */
	public int getRsu6Channel() {
		return rsu6Channel;
	}

	/**
	 * @param rsu6Channel
	 *            the rsu6Channel to set
	 */
	public void setRsu6Channel(final int rsu6Channel) {
		this.rsu6Channel = rsu6Channel;
	}

	/**
	 * @return the rsu6Switch
	 */
	public int getRsu6Switch() {
		return rsu6Switch;
	}

	/**
	 * @param rsu6Switch
	 *            the rsu6Switch to set
	 */
	public void setRsu6Switch(final int rsu6Switch) {
		this.rsu6Switch = rsu6Switch;
	}

	/**
	 * @return the rsu6ErrInfo
	 */
	public int getRsu6ErrInfo() {
		return rsu6ErrInfo;
	}

	/**
	 * @param rsu6ErrInfo
	 *            the rsu6ErrInfo to set
	 */
	public void setRsu6ErrInfo(final int rsu6ErrInfo) {
		this.rsu6ErrInfo = rsu6ErrInfo;
	}

	/**
	 * @return the rsu7
	 */
	public int getRsu7() {
		return rsu7;
	}

	/**
	 * @param rsu7
	 *            the rsu7 to set
	 */
	public void setRsu7(final int rsu7) {
		this.rsu7 = rsu7;
	}

	/**
	 * @return the rsu7Power
	 */
	public int getRsu7Power() {
		return rsu7Power;
	}

	/**
	 * @param rsu7Power
	 *            the rsu7Power to set
	 */
	public void setRsu7Power(final int rsu7Power) {
		this.rsu7Power = rsu7Power;
	}

	/**
	 * @return the rsu7Channel
	 */
	public int getRsu7Channel() {
		return rsu7Channel;
	}

	/**
	 * @param rsu7Channel
	 *            the rsu7Channel to set
	 */
	public void setRsu7Channel(final int rsu7Channel) {
		this.rsu7Channel = rsu7Channel;
	}

	/**
	 * @return the rsu7Switch
	 */
	public int getRsu7Switch() {
		return rsu7Switch;
	}

	/**
	 * @param rsu7Switch
	 *            the rsu7Switch to set
	 */
	public void setRsu7Switch(final int rsu7Switch) {
		this.rsu7Switch = rsu7Switch;
	}

	/**
	 * @return the rsu7ErrInfo
	 */
	public int getRsu7ErrInfo() {
		return rsu7ErrInfo;
	}

	/**
	 * @param rsu7ErrInfo
	 *            the rsu7ErrInfo to set
	 */
	public void setRsu7ErrInfo(final int rsu7ErrInfo) {
		this.rsu7ErrInfo = rsu7ErrInfo;
	}

	/**
	 * @return the rsu8
	 */
	public int getRsu8() {
		return rsu8;
	}

	/**
	 * @param rsu8
	 *            the rsu8 to set
	 */
	public void setRsu8(final int rsu8) {
		this.rsu8 = rsu8;
	}

	/**
	 * @return the rsu8Power
	 */
	public int getRsu8Power() {
		return rsu8Power;
	}

	/**
	 * @param rsu8Power
	 *            the rsu8Power to set
	 */
	public void setRsu8Power(final int rsu8Power) {
		this.rsu8Power = rsu8Power;
	}

	/**
	 * @return the rsu8Channel
	 */
	public int getRsu8Channel() {
		return rsu8Channel;
	}

	/**
	 * @param rsu8Channel
	 *            the rsu8Channel to set
	 */
	public void setRsu8Channel(final int rsu8Channel) {
		this.rsu8Channel = rsu8Channel;
	}

	/**
	 * @return the rsu8Switch
	 */
	public int getRsu8Switch() {
		return rsu8Switch;
	}

	/**
	 * @param rsu8Switch
	 *            the rsu8Switch to set
	 */
	public void setRsu8Switch(final int rsu8Switch) {
		this.rsu8Switch = rsu8Switch;
	}

	/**
	 * @return the rsu8ErrInfo
	 */
	public int getRsu8ErrInfo() {
		return rsu8ErrInfo;
	}

	/**
	 * @param rsu8ErrInfo
	 *            the rsu8ErrInfo to set
	 */
	public void setRsu8ErrInfo(final int rsu8ErrInfo) {
		this.rsu8ErrInfo = rsu8ErrInfo;
	}

	/**
	 * @return the rsu9
	 */
	public int getRsu9() {
		return rsu9;
	}

	/**
	 * @param rsu9
	 *            the rsu9 to set
	 */
	public void setRsu9(final int rsu9) {
		this.rsu9 = rsu9;
	}

	/**
	 * @return the rsu9Power
	 */
	public int getRsu9Power() {
		return rsu9Power;
	}

	/**
	 * @param rsu9Power
	 *            the rsu9Power to set
	 */
	public void setRsu9Power(final int rsu9Power) {
		this.rsu9Power = rsu9Power;
	}

	/**
	 * @return the rsu9Channel
	 */
	public int getRsu9Channel() {
		return rsu9Channel;
	}

	/**
	 * @param rsu9Channel
	 *            the rsu9Channel to set
	 */
	public void setRsu9Channel(final int rsu9Channel) {
		this.rsu9Channel = rsu9Channel;
	}

	/**
	 * @return the rsu9Switch
	 */
	public int getRsu9Switch() {
		return rsu9Switch;
	}

	/**
	 * @param rsu9Switch
	 *            the rsu9Switch to set
	 */
	public void setRsu9Switch(final int rsu9Switch) {
		this.rsu9Switch = rsu9Switch;
	}

	/**
	 * @return the rsu9ErrInfo
	 */
	public int getRsu9ErrInfo() {
		return rsu9ErrInfo;
	}

	/**
	 * @param rsu9ErrInfo
	 *            the rsu9ErrInfo to set
	 */
	public void setRsu9ErrInfo(final int rsu9ErrInfo) {
		this.rsu9ErrInfo = rsu9ErrInfo;
	}

	/**
	 * @return the rsu10
	 */
	public int getRsu10() {
		return rsu10;
	}

	/**
	 * @param rsu10
	 *            the rsu10 to set
	 */
	public void setRsu10(final int rsu10) {
		this.rsu10 = rsu10;
	}

	/**
	 * @return the rsu10Power
	 */
	public int getRsu10Power() {
		return rsu10Power;
	}

	/**
	 * @param rsu10Power
	 *            the rsu10Power to set
	 */
	public void setRsu10Power(final int rsu10Power) {
		this.rsu10Power = rsu10Power;
	}

	/**
	 * @return the rsu10Channel
	 */
	public int getRsu10Channel() {
		return rsu10Channel;
	}

	/**
	 * @param rsu10Channel
	 *            the rsu10Channel to set
	 */
	public void setRsu10Channel(final int rsu10Channel) {
		this.rsu10Channel = rsu10Channel;
	}

	/**
	 * @return the rsu10Switch
	 */
	public int getRsu10Switch() {
		return rsu10Switch;
	}

	/**
	 * @param rsu10Switch
	 *            the rsu10Switch to set
	 */
	public void setRsu10Switch(final int rsu10Switch) {
		this.rsu10Switch = rsu10Switch;
	}

	/**
	 * @return the rsu10ErrInfo
	 */
	public int getRsu10ErrInfo() {
		return rsu10ErrInfo;
	}

	/**
	 * @param rsu10ErrInfo
	 *            the rsu10ErrInfo to set
	 */
	public void setRsu10ErrInfo(final int rsu10ErrInfo) {
		this.rsu10ErrInfo = rsu10ErrInfo;
	}

	/**
	 * @return the rsu11
	 */
	public int getRsu11() {
		return rsu11;
	}

	/**
	 * @param rsu11
	 *            the rsu11 to set
	 */
	public void setRsu11(final int rsu11) {
		this.rsu11 = rsu11;
	}

	/**
	 * @return the rsu11Power
	 */
	public int getRsu11Power() {
		return rsu11Power;
	}

	/**
	 * @param rsu11Power
	 *            the rsu11Power to set
	 */
	public void setRsu11Power(final int rsu11Power) {
		this.rsu11Power = rsu11Power;
	}

	/**
	 * @return the rsu11Channel
	 */
	public int getRsu11Channel() {
		return rsu11Channel;
	}

	/**
	 * @param rsu11Channel
	 *            the rsu11Channel to set
	 */
	public void setRsu11Channel(final int rsu11Channel) {
		this.rsu11Channel = rsu11Channel;
	}

	/**
	 * @return the rsu11Switch
	 */
	public int getRsu11Switch() {
		return rsu11Switch;
	}

	/**
	 * @param rsu11Switch
	 *            the rsu11Switch to set
	 */
	public void setRsu11Switch(final int rsu11Switch) {
		this.rsu11Switch = rsu11Switch;
	}

	/**
	 * @return the rsu11ErrInfo
	 */
	public int getRsu11ErrInfo() {
		return rsu11ErrInfo;
	}

	/**
	 * @param rsu11ErrInfo
	 *            the rsu11ErrInfo to set
	 */
	public void setRsu11ErrInfo(final int rsu11ErrInfo) {
		this.rsu11ErrInfo = rsu11ErrInfo;
	}

	/**
	 * @return the rsu12
	 */
	public int getRsu12() {
		return rsu12;
	}

	/**
	 * @param rsu12
	 *            the rsu12 to set
	 */
	public void setRsu12(final int rsu12) {
		this.rsu12 = rsu12;
	}

	/**
	 * @return the rsu12Power
	 */
	public int getRsu12Power() {
		return rsu12Power;
	}

	/**
	 * @param rsu12Power
	 *            the rsu12Power to set
	 */
	public void setRsu12Power(final int rsu12Power) {
		this.rsu12Power = rsu12Power;
	}

	/**
	 * @return the rsu12Channel
	 */
	public int getRsu12Channel() {
		return rsu12Channel;
	}

	/**
	 * @param rsu12Channel
	 *            the rsu12Channel to set
	 */
	public void setRsu12Channel(final int rsu12Channel) {
		this.rsu12Channel = rsu12Channel;
	}

	/**
	 * @return the rsu12Switch
	 */
	public int getRsu12Switch() {
		return rsu12Switch;
	}

	/**
	 * @param rsu12Switch
	 *            the rsu12Switch to set
	 */
	public void setRsu12Switch(final int rsu12Switch) {
		this.rsu12Switch = rsu12Switch;
	}

	/**
	 * @return the rsu12ErrInfo
	 */
	public int getRsu12ErrInfo() {
		return rsu12ErrInfo;
	}

	/**
	 * @param rsu12ErrInfo
	 *            the rsu12ErrInfo to set
	 */
	public void setRsu12ErrInfo(final int rsu12ErrInfo) {
		this.rsu12ErrInfo = rsu12ErrInfo;
	}

	/**
	 * @return the rsu13
	 */
	public int getRsu13() {
		return rsu13;
	}

	/**
	 * @param rsu13
	 *            the rsu13 to set
	 */
	public void setRsu13(final int rsu13) {
		this.rsu13 = rsu13;
	}

	/**
	 * @return the rsu13Power
	 */
	public int getRsu13Power() {
		return rsu13Power;
	}

	/**
	 * @param rsu13Power
	 *            the rsu13Power to set
	 */
	public void setRsu13Power(final int rsu13Power) {
		this.rsu13Power = rsu13Power;
	}

	/**
	 * @return the rsu13Channel
	 */
	public int getRsu13Channel() {
		return rsu13Channel;
	}

	/**
	 * @param rsu13Channel
	 *            the rsu13Channel to set
	 */
	public void setRsu13Channel(final int rsu13Channel) {
		this.rsu13Channel = rsu13Channel;
	}

	/**
	 * @return the rsu13Switch
	 */
	public int getRsu13Switch() {
		return rsu13Switch;
	}

	/**
	 * @param rsu13Switch
	 *            the rsu13Switch to set
	 */
	public void setRsu13Switch(final int rsu13Switch) {
		this.rsu13Switch = rsu13Switch;
	}

	/**
	 * @return the rsu13ErrInfo
	 */
	public int getRsu13ErrInfo() {
		return rsu13ErrInfo;
	}

	/**
	 * @param rsu13ErrInfo
	 *            the rsu13ErrInfo to set
	 */
	public void setRsu13ErrInfo(final int rsu13ErrInfo) {
		this.rsu13ErrInfo = rsu13ErrInfo;
	}

	/**
	 * @return the rsu14
	 */
	public int getRsu14() {
		return rsu14;
	}

	/**
	 * @param rsu14
	 *            the rsu14 to set
	 */
	public void setRsu14(final int rsu14) {
		this.rsu14 = rsu14;
	}

	/**
	 * @return the rsu14Power
	 */
	public int getRsu14Power() {
		return rsu14Power;
	}

	/**
	 * @param rsu14Power
	 *            the rsu14Power to set
	 */
	public void setRsu14Power(final int rsu14Power) {
		this.rsu14Power = rsu14Power;
	}

	/**
	 * @return the rsu14Channel
	 */
	public int getRsu14Channel() {
		return rsu14Channel;
	}

	/**
	 * @param rsu14Channel
	 *            the rsu14Channel to set
	 */
	public void setRsu14Channel(final int rsu14Channel) {
		this.rsu14Channel = rsu14Channel;
	}

	/**
	 * @return the rsu14Switch
	 */
	public int getRsu14Switch() {
		return rsu14Switch;
	}

	/**
	 * @param rsu14Switch
	 *            the rsu14Switch to set
	 */
	public void setRsu14Switch(final int rsu14Switch) {
		this.rsu14Switch = rsu14Switch;
	}

	/**
	 * @return the rsu14ErrInfo
	 */
	public int getRsu14ErrInfo() {
		return rsu14ErrInfo;
	}

	/**
	 * @param rsu14ErrInfo
	 *            the rsu14ErrInfo to set
	 */
	public void setRsu14ErrInfo(final int rsu14ErrInfo) {
		this.rsu14ErrInfo = rsu14ErrInfo;
	}

	/**
	 * @return the rsu15
	 */
	public int getRsu15() {
		return rsu15;
	}

	/**
	 * @param rsu15
	 *            the rsu15 to set
	 */
	public void setRsu15(final int rsu15) {
		this.rsu15 = rsu15;
	}

	/**
	 * @return the rsu15Power
	 */
	public int getRsu15Power() {
		return rsu15Power;
	}

	/**
	 * @param rsu15Power
	 *            the rsu15Power to set
	 */
	public void setRsu15Power(final int rsu15Power) {
		this.rsu15Power = rsu15Power;
	}

	/**
	 * @return the rsu15Channel
	 */
	public int getRsu15Channel() {
		return rsu15Channel;
	}

	/**
	 * @param rsu15Channel
	 *            the rsu15Channel to set
	 */
	public void setRsu15Channel(final int rsu15Channel) {
		this.rsu15Channel = rsu15Channel;
	}

	/**
	 * @return the rsu15Switch
	 */
	public int getRsu15Switch() {
		return rsu15Switch;
	}

	/**
	 * @param rsu15Switch
	 *            the rsu15Switch to set
	 */
	public void setRsu15Switch(final int rsu15Switch) {
		this.rsu15Switch = rsu15Switch;
	}

	/**
	 * @return the rsu15ErrInfo
	 */
	public int getRsu15ErrInfo() {
		return rsu15ErrInfo;
	}

	/**
	 * @param rsu15ErrInfo
	 *            the rsu15ErrInfo to set
	 */
	public void setRsu15ErrInfo(final int rsu15ErrInfo) {
		this.rsu15ErrInfo = rsu15ErrInfo;
	}

	/**
	 * @return the rsu16
	 */
	public int getRsu16() {
		return rsu16;
	}

	/**
	 * @param rsu16
	 *            the rsu16 to set
	 */
	public void setRsu16(final int rsu16) {
		this.rsu16 = rsu16;
	}

	/**
	 * @return the rsu16Power
	 */
	public int getRsu16Power() {
		return rsu16Power;
	}

	/**
	 * @param rsu16Power
	 *            the rsu16Power to set
	 */
	public void setRsu16Power(final int rsu16Power) {
		this.rsu16Power = rsu16Power;
	}

	/**
	 * @return the rsu16Channel
	 */
	public int getRsu16Channel() {
		return rsu16Channel;
	}

	/**
	 * @param rsu16Channel
	 *            the rsu16Channel to set
	 */
	public void setRsu16Channel(final int rsu16Channel) {
		this.rsu16Channel = rsu16Channel;
	}

	/**
	 * @return the rsu16Switch
	 */
	public int getRsu16Switch() {
		return rsu16Switch;
	}

	/**
	 * @param rsu16Switch
	 *            the rsu16Switch to set
	 */
	public void setRsu16Switch(final int rsu16Switch) {
		this.rsu16Switch = rsu16Switch;
	}

	/**
	 * @return the rsu16ErrInfo
	 */
	public int getRsu16ErrInfo() {
		return rsu16ErrInfo;
	}

	/**
	 * @param rsu16ErrInfo
	 *            the rsu16ErrInfo to set
	 */
	public void setRsu16ErrInfo(final int rsu16ErrInfo) {
		this.rsu16ErrInfo = rsu16ErrInfo;
	}

	/**
	 * @return the backup1
	 */
	public String getBackup1() {
		return backup1;
	}

	/**
	 * @param backup1
	 *            the backup1 to set
	 */
	public void setBackup1(final String backup1) {
		this.backup1 = backup1;
	}

	/**
	 * @return the backup2
	 */
	public String getBackup2() {
		return backup2;
	}

	/**
	 * @param backup2
	 *            the backup2 to set
	 */
	public void setBackup2(final String backup2) {
		this.backup2 = backup2;
	}

	/**
	 * @return the backup3
	 */
	public String getBackup3() {
		return backup3;
	}

	/**
	 * @param backup3
	 *            the backup3 to set
	 */
	public void setBackup3(final String backup3) {
		this.backup3 = backup3;
	}

	/**
	 * @return the backup4
	 */
	public String getBackup4() {
		return backup4;
	}

	/**
	 * @param backup4
	 *            the backup4 to set
	 */
	public void setBackup4(final String backup4) {
		this.backup4 = backup4;
	}

	/**
	 * @return the timeFlag
	 */
	public Date getTimeFlag() {
		return timeFlag;
	}

	/**
	 * @param timeFlag
	 *            the timeFlag to set
	 */
	public void setTimeFlag(final Date timeFlag) {
		this.timeFlag = timeFlag;
	}

	/**
	 * The identifier of {@link FlagRunStatus}.
	 * 
	 * @author Dorsey
	 *
	 */
	public static class FlagRunStatusID {

		private int flagNetRoadID;
		private int flagRoadID;
		private int flagID;
		private int posId;
		private int direction;
		private Date catchTime;

		public FlagRunStatusID(final int flagNetRoadID, final int flagRoadID, final int flagID,
				final int posId, final int direction, final Date catchTime) {
			setFlagNetRoadID(flagNetRoadID);
			setFlagRoadID(flagRoadID);
			setFlagID(flagID);
			setPosId(posId);
			setDirection(direction);
			setCatchTime(catchTime);
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
		 * @return the catchTime
		 */
		public Date getCatchTime() {
			return catchTime;
		}

		/**
		 * @param catchTime
		 *            the catchTime to set
		 */
		public void setCatchTime(final Date catchTime) {
			this.catchTime = catchTime;
		}

		@Override
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;

			result = PRIME * result + direction;
			result = PRIME * result + flagID;
			result = PRIME * result + flagNetRoadID;
			result = PRIME * result + flagRoadID;
			result = PRIME * result + posId;

			return result + Objects.hash(catchTime);
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this)
				return true;
			if (obj == null || !(obj instanceof FlagRunStatusID))
				return false;

			FlagRunStatusID other = (FlagRunStatusID) obj;
			return (flagID == other.flagID && flagNetRoadID == other.flagNetRoadID
					&& flagRoadID == other.flagRoadID && posId == other.posId
					&& direction == other.direction && Objects.equals(catchTime, other.catchTime));
		}

		@Override
		public String toString() {
			return "FlagRunStatusID [flagNetRoadID=" + flagNetRoadID + ", flagRoadID=" + flagRoadID
					+ ", flagID=" + flagID + ", posId=" + posId + ", direction=" + direction
					+ ", catchTime=" + catchTime + "]";
		}

	}

}
