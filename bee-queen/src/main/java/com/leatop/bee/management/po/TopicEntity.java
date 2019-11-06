/**
 * File: TopicPO.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.po;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 主题管理实体类
 * 
 * @author zlm
 *
 */
public class TopicEntity implements Serializable {

	private static final long serialVersionUID = -5179880441858351469L;
	private String name;//主题名称
	private String zookeeperHost;//zookeeper地址
	private int replicationFactor;//副本数量
	private int partitions;//分区数量

	/**
	 * Empty constructor of {@link TopicEntity}.
	 */
	public TopicEntity() {
	}

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getZookeeperHost() {
		return zookeeperHost;
	}



	public void setZookeeperHost(String zookeeperHost) {
		this.zookeeperHost = zookeeperHost;
	}



	public int getReplicationFactor() {
		return replicationFactor;
	}



	public void setReplicationFactor(int replicationFactor) {
		this.replicationFactor = replicationFactor;
	}



	public int getPartitions() {
		return partitions;
	}



	public void setPartitions(int partitions) {
		this.partitions = partitions;
	}

	@Override
	public String toString() {
		return "TopicEntity [name=" + name + ", zookeeperHost=" + zookeeperHost
				+ ", replicationFactor=" + replicationFactor + ", partitions=" + partitions + "]";
	}
	

}
