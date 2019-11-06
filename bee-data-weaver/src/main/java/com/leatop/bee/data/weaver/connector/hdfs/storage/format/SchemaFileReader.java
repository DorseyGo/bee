/**
 * File: SchemaFileReader.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月4日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format;

import java.io.Closeable;
import java.util.Iterator;

import org.apache.kafka.connect.data.Schema;

/**
 * @author Dorsey
 *
 */
public interface SchemaFileReader<CONFIG, T> extends Iterator<Object>, Iterable<Object>, Closeable {

	/**
	 * Get the schema for this object at the given <code>path</code>.
	 * 
	 */
	Schema getSchema(final CONFIG config, final T path);
}
