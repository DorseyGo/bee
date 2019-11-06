/**
 * File: DefaultTableNameGenerator.java
 * Author: DORSEy Q F TANG
 * Created: 2019年7月9日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.generator;

import org.apache.kafka.connect.sink.SinkRecord;

import com.leatop.bee.data.weaver.connector.jdbc.sink.JDBCSinkConfig;

/**
 * A default implementation of {@link TableNameGenerator}.
 * 
 * @author DORSEy
 *
 */
public class DefaultTableNameGenerator extends TableNameGenerator {
	
	/**
	 * The format pattern.
	 */
	private static final String TABLE_NAME_FORMAT_PATTERN = "${topic}";

	/**
	 * Default constructor of {@link DefaultTableNameGenerator}.
	 * 
	 * @param config
	 */
	public DefaultTableNameGenerator(final JDBCSinkConfig config) {
		super(config);
	}

	@Override
	public String genTableName(final SinkRecord record) {
		return config.tableNameFormat.replace(TABLE_NAME_FORMAT_PATTERN, record.topic());
	}

}
