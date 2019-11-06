/**
 * File: KafkaConfiguration.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * kafka配置信息类
 * 
 * @author zlm
 *
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "bee.queen")
@PropertySource(encoding = "UTF-8", value = "classpath:application.properties")
public class KafkaConfiguration {

	private String zookeeperHost;
    private int sessionTimeout; // 与 zookeeper 连接的 session 的过期时间
    private int connectTimeout; // 连接 zookeeper 的超时时间
    private String bootstrapServers;
    
	public String getBootstrapServers() {
		return bootstrapServers;
	}
	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}
	public String getZookeeperHost() {
		return zookeeperHost;
	}
	public void setZookeeperHost(String zookeeperHost) {
		this.zookeeperHost = zookeeperHost;
	}
	public int getSessionTimeout() {
		return sessionTimeout;
	}
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
    
}
