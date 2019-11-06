/**
 * File: CommitFileTopicPartitionFilter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.filter;

import java.util.regex.Matcher;

import org.apache.hadoop.fs.Path;
import org.apache.kafka.common.TopicPartition;

import com.leatop.bee.common.utils.Objects;
import com.leatop.bee.data.weaver.connector.hdfs.config.Constants;

/**
 * @author Dorsey
 *
 */
public class CommitFileTopicPartitionFilter extends CommitFileFilter {

	private final TopicPartition topicPartition;

	/**
	 * Constructor of {@link CommitFileTopicPartitionFilter}.
	 * 
	 * @param topicPartition
	 */
	public CommitFileTopicPartitionFilter(final TopicPartition topicPartition) {
		this.topicPartition = topicPartition;
	}

	@Override
	public boolean accept(final Path path) {
		final String filename = path.getName();
		final Matcher matcher = Constants.COMMIT_FILENAME_PATTERN.matcher(filename);
		Objects.assertionTrue(matcher.matches(), "Match expected");

		final String topic = matcher.group(Constants.PATTERN_TOPIC_GROUP);
		final int partition = Integer.valueOf(matcher.group(Constants.PATTERN_PARTITION_GROUP));

		return java.util.Objects.equals(topic, topicPartition.topic())
				&& (partition == topicPartition.partition());
	}
}
