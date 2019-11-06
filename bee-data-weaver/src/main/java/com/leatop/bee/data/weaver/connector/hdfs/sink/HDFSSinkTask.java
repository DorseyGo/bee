/**
 * File: HDFSSink.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月3日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.sink;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.hdfs.Version;
import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;

/**
 * An implementation of {@link SinkTask}, which is used to sink data to
 * <tt>HDFS</tt>.
 * 
 * @author Dorsey
 *
 */
public class HDFSSinkTask extends SinkTask {

	private static final Logger LOG = LoggerFactory.getLogger(HDFSSinkTask.class);
	private HDFSWriter writer;

	@Override
	public String version() {
		return Version.getVersion();
	}

	@Override
	public void start(final Map<String, String> props) {
		try {
			HDFSSinkConnectorConfig config = new HDFSSinkConnectorConfig(props);
			writer = new HDFSWriter(config, context);

			recover(context.assignment());
		} catch (ConfigException e) {
			throw new ConnectException("Failed to start HDFSSinkTask due to configuration error",
					e);
		} catch (ConnectException e) {
			LOG.info("Couldn't start HDFSSinkTask, shutting down the task...", e);
			if (writer != null) {
				writer.stop();
				writer.close();
			}
		}
	}

	@Override
	public void put(final Collection<SinkRecord> records) {
		if (records.isEmpty()) {
			// no records pulled from Kafka right now.
			return;
		}

		LOG.debug("Read {} records into HDFS", records.size());
		try {
			writer.write(records);
		} catch (ConnectException e) {
			throw e;
		}
	}

	@Override
	public void flush(final Map<TopicPartition, OffsetAndMetadata> offsets) {
		// no-op
	}

	@Override
	public void stop() {
		if (writer != null) {
			writer.stop();
		}
	}

	@Override
	public void close(final Collection<TopicPartition> partitions) {
		if (writer != null) {
			writer.close();
		}
	}

	private void recover(final Set<TopicPartition> assignment) {
		for (TopicPartition partition : assignment) {
			writer.recover(partition);
		}
	}

	@Override
	public void open(final Collection<TopicPartition> partitions) {
		writer.open(partitions);
	}

	@Override
	public Map<TopicPartition, OffsetAndMetadata> preCommit(
			final Map<TopicPartition, OffsetAndMetadata> currentOffsets) {
		final Map<TopicPartition, OffsetAndMetadata> result = new HashMap<>();
		for (Map.Entry<TopicPartition, Long> entry : writer.getCommittedOffsets().entrySet()) {
			LOG.debug("Last committed offset {} for topic partition {}", entry.getValue(),
					entry.getKey());

			result.put(entry.getKey(), new OffsetAndMetadata(entry.getValue()));
		}

		LOG.debug("current committed offsets: {}", result);
		return result;
	}
}
