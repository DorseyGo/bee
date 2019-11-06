/**
 * File: AvroConverterConfig.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月13日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.converter;

import java.util.HashMap;
import java.util.Map;

import com.leatop.bee.data.weaver.serializer.config.KafkaAvroSerDeConfig;

/**
 * @author Dorsey
 *
 */
public class AvroConverterConfig extends KafkaAvroSerDeConfig {

	public AvroConverterConfig(final Map<?, ?> originals) {
		super(basicConfig(), originals);
	}

	public static class Builder {

		private Map<String, Object> props = new HashMap<>();

		public Builder with(final String key, final Object value) {
			props.put(key, value);
			
			return this;
		}

		public AvroConverterConfig build() {
			return new AvroConverterConfig(props);
		}
	}
}
