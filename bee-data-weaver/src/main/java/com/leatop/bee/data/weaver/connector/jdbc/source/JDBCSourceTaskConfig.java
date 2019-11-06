/**
 * File: JDBCSourceTaskConfig.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月12日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

/**
 * Configuration for JDBC source task.
 * 
 * @description
 */
public class JDBCSourceTaskConfig extends JDBCSourceConnectorConfig {

	public static final String TABLES_CONFIG = "tables";
	private static final String TABLES_DEFAULT = "";
	private static final String TABLES_DOC = "List of tables for this task to watch for changes.";

	public static final String TABLE_NAME_AND_BATCH_NOS_CONFIG = "table.name.and.batch.num";
	private static final String TABLE_NAME_AND_BATCH_NOS_DEFAULT = "";
	private static final String TABLE_NAME_AND_BATCH_NOS_DOC = "List of table name and batch nos for this task to retrieve data.";

	static ConfigDef config = baseConfigDef()
			.define(TABLES_CONFIG, Type.LIST, TABLES_DEFAULT, Importance.HIGH, TABLES_DOC)
			.define(TABLE_NAME_AND_BATCH_NOS_CONFIG, Type.LIST, TABLE_NAME_AND_BATCH_NOS_DEFAULT,
					Importance.HIGH,
					TABLE_NAME_AND_BATCH_NOS_DOC);

	public JDBCSourceTaskConfig(final Map<String, String> props) {
		super(config, props);
	}
}
