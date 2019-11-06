/**
 * File: DateToStringFieldTransformer.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月13日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source.processor;

import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.kafka.common.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceTaskConfig;

/**
 * An implementation of {@link FieldTransformer}, which process the date field to
 * string.
 * 
 * @author Dorsey
 *
 */
public class DateToStringFieldTransformer extends FieldTransformer {
	private static final Logger LOG = LoggerFactory.getLogger(DateToStringFieldTransformer.class);
	private static final String PATTERN_SUFFIX = ".pattern";
	private static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:00:00";
	private String pattern;
	private Object srcFieldValue;

	public DateToStringFieldTransformer(final String srcFieldName, final String derivedFieldName) {
		super(srcFieldName, derivedFieldName);
	}

	@Override
	public Object process() throws SQLException {
		Objects.requireNonNull(this.srcFieldValue);
		LOG.info("Format via using pattern: {}", pattern);

		final Date fieldValue = (Date) srcFieldValue;
		final DateFormat dateFormat = new SimpleDateFormat(pattern);

		return dateFormat.format(fieldValue);
	}

	@Override
	public int getSqlType() {
		return Types.VARCHAR;
	}

	@Override
	public FieldTransformer setSourceFieldValue(final Object srcFieldValue) {
		this.srcFieldValue = srcFieldValue;

		return this;
	}

	@Override
	public void configure(final JDBCSourceTaskConfig config, final Map<String, String> originals)
			throws ConfigException {
		String format = config
				.getString(JDBCSourceTaskConfig.TABLE_FROM_TABLE_SPLITTER_FORMAT_CONFIG);
		final String patternFormat = (format + PATTERN_SUFFIX);
		final String key = String.format(patternFormat, derivedFieldName, srcFieldName);
		this.pattern = ((pattern = originals.get(key)) == null) ? DEFAULT_DATE_FORMAT_PATTERN
				: pattern;
	}

}
