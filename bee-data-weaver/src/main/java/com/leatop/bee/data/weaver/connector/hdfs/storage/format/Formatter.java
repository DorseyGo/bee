/**
 * File: Formatter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月4日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format;

/**
 * @author Dorsey
 *
 */
public interface Formatter<CONFIG, T> {

	RecordWriterSupplier<CONFIG> getRecordWriterSupplier();

	SchemaFileReader<CONFIG, T> getSchemaFileReader();
}
