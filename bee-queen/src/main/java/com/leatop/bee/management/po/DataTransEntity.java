/**
 * File: SchemaPO.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.po;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * kafka主题数据传输实体类
 * 
 * @author zlm
 *
 */
public class DataTransEntity implements Serializable {

	private static final long serialVersionUID = -5179880441858351469L;

	private int id;
	private int transCount;//传输条数
	private int successCount;//成功条数
	private int errorCount;//失败条数
	private String topicName;//主题名称
	private Date startTime;//传输开始时间
	private Date endTime;//传输结束时间
	private String hostName;//主机名称
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTransCount() {
		return transCount;
	}
	public void setTransCount(int transCount) {
		this.transCount = transCount;
	}
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public int getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Override
	public String toString() {
		return "DataTransEntity [id=" + id + ", transCount=" + transCount + ", successCount="
				+ successCount + ", errorCount=" + errorCount + ", topicName=" + topicName
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", hostName=" + hostName
				+ "]";
	}
	
	
}
