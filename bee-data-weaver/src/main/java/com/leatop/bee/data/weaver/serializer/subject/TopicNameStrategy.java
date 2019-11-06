/**
 * File: TopicNameStrategy.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.serializer.subject;

import java.util.Map;

import org.apache.avro.Schema;

/**
 * An implementation of {@link SubjectNameStrategy}.
 * 
 * @author Dorsey
 *
 */
public class TopicNameStrategy implements SubjectNameStrategy<Schema> {

	// ~~~~~~ fields
	// ====================================================================
	private static final String KEY_SUFFIX = "-key";
	private static final String VALUE_SUFFIX = "-value";

	// ~~~~~~ methods
	// ====================================================================
	@Override
	public void configure(final Map<String, ?> configs) {
		// empty for configuration.
	}

	@Override
	public String subjectNameOf(final String topic, final boolean isKey, final Schema schema) {
		return (topic + (isKey ? KEY_SUFFIX : VALUE_SUFFIX));
	}

}
