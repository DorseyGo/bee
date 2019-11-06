/**
 * File: Storage.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月4日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage;

import java.io.Closeable;
import java.io.OutputStream;

import org.apache.avro.file.SeekableInput;

/**
 * @author Dorsey
 *
 */
public interface Storage<CONFIG, LIST> extends Closeable {

	boolean exists(final String path);

	boolean create(final String path);

	OutputStream create(final String path, final CONFIG config, final boolean override);

	SeekableInput open(final String path, final CONFIG config);

	OutputStream append(final String path);

	boolean delete(final String path);

	LIST list(final String path);

	@Override
	void close();

	String url();

	CONFIG getConfig();
}
