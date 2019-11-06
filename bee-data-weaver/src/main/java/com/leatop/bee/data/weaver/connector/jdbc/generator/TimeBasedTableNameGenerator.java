/**
 * File: TimeBasedTableNameGenerator.java
 * Author: DORSEy Q F TANG
 * Created: 2019年7月9日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.generator;

import java.util.Date;

import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.common.utils.DateUtils;
import com.leatop.bee.data.weaver.connector.jdbc.sink.JDBCSinkConfig;

/**
 * An implementation of {@link TableNameGenerator}, which will generate the
 * table name according to the value of specific field.
 * 
 * @author DORSEy
 *
 */
public class TimeBasedTableNameGenerator extends TableNameGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(TimeBasedTableNameGenerator.class);
	public static final String LEFT_BRACE_DELIM = "{";
	private static final String RIGHT_BRACE_DELIM = "}";
	private final String timeBasedPattern;
	private final String pivotFieldName;
	private final String basedTableName;

	/**
	 * Constructor of {@link TimeBasedTableNameGenerator}, with configuration
	 * specified.
	 * 
	 * @param config
	 *            the configuration.
	 */
	public TimeBasedTableNameGenerator(final JDBCSinkConfig config) {
		super(config);
		final String tableNameFormat = config.tableNameFormat;
		final int leftBraceIndex = tableNameFormat.indexOf(LEFT_BRACE_DELIM);
		final int rightBraceIndex = tableNameFormat.lastIndexOf(RIGHT_BRACE_DELIM);
		if (leftBraceIndex < 0 || rightBraceIndex < 0 || (leftBraceIndex >= rightBraceIndex)) {
			throw new ConfigException("table.name.format MUST be TABLE_NAME_{#date_format} when "
					+ TimeBasedTableNameGenerator.class.getName() + " is used");
		}

		this.timeBasedPattern = tableNameFormat.substring(leftBraceIndex + 1, rightBraceIndex);
		this.basedTableName = tableNameFormat.substring(0, leftBraceIndex);
		this.pivotFieldName = config.pivotFieldName;
		if (pivotFieldName == null || pivotFieldName.trim().isEmpty()) {
			throw new ConfigException("pivot.field MUST be specified, if "
					+ TimeBasedTableNameGenerator.class.getName() + " is in usage");
		}
	}

	@Override
	public String genTableName(final SinkRecord record) {
		Struct valStruct = (Struct) record.value();
		Schema valSchema = record.valueSchema();
		if (valSchema.field(pivotFieldName) == null) {
			LOG.error(String.format("Field:[%s] defined by pivot.field not found in schema",
					pivotFieldName));

			throw new ConfigException(
					"Field:" + pivotFieldName + ", defined by pivot.field not found in schema");
		}

		final Date pivotFieldVal = (Date) valStruct.get(pivotFieldName);
		final String suffix = DateUtils.format(pivotFieldVal, timeBasedPattern);

		return (basedTableName + suffix);
	}

}
