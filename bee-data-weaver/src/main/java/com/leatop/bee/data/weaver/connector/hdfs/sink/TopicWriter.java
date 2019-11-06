/**
 * File: TopicWriter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月3日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.sink;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.common.utils.Time;
import com.leatop.bee.data.weaver.connector.hdfs.config.Constants;
import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.config.StorageSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.filter.CommitFileFilter;
import com.leatop.bee.data.weaver.connector.hdfs.filter.CommitFileTopicPartitionFilter;
import com.leatop.bee.data.weaver.connector.hdfs.partitioner.Partitioner;
import com.leatop.bee.data.weaver.connector.hdfs.storage.HDFSStorage;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.RecordWriter;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.RecordWriterSupplier;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.SchemaFileReader;
import com.leatop.bee.data.weaver.connector.hdfs.storage.schema.StorageSchemaCompatibility;
import com.leatop.bee.data.weaver.connector.utils.FileUtils;

/**
 * @author Dorsey
 *
 */
public class TopicWriter {

	// ~~~~~~~~~ constants
	// =========================================================
	private static final Time SYS_TIME = new Time.SysTime();
	private static final Logger LOG = LoggerFactory.getLogger(TopicWriter.class);

	// ~~~~~~~~~ fields
	// =========================================================
	private final HDFSStorage storage;
	private final Map<String, String> tmpFiles;
	private final Map<String, RecordWriter> writers;
	private final TopicPartition topicPartition;
	private final Partitioner partitioner;
	private final Time time;
	private final String url;
	private final String topicsFolder;
	private final Queue<SinkRecord> recordBuffer;
	private boolean recovered;
	private State state;
	private final SinkTaskContext context;
	/** record the number of record to be written */
	private int counter;
	private Schema curSchema;
	private long offset;
	/* record the time when it fails */
	private long timeWhenFailed;
	private final long timeoutInMs;
	private final SchemaFileReader<HDFSSinkConnectorConfig, Path> schemaFileReader;

	/**
	 * The file extension.
	 */
	private final String extension;

	/* batch size */
	private final int batchSize;
	private final Map<String, Long> startOffsets;
	private final Map<String, Long> offsets;
	private final RecordWriterSupplier<HDFSSinkConnectorConfig> recordWriterSupplier;
	private final HDFSSinkConnectorConfig config;
	private final StorageSchemaCompatibility compability;
	private final String zeroPadOffseter;

	public TopicWriter(final TopicPartition topicPartition, final HDFSStorage storage,
			final RecordWriterSupplier<HDFSSinkConnectorConfig> recordWriterSupplier,
			final Partitioner partitioner, final HDFSSinkConnectorConfig config,
			final SinkTaskContext context,
			final SchemaFileReader<HDFSSinkConnectorConfig, Path> schemaFileReader) {
		this.topicPartition = topicPartition;
		this.storage = storage;
		this.partitioner = partitioner;
		this.context = context;
		this.recordWriterSupplier = recordWriterSupplier;
		this.time = SYS_TIME;

		this.url = storage.url();
		this.config = storage.getConfig();
		this.topicsFolder = config.getString(StorageSinkConnectorConfig.TOPICS_FOLDER_CONFIG);
		batchSize = config.getInt(HDFSSinkConnectorConfig.BATCH_SIZE_CONFIG);
		this.compability = StorageSchemaCompatibility.getCompability(
				config.getString(StorageSinkConnectorConfig.SCHEMA_COMPABILITY_CONFIG));
		this.offset = -1L;
		this.timeWhenFailed = -1L;
		this.timeoutInMs = config.getLong(HDFSSinkConnectorConfig.TIMEOUT_IN_MS_CONFIG);

		this.zeroPadOffseter = "%0"
				+ config.getInt(HDFSSinkConnectorConfig.FILENAME_OFFSET_ZERO_PAD_WIDTH_CONFIG)
				+ "d";
		this.extension = recordWriterSupplier.getExtension();

		recordBuffer = new LinkedList<>();
		writers = new HashMap<>();
		tmpFiles = new HashMap<>();
		offsets = new HashMap<>();
		startOffsets = new HashMap<>();

		state = State.RECOVER_STARTED;
		this.schemaFileReader = schemaFileReader;
	}

	public void buffer(final SinkRecord record) {
		recordBuffer.add(record);
	}

