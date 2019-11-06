/**
 * File: FieldTransformer.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月13日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source.processor;

import java.sql.SQLException;
import java.util.Map;

import org.apache.kafka.common.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceTaskConfig;

/**
 * @author DORSEy
 *
 */
public abstract class FieldTransformer {

	// ~~~ fields
	// ==============================================================================
	protected static final Logger LOG = LoggerFactory.getLogger(FieldTransformer.class);
	protected final String srcFieldName;
	protected final String derivedFieldName;

	/**
	 * Default constructor of {@link FieldTransformer}, with source field name and
	 * derived field name specified.
	 */
	protected FieldTransformer(final String srcFieldName, final String derivedFieldName) {
		this.srcFieldName = srcFieldName;
		this.derivedFieldName = derivedFieldName;
	}

	// ~~~ methods
	// ==============================================================================
	/**
	 * Proceed, and returns the value of destination field, which is derived
	 * from existing field.
	 * 
	 * @return
	 * @throws SQLException
	 *             any SQL exception detected.
	 */
	public abstract Object process() throws SQLException;

	public abstract int getSqlType();

	public abstract void configure(final JDBCSourceTaskConfig config,
			final Map<String, String> originals) throws ConfigException;

	public abstract FieldTransformer setSourceFieldValue(final Object srcFieldValue);

}
