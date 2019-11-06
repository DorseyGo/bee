/**
 * File: TimeZoneValidator.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.utils;

import java.util.Arrays;
import java.util.TimeZone;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

/**
 * @author Dorsey
 *
 */
public class TimeZoneValidator implements ConfigDef.Validator {
	public static final TimeZoneValidator INSTANCE = new TimeZoneValidator();
	
	@Override
	public void ensureValid(final String name, final Object value) {
		if (value != null) {
			if (!Arrays.asList(TimeZone.getAvailableIDs()).contains(value.toString())) {
				throw new ConfigException(name, value, "Invalid time zone identifier");
			}
		}
	}

}
