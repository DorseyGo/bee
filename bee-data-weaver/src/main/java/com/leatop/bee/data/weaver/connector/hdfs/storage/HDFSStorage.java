/**
 * File: HDFSStorage.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月3日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.storage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.avro.file.SeekableInput;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.connect.errors.ConnectException;

import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;

/**
 * An implementation of {@link Storage}, with HDFS as its underlying storage.
 * 
 * @author Dorsey
 *
 */
public class HDFSStorage implements Storage<HDFSSinkConnectorConfig, List<FileStatus>> {

	private final FileSystem fs;
	private final String url;
	private final HDFSSinkConnectorConfig config;

	/**
	 * Constructors, with configuration and URL specified.
	 * 
	 * @throws IOException
	 */
	public HDFSStorage(final HDFSSinkConnectorConfig config, final String url) throws IOException {
		this(FileSystem.newInstance(URI.create(url), config.getHadoopConfiguration()), url, config);
	}

	public HDFSStorage(final FileSystem fs, final String url,
			final HDFSSinkConnectorConfig config) {
		this.fs = fs;
		this.url = url;
		this.config = config;
	}

	@Override
	public void close() {
		if (fs != null) {
			try {
				fs.close();
			} catch (IOException e) {
				throw new ConnectException(e);
			}
		}
	}

	@Override
	public boolean exists(final String path) {
		try {
			return fs.exists(new Path(path));
		} catch (IOException e) {
			throw new ConnectException(e);
		}
	}

	@Override
	public boolean create(final String path) {
		try {
			return fs.mkdirs(new Path(path));
		} catch (IOException e) {
			throw new ConnectException(e);
		}
	}

	@Override
	public OutputStream create(final String path, final HDFSSinkConnectorConfig config,
			final boolean overwrite) {
		try {
			Path pt = new Path(path);
			return pt.getFileSystem(config.getHadoopConfiguration()).create(pt);
		} catch (IOException e) {
			throw new ConnectException(e);
		}
	}

	@Override
	public SeekableInput open(final String path, final HDFSSinkConnectorConfig config) {
		try {
			return new FsInput(new Path(path), config.getHadoopConfiguration());
		} catch (IOException e) {
			throw new ConnectException(e);
		}
	}

	@Override
	public OutputStream append(final String path) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean delete(final String path) {
		try {
			return fs.delete(new Path(path), true);
		} catch (IOException e) {
			throw new ConnectException(e);
		}
	}

	@Override
	public List<FileStatus> list(final String path) {
		try {
			return Arrays.asList(fs.listStatus(new Path(path)));
		} catch (IOException e) {
			throw new ConnectException(e);
		}
	}

	@Override
	public String url() {
		return url;
	}

	@Override
	public HDFSSinkConnectorConfig getConfig() {
		return config;
	}

	/**
	 * @param tmpFile
	 * @param file2BeCommit
	 */
	public void commit(final String tmpFile, final String file2BeCommit) {
		if (tmpFile.equals(file2BeCommit)) {
			return;
		}

		try {
			Path sourcePath = new Path(tmpFile);
			Path destPath = new Path(file2BeCommit);

			if (fs.exists(sourcePath)) {
				fs.rename(sourcePath, destPath);
			}
		} catch (IOException e) {
			throw new ConnectException(e);
		}
	}

}
