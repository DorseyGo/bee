/**
 * File: SendBroadEntity.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月22日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.buzi;

import java.sql.Date;

/**
 * @author Dorsey
 *
 */
public class SendBroadEntity {
	private int systemId;
	private String tableName;
	private String batchNo;
	private String version;
	private String protocolType;
	private Date createTime;
	private int numRecords;
	private int updateFlag;
	private Date updateTime;

	// ~~~ constructors
	// ===========================================================================================
	public SendBroadEntity(final String tableName, final String batchNo, final int numRecords) {
		this.tableName = tableName;
		this.batchNo = batchNo;
		this.numRecords = numRecords;
		this.systemId = 6;
		this.createTime = new Date(new java.util.Date().getTime());
		this.updateFlag = 0;
		this.updateTime = new Date(new java.util.Date().getTime());
	}

	/**
	 * @return the systemId
	 */
	public int getSystemId() {
		return systemId;
	}

	/**
	 * @param systemId
	 *            the systemId to set
	 */
	public void setSystemId(final int systemId) {
		this.systemId = systemId;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the batchNo
	 */
	public String getBatchNo() {
		return batchNo;
	}

	/**
	 * @param batchNo
	 *            the batchNo to set
	 */
	public void setBatchNo(final String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(final String version) {
		this.version = version;
	}

	/**
	 * @return the protocolType
	 */
	public String getProtocolType() {
		return protocolType;
	}

	/**
	 * @param protocolType
	 *            the protocolType to set
	 */
	public void setProtocolType(final String protocolType) {
		this.protocolType = protocolType;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the numRecords
	 */
	public int getNumRecords() {
		return numRecords;
	}

	/**
	 * @param numRecords
	 *            the numRecords to set
	 */
	public void setNumRecords(final int numRecords) {
		this.numRecords = numRecords;
	}

	/**
	 * @return the updateFlag
	 */
	public int getUpdateFlag() {
		return updateFlag;
	}

	/**
	 * @param updateFlag
	 *            the updateFlag to set
	 */
	public void setUpdateFlag(final int updateFlag) {
		this.updateFlag = updateFlag;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(final Date updateTime) {
		this.updateTime = updateTime;
	}

}
