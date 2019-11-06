/**
 * File: CommitFileFilter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.filter;

import java.util.regex.Matcher;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import com.leatop.bee.data.weaver.connector.hdfs.config.Constants;

/**
 * An implementation of {@link PathFilter}, aim at filtering the file committed
 * in HDFS.
 * 
 * @author Dorsey
 *
 */
public class CommitFileFilter implements PathFilter {

	@Override
	public boolean accept(final Path path) {
		final String filename = path.getName();
		Matcher matcher = Constants.COMMIT_FILENAME_PATTERN.matcher(filename);

		return matcher.matches();
	}
}
