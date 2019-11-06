/**
 * File: RecordWriterSupplier.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月4日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format;

/**
 * @author Dorsey
 *
 */
public interface RecordWriterSupplier<CONFIG> {

	String getExtension();

	RecordWriter getWriter(final CONFIG config, final String filename);
}