	public void write() {
		long now = time.milliseconds();
		if (timeWhenFailed > 0 && (now - timeWhenFailed) < timeoutInMs) {
			return;
		}

		SinkRecord curRecord = null;
		if (state.compareTo(State.WRITE_STARTED) < 0) {
			// guarantee that the storage is prepared for writing.
			boolean success = recover();
			if (success == false) {
				return;
			}
		}

		while (!recordBuffer.isEmpty()) {
			try {
				switch (state) {
				case WRITE_STARTED:
					pause();
					nextState();

				case WRITE_PARTITION_HALT:
					fixCurrentSchema();

					SinkRecord record = recordBuffer.peek();
					curRecord = record;
					Schema valueSchema = record.valueSchema();
					if ((counter <= 0 && curSchema == null && valueSchema != null)
							|| compability.needChangeSchema(record, null, curSchema)) {
						curSchema = valueSchema;

						if (counter <= 0) {
							break;
						}

						nextState();
					} else {
						if (!shouldRotate()) {
							SinkRecord projectedRecord = compability.project(record, null,
									curSchema);
							write(projectedRecord);
							recordBuffer.poll();
							break;
						}

						nextState();
					}

				case TMP_FILE_CLOSED:
					closeTmpFile();
					commitFile();
					nextState();
				case FILE_COMMITTED:
					setState(State.WRITE_PARTITION_HALT);
					break;
				default:
					LOG.error("invalid state: {} to write record: {} for topic partition: {}",
							state, curRecord, topicPartition);
				}
			} catch (ConnectException e) {
				// retry if connect exception detected.
				timeWhenFailed = time.milliseconds();
				setRetryTimeOut(timeoutInMs);
				break;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (recordBuffer.isEmpty()) {
			if (counter > 0) {
				try {
					closeTmpFile();
					commitFile();
				} catch (ConnectException e) {
					timeWhenFailed = time.milliseconds();
					setRetryTimeOut(timeoutInMs);
				}
			}

			resume();
			setState(State.WRITE_STARTED);
		}
	}

	private boolean shouldRotate() {
		return (counter >= batchSize);
	}

	private void resume() {
		context.resume(topicPartition);
	}

	/**
	 * @param timeoutInMs
	 */
	private void setRetryTimeOut(final long timeoutInMs) {
		context.timeout(timeoutInMs);
	}

	private void commitFile() {
		LOG.debug("Committing file...");
		for (String encodedPartition : tmpFiles.keySet()) {
			commitFile(encodedPartition);
		}
	}

	/**
	 * @param encodedPartition
	 */
	private void commitFile(final String encodedPartition) {
		Long startOffset = startOffsets.get(encodedPartition);
		if (startOffset == null) {
			return;
		}

		long endOffset = offsets.get(encodedPartition);
		final String tmpFile = tmpFiles.get(encodedPartition);
		final String folder = getDir(encodedPartition);
		String file2BeCommit = FileUtils.genCommitFileName(url, topicsFolder, folder,
				topicPartition, startOffset, endOffset, extension, zeroPadOffseter);

		final String dirName = FileUtils.dirName(url, topicsFolder, folder);
		if (!storage.exists(dirName)) {
			storage.create(dirName);
		}

		storage.commit(tmpFile, file2BeCommit);

		// state modify
		startOffsets.remove(encodedPartition);
		offsets.remove(encodedPartition);
		offset += counter;
		counter = 0;
		LOG.info("Commit {} for {}", file2BeCommit, topicPartition);
	}

	private void closeTmpFile() {
		Iterator<String> iter = tmpFiles.keySet().iterator();
		while (iter.hasNext()) {
			String encodedPartition = iter.next();

			closeTmpFile(encodedPartition);
		}
	}

	private void closeTmpFile(final String encodedPartition) {
		if (writers.containsKey(encodedPartition)) {
			RecordWriter writer = writers.get(encodedPartition);
			writer.close();
			writers.remove(encodedPartition);
		}
	}

	private void setState(final State state) {
		this.state = state;
	}

	/**
	 * Write the record to the underlying storage.
	 * 
	 * @param record
	 */
	private void write(final SinkRecord record) {
		if (offset == -1L) {
			offset = record.kafkaOffset();
		}

		LOG.debug("Write record: {} to offset: {}", record, offset);
		final String encodedPartition = partitioner.encodePartition(record);
		RecordWriter writer = getWriter(record, encodedPartition);
		writer.write(record);

		if (!startOffsets.containsKey(encodedPartition)) {
			startOffsets.put(encodedPartition, record.kafkaOffset());
		}

		offsets.put(encodedPartition, record.kafkaOffset());
		counter++;
	}

	private RecordWriter getWriter(final SinkRecord record, final String encodedPartition)
			throws ConnectException {
		RecordWriter writer = writers.get(encodedPartition);
		if (writer != null) {
			// already exists, return it immediately.
			return writer;
		}

		String tmpFile = getTmpFile(encodedPartition);
		if (recordWriterSupplier == null) {
			throw new ConnectException("No record writer supplier implied");
		}

		writer = recordWriterSupplier.getWriter(config, tmpFile);
		// put it into cache, for later usage.
		writers.put(encodedPartition, writer);

		return writer;
	}

	private String getTmpFile(final String encodedPartition) {
		String tmpFile = tmpFiles.get(encodedPartition);
		if (tmpFile == null) {
			// not exists, then create and buffer it.
			String folder = Constants.TEMP_FILE_FOLDER + getDir(encodedPartition);
			tmpFile = FileUtils.tmpFile(url, topicsFolder, folder, extension);
			tmpFiles.put(encodedPartition, tmpFile);
		}

		return tmpFile;
	}

	private String getDir(final String encodedPartition) {
		return partitioner.genPartitionedPath(topicPartition.topic(), encodedPartition);
	}

	/**
	 * Try to fix the current schema if it is absent.
	 */
	private void fixCurrentSchema() {
		if (curSchema == null) {
			if (compability != StorageSchemaCompatibility.NONE && offset != -1L) {
				String topicFolder = FileUtils.topicFolder(url, topicsFolder,
						topicPartition.topic());
				CommitFileFilter filter = new CommitFileTopicPartitionFilter(topicPartition);

				FileStatus fileStatusWithMaxOffset = FileUtils.maxOffset(storage,
						new Path(topicFolder), filter);
				if (fileStatusWithMaxOffset != null) {
					curSchema = schemaFileReader.getSchema(config,
							fileStatusWithMaxOffset.getPath());
				}
			}
		}
	}

	private void nextState() {
		state = state.next();
	}

	private void pause() {
		context.pause(topicPartition);
	}

	public void close() {

	}

	public boolean recover() {
		try {
			switch (state) {
			case RECOVER_STARTED:
				pause();
				nextState();
			case RECOVER_PARTITION_HALT:
				resetOffset();
				nextState();
			case OFFSET_RESET:
				resume();
				nextState();
				LOG.info("Recovery on partition: {} finished", topicPartition);
				break;
			default:
				break;
			}
		} catch (ConnectException e) {
			LOG.error("Failed to proceed recovery on partition: {}", topicPartition, e);
			setRetryTimeOut(timeoutInMs);
			return false;
		}

		return true;
	}

	/**
	 * Reset the offset.
	 */
	private void resetOffset() {
		if (!recovered) {
			readOffset();

			if (offset > 0) {
				LOG.debug("Reset topic partition: {} with offset:{}", topicPartition, offset);
				context.offset(topicPartition, offset);
			} else {
				LOG.debug("Recovery proceed on topic partition: {} based on custom config",
						topicPartition);
			}

			recovered = true;
		}
	}

	private void readOffset() {
		final String path = FileUtils.dirName(url, topicsFolder, topicPartition.topic());
		FileStatus fileStatus = FileUtils.maxOffset(storage, new Path(path),
				new CommitFileTopicPartitionFilter(topicPartition));

		if (fileStatus != null) {
			long maxOffset = FileUtils.extractOffset(fileStatus.getPath().getName());

			// for next round
			offset = maxOffset + 1;
		}
	}

	private enum State {
		RECOVER_STARTED, RECOVER_PARTITION_HALT, OFFSET_RESET, WRITE_STARTED, WRITE_PARTITION_HALT, SHOULD_ROTATE, TMP_FILE_CLOSED, FILE_COMMITTED;

		private static State[] states = values();

		public State next() {
			return states[(this.ordinal() + 1) % states.length];
		}
	}

	/**
	 * return the offset.
	 */
	public long offset() {
		return offset;
	}
}
