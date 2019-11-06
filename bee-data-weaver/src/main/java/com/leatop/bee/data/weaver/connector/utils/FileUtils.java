/**
 * File: FileUtils.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.common.TopicPartition;

import com.leatop.bee.common.utils.Objects;
import com.leatop.bee.data.weaver.connector.hdfs.config.Constants;
import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.filter.CommitFileFilter;
import com.leatop.bee.data.weaver.connector.hdfs.storage.Storage;

/**
 * @author Dorsey
 *
 */
public class FileUtils {

	public static String topicFolder(final String url, final String topicsFolder,
			final String topic) {
		return dirName(url, topicsFolder, topic);
	}

	public static String dirName(final String url, final String topicsFolder, final String dir) {
		return url + Constants.FORWARD_SLASH_DELIM + topicsFolder + Constants.FORWARD_SLASH_DELIM
				+ dir;
	}

	public static FileStatus maxOffset(
			final Storage<HDFSSinkConnectorConfig, List<FileStatus>> storage, final Path path,
			final CommitFileFilter filter) {
		Map<Long, FileStatus> offsetWithFileStatus = new HashMap<>();
		maxOffset0(storage, path, filter, offsetWithFileStatus);

		List<Long> keys = new ArrayList<>(offsetWithFileStatus.keySet());
		if (keys.isEmpty()) {
			return null;
		}

		Collections.sort(keys);
		Long maxOffset = keys.get(keys.size() - 1);

		return offsetWithFileStatus.get(maxOffset);
	}

	private static void maxOffset0(final Storage<HDFSSinkConnectorConfig, List<FileStatus>> storage,
			final Path path, final CommitFileFilter filter,
			final Map<Long, FileStatus> offsetWithFileStatus) {
		if (!storage.exists(path.toString())) {
			return;
		}

		List<FileStatus> fileStatuses = storage.list(path.toString());
		for (FileStatus fileStatus : fileStatuses) {
			final Path childPath = fileStatus.getPath();
			if (fileStatus.isFile()) {
				if (filter.accept(childPath)) {
					long offset = extractOffset(childPath.getName());
					offsetWithFileStatus.put(offset, fileStatus);
				}

				continue;
			}

			maxOffset0(storage, fileStatus.getPath(), filter, offsetWithFileStatus);
		}
	}

	public static long extractOffset(final String filename) {
		final Matcher matcher = Constants.COMMIT_FILENAME_PATTERN.matcher(filename);
		Objects.assertionTrue(matcher.matches(), "match expected here");

		return Long.valueOf(matcher.group(Constants.PATTERN_END_OFFSET_GROUP));
	}

	public static String tmpFile(final String url, final String topicsFolder, final String folder,
			final String extension) {
		final String uuid = UUID.randomUUID().toString();
		String name = uuid + Constants.UNDER_SCORE_DELIM + Constants.TMP_SYMBOL + extension;

		return filename(url, topicsFolder, folder, name);
	}

	public static String filename(final String url, final String topicsFolder, final String folder,
			final String name) {
		return dirName(url, topicsFolder, folder) + Constants.FORWARD_SLASH_DELIM + name;
	}

	/**
	 * Returns the committed file name, which will contains the uploaded
	 * records.
	 * 
	 */
	public static String genCommitFileName(final String url, final String topicsFolder, final String folder,
			final TopicPartition topicPartition, final Long startOffset, final long endOffset,
			final String extension,
			final String zeroPadOffseter) {
		final String topic = topicPartition.topic();
		final int partition = topicPartition.partition();
		final StringBuilder builder = new StringBuilder();
		builder.append(topic);
		builder.append(Constants.COMMITTED_FILENAME_SEPARATOR);
		builder.append(partition);
		builder.append(Constants.COMMITTED_FILENAME_SEPARATOR);
		builder.append(String.format(zeroPadOffseter, startOffset));
		builder.append(Constants.COMMITTED_FILENAME_SEPARATOR);
		builder.append(String.format(zeroPadOffseter, endOffset));
		builder.append(extension);

		final String committedFilename = builder.toString();
		return filename(url, topicsFolder, folder, committedFilename);
	}
}
