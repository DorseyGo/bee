/**
 * File: TableNameGenerator.java
 * Author: DORSEy Q F TANG
 * Created: 2019年7月9日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.generator;

import org.apache.kafka.connect.sink.SinkRecord;

import com.leatop.bee.data.weaver.connector.jdbc.sink.JDBCSinkConfig;

/**
 * Abstraction of generating the table name.
 * 
 * @author DORSEy
 *
 */
public abstract class TableNameGenerator {
	protected final JDBCSinkConfig config;

	/**
	 * Default constructor of {@link TableNameGenerator}.
	 * 
	 * @param config the configuration.
	 */
	protected TableNameGenerator(final JDBCSinkConfig config) {
		this.config = config;
	}

	/**
	 * Generate the table name accordingly. The table name generated according
	 * to its specific subclasses.
	 * 
	 * @return
	 */
	public abstract String genTableName(final SinkRecord record);
}
