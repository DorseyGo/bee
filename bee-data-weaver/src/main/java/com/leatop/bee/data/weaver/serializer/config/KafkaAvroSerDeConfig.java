/**
 * File: KafkaAvroSerDeConfig.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.serializer.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import com.leatop.bee.data.weaver.auth.BasicAuthenticationProvider;
import com.leatop.bee.data.weaver.serializer.subject.SubjectNameStrategy;
import com.leatop.bee.data.weaver.serializer.subject.TopicNameStrategy;

/**
 * @author Dorsey
 *
 */
public class KafkaAvroSerDeConfig extends AbstractConfig {

	public static final String REQUEST_HEADER_PREFIX = "request.header.";

	public static final String SCHEMA_REGISTRY_URL_CONFIG = "schema.registry.url";
	public static final String SCHEMA_REGISTRY_URL_DOC = "URL for schema registry instances that can be used to register "
			+ "or look up schemas.";

	public static final String MAX_SCHEMAS_PER_SUBJECT_CONFIG = "max.schemas.per.subject";
	public static final int MAX_SCHEMAS_PER_SUBJECT_DEFAULT = 100;
	public static final String MAX_SCHEMAS_PER_SUBJECT_DOC = "Maximum number of schemas to create or cache locally.";

	public static final String AUTO_REGISTER_SCHEMAS = "auto.register.schemas";
	public static final boolean AUTO_REGISTER_SCHEMAS_DEFAULT = true;
	public static final String AUTO_REGISTER_SCHEMAS_DOC = "Specify if the Serializer should attempt to register the Schema with Schema Registry";

	public static final String KEY_SUBJECT_NAME_STRATEGY = "key.subject.name.strategy";
	public static final String KEY_SUBJECT_NAME_STRATEGY_DEFAULT = TopicNameStrategy.class
			.getName();
	public static final String KEY_SUBJECT_NAME_STRATEGY_DOC = "Determines how to construct the subject name under which the key schema is registered "
			+ "with the schema registry. By default, <topic>-key is used as subject.";

	public static final String VALUE_SUBJECT_NAME_STRATEGY = "value.subject.name.strategy";
	public static final String VALUE_SUBJECT_NAME_STRATEGY_DEFAULT = TopicNameStrategy.class
			.getName();
	public static final String VALUE_SUBJECT_NAME_STRATEGY_DOC = "Determines how to construct the subject name under which the value schema is registered "
			+ "with the schema registry. By default, <topic>-value is used as subject.";

	public static final String BASIC_AUTH_PROVIDER_SOURCE = BasicAuthenticationProvider.Constants.BASIC_AUTH_PROVIDER_SOURCE;
	public static final String VALUE_BASIC_AUTH_PROVIDER_SOURCE_DEFAULT = "URL";
	public static final String VALUE_BASIC_AUTH_PROVIDER_SOURCE_DEFAULT_DOC = "Specify how to pick the credentials for Basic Auth header. The supported values are URL, USER_INFO and SASL_INHERIT";

	public static final String USER_INFO_CONFIG = BasicAuthenticationProvider.Constants.USER_INFO_CONFIG;
	public static final String USER_INFO_DEFAULT = "";
	public static final String SCHEMA_REGISTRY_USER_INFO_DOC = "Specify the user info for Basic Auth in the form of {username}:{password}";

	/**
	 * @param definition
	 * @param originals
	 */
	public KafkaAvroSerDeConfig(final ConfigDef definition, final Map<?, ?> originals) {
		super(definition, originals);
	}

	public static ConfigDef basicConfig() {
		return new ConfigDef()
				.define(SCHEMA_REGISTRY_URL_CONFIG, Type.LIST, Importance.HIGH,
						SCHEMA_REGISTRY_URL_DOC)
				.define(MAX_SCHEMAS_PER_SUBJECT_CONFIG, Type.INT, MAX_SCHEMAS_PER_SUBJECT_DEFAULT,
						Importance.LOW, MAX_SCHEMAS_PER_SUBJECT_DOC)
				.define(AUTO_REGISTER_SCHEMAS, Type.BOOLEAN, AUTO_REGISTER_SCHEMAS_DEFAULT,
						Importance.MEDIUM, AUTO_REGISTER_SCHEMAS_DOC)
				.define(KEY_SUBJECT_NAME_STRATEGY, Type.CLASS, KEY_SUBJECT_NAME_STRATEGY_DEFAULT,
						Importance.MEDIUM, KEY_SUBJECT_NAME_STRATEGY_DOC)
				.define(VALUE_SUBJECT_NAME_STRATEGY, Type.CLASS,
						VALUE_SUBJECT_NAME_STRATEGY_DEFAULT, Importance.MEDIUM,
						VALUE_SUBJECT_NAME_STRATEGY_DOC)
				.define(BASIC_AUTH_PROVIDER_SOURCE, Type.STRING,
						VALUE_BASIC_AUTH_PROVIDER_SOURCE_DEFAULT, Importance.MEDIUM,
						VALUE_BASIC_AUTH_PROVIDER_SOURCE_DEFAULT_DOC)
				.define(USER_INFO_CONFIG, Type.PASSWORD, USER_INFO_DEFAULT, Importance.MEDIUM,
						SCHEMA_REGISTRY_USER_INFO_DOC);
	}

	/**
	 * @return the autoRegisterSchemasDefault
	 */
	public boolean isAutoRegisterSchemas() {
		return this.getBoolean(AUTO_REGISTER_SCHEMAS);
	}

	/**
	 * 
	 */
	public String getSchemaRegistryUrl() {
		List<String> urls = this.getList(SCHEMA_REGISTRY_URL_CONFIG);
		// here, only one expect
		return (urls != null && !urls.isEmpty()) ? urls.get(0) : null;
	}

	/**
	 * @return
	 */
	public int getMaxSchemaPerSubject() {
		return this.getInt(MAX_SCHEMAS_PER_SUBJECT_CONFIG);
	}

	/**
	 * @return
	 */
	public Map<String, String> getRequestHeaders() {
		Iterator<Entry<String, Object>> iter = originalsWithPrefix(REQUEST_HEADER_PREFIX).entrySet()
				.iterator();
		Map<String, String> headers = new HashMap<String, String>();
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			headers.put(entry.getKey(), Objects.toString(entry.getValue()));
		}

		return headers;
	}

	/**
	 * @return
	 */
	public Object keySubjectNameStrategy() {
		return subjectStrategyOf(KEY_SUBJECT_NAME_STRATEGY);
	}

	/**
	 * @return
	 */
	public Object valueSubjectNameStrategy() {
		return subjectStrategyOf(VALUE_SUBJECT_NAME_STRATEGY);
	}

	@SuppressWarnings("rawtypes")
	private SubjectNameStrategy subjectStrategyOf(final String name) {
		return this.getConfiguredInstance(name, SubjectNameStrategy.class);
	}
}
