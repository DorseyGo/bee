package com.leatop.bee.data.weaver.connector.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialects;

public class DialectRecommender implements ConfigDef.Recommender, ConfigDef.Validator {

	public static final DialectRecommender INSTANCE = new DialectRecommender();

	private static final List<Object> DIALECT_NAMES;

	static {
		DIALECT_NAMES = new ArrayList<>();
		DIALECT_NAMES.add("");
		DIALECT_NAMES.addAll(Dialects.registeredDialectNames());
	}

	@Override
	public List<Object> validValues(final String var1, final Map<String, Object> var2) {
		return DIALECT_NAMES;
	}

	@Override
	public boolean visible(final String var1, final Map<String, Object> var2) {
		return true;
	}

	@Override
	public void ensureValid(final String key, final Object value) {
		if (value != null && !DIALECT_NAMES.contains(value.toString())) {
			throw new ConfigException(key, value, "Invalid enumerator");
		}
	}

	@Override
	public String toString() {
		return DIALECT_NAMES.toString();
	}
}
