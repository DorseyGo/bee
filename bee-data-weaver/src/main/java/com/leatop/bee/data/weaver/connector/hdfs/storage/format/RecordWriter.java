/**
 * File: RecordWriter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月4日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.format;

import java.io.Closeable;

import org.apache.kafka.connect.sink.SinkRecord;

/**
 * @author Dorsey
 *
 */
public interface RecordWriter extends Closeable {
	
	/**
	 * Write the given <code>record</code> to the underlying storage.
	 * 
	 * @param record
	 */
	void write(final SinkRecord record);
	
	/**
	 * Flush the writer's data to the under storage, maybe it will cause writer close.
	 */
	void commit();
	
	/**
	 * Close this writer.
	 */
	@Override
	void close();
}
