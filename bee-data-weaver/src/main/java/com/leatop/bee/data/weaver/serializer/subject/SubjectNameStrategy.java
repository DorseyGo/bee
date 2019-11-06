/**
 * File: SubjectNameStrategy.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.serializer.subject;

import org.apache.kafka.common.Configurable;

/**
 * @author Dorsey
 *
 */
public interface SubjectNameStrategy<T> extends Configurable {

	String subjectNameOf(final String topic, final boolean isKey, final T schema);
}
