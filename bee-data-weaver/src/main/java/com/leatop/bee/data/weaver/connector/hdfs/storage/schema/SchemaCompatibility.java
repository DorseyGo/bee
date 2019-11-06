/**
 * File: SchemaCompatibility.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月4日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage.schema;

import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.sink.SinkRecord;

/**
 * @author Dorsey
 *
 */
public interface SchemaCompatibility {

	boolean needChangeSchema(final ConnectRecord record, final Schema curKeySchema,
			final Schema curValSchema);

	SinkRecord project(final SinkRecord record, final Schema curKeySchema,
			final Schema curValSchema);
}
