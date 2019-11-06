/**
 * File: EnumRecommender.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

/**
 * @author Dorsey
 *
 */
public class EnumRecommender implements ConfigDef.Validator, ConfigDef.Recommender {

	private final List<String> originals;
	private final Set<String> validates;

	/**
	 * @param originals
	 * @param validates
	 */
	public EnumRecommender(final List<String> originals, final Set<String> validates) {
		this.originals = originals;
		this.validates = validates;
	}

	@Override
	public List<Object> validValues(final String name, final Map<String, Object> parsedConfig) {
		return new ArrayList<>(originals);
	}

	@Override
	public boolean visible(final String name, final Map<String, Object> parsedConfig) {
		return true;
	}

	@Override
	public void ensureValid(final String name, final Object value) {
		if (value != null && validates.contains(Objects.toString(value)) == false) {
			throw new ConfigException(name, value, "Invalid enumerators");
		}
	}

	/**
	 * @param values
	 * @return
	 */
	@SafeVarargs
	public static <E> EnumRecommender in(final E... enumerators) {
		final List<String> originals = new ArrayList<>(enumerators.length);
		final Set<String> validates = new HashSet<>(enumerators.length * 2);
		
		if (enumerators.length > 0) {
			for (E enumerator : enumerators) {
				originals.add(enumerator.toString().toLowerCase());
				validates.add(enumerator.toString().toLowerCase(Locale.ROOT));
				validates.add(enumerator.toString().toUpperCase(Locale.ROOT));
			}
		}
		
		return new EnumRecommender(originals, validates);
	}

}
