/**
 * File: JDBCSinkConnector.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.sink.JDBCSinkConfig;
import com.leatop.bee.data.weaver.connector.jdbc.sink.JDBCSinkTask;
import com.leatop.bee.data.weaver.connector.utils.VersionUtils;

/**
 * An implementation of {@link SinkConnector}, which provides service to write
 * data from kafka to RDBMS.
 * 
 * @author Dorsey
 *
 */
public class JDBCSinkConnector extends SinkConnector {

	private Map<String, String> configProps;
	private static final Logger log = LoggerFactory.getLogger(JDBCSinkConnector.class);

	@Override
	public ConfigDef config() {
		return JDBCSinkConfig.CONFIG_DEF;
	}

	@Override
	public void start(final Map<String, String> configs) {
		this.configProps = configs;
	}

	@Override
	public void stop() {
		// no-op
	}

	@Override
	public Class<? extends Task> taskClass() {
		return JDBCSinkTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(final int maxTasks) {
		log.info("Setting task configuration for {} workers", maxTasks);
		final List<Map<String, String>> configs = new ArrayList<>(maxTasks);
		for (int i = 0; i < maxTasks; ++i) {
			configs.add(configProps);
		}

		return configs;
	}

	@Override
	public String version() {
		return VersionUtils.getVersion();
	}

}
