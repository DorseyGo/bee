/**
 * File: HDFSSinkConnector.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月3日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.sink.HDFSSinkTask;

/**
 * An implementation of {@link SinkConnector}, which is used to copy data from
 * <tt>kafka</tt> to <tt>HDFS</tt>.
 * 
 * @author Dorsey
 *
 */
public class HDFSSinkConnector extends SinkConnector {

	private static final Logger LOG = LoggerFactory.getLogger(HDFSSinkConnector.class);
	private Map<String, String> configProps;
	@SuppressWarnings("unused")
	private HDFSSinkConnectorConfig config;

	@Override
	public String version() {
		return Version.getVersion();
	}

	@Override
	public void start(final Map<String, String> props) {
		try {
			this.configProps = props;
			config = new HDFSSinkConnectorConfig(this.configProps);
		} catch (ConfigException e) {
			throw new ConnectException("Failed to config the HDFSSinkConnector", e);
		}
	}

	@Override
	public Class<? extends Task> taskClass() {
		return HDFSSinkTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(final int maxTasks) {
		if (maxTasks < 0) {
			throw new IllegalArgumentException(
					String.format("num.tasks[%d] MUST greater than 0", maxTasks));
		}

		LOG.info("Setting task configuration for {} workers", maxTasks);
		List<Map<String, String>> configs = new ArrayList<>(maxTasks);
		for (int index = 0; index < maxTasks; index++) {
			configs.add(configProps);
		}

		return configs;
	}

	@Override
	public void stop() {
		// no-op
	}

	@Override
	public ConfigDef config() {
		return HDFSSinkConnectorConfig.getConfig();
	}

}
