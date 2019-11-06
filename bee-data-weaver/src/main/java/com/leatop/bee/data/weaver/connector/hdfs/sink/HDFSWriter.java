/**
 * File: HDFSWriter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月3日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.sink;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.SecurityUtil;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.UserGroupInformation.AuthenticationMethod;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.hdfs.config.Constants;
import com.leatop.bee.data.weaver.connector.hdfs.config.HDFSSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.config.PartitionerConfig;
import com.leatop.bee.data.weaver.connector.hdfs.config.StorageSinkConnectorConfig;
import com.leatop.bee.data.weaver.connector.hdfs.partitioner.Partitioner;
import com.leatop.bee.data.weaver.connector.hdfs.partitioner.PartitionerFactory;
import com.leatop.bee.data.weaver.connector.hdfs.storage.HDFSStorage;
import com.leatop.bee.data.weaver.connector.hdfs.storage.StorageFactory;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.Formatter;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.FormatterFactory;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.RecordWriterSupplier;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.SchemaFileReader;

/**
 * @author Dorsey
 *
 */
public class HDFSWriter {

	private static final Logger LOG = LoggerFactory.getLogger(HDFSWriter.class);
	private final Object lock = new Object();

	// ~~~~~ fields
	// ============================================================================
	private Map<TopicPartition, TopicWriter> topicWriters;
	// HDFS url
	private String url;
	private String topicsFolder;
	private HDFSStorage hdfsStorage;
	private RecordWriterSupplier<HDFSSinkConnectorConfig> recordWriterSupplier;
	private Set<TopicPartition> assignment;
	private Partitioner partitioner;
	private Thread tokenRefreshThread;
	private volatile boolean running;

	private final HDFSSinkConnectorConfig config;
	private final SinkTaskContext context;
	private Formatter<HDFSSinkConnectorConfig, Path> formatter;
	private SchemaFileReader<HDFSSinkConnectorConfig, Path> schemaFileReader;

	// ~~~~~ constructors
	// ============================================================================
	public HDFSWriter(final HDFSSinkConnectorConfig config, final SinkTaskContext context) {
		this.config = config;
		this.context = context;

		// initialize other services.
		initialize();
	}

	/**
	 * initialize other sort of services.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		final String hadoopHome = config.getString(HDFSSinkConnectorConfig.HADOOP_HOME_CONFIG);
		System.setProperty(Constants.SYS_HADOOP_HOME_DIR_CONFIG, hadoopHome);

		final String hadoopConfDir = config
				.getString(HDFSSinkConnectorConfig.HADOOP_CONF_DIR_CONFIG);
		LOG.info("Hadoop Configuration directory: {}", hadoopConfDir);
		Configuration conf = config.getHadoopConfiguration();
		if (!"".equals(hadoopConfDir)) {
			conf.addResource(new Path(
					hadoopConfDir + Constants.FORWARD_SLASH_DELIM + Constants.CORE_SITE_XML));
			conf.addResource(new Path(
					hadoopConfDir + Constants.FORWARD_SLASH_DELIM + Constants.HDFS_SITE_XML));
		}

		try {
			initSecurityIfNeeded(conf);

			url = config.getString(HDFSSinkConnectorConfig.HDFS_URL_CONFIG);
			topicsFolder = config.getString(StorageSinkConnectorConfig.TOPICS_FOLDER_CONFIG);
			Class<? extends HDFSStorage> storageClass = (Class<? extends HDFSStorage>) config
					.getClass(StorageSinkConnectorConfig.STORAGE_CLASS_CONFIG);

			hdfsStorage = StorageFactory.getStorage(storageClass, HDFSSinkConnectorConfig.class,
					config, url);
			// the folder to which the data should be written
			createFolder(topicsFolder);
			// the folder for temporary store the data.
			createFolder(topicsFolder + Constants.TEMP_FILE_FOLDER);
			createFolder(config.getString(HDFSSinkConnectorConfig.LOGS_DIR_CONFIG));

			// the formatter
			initFormatter();

			partitioner = getPartitioner(config);
			assignment = new HashSet<>(context.assignment());

			topicWriters = new HashMap<>();
			for (TopicPartition topicPartition : assignment) {
				TopicWriter partitionWriter = new TopicWriter(topicPartition, hdfsStorage,
						recordWriterSupplier, partitioner, config, context, schemaFileReader);
				topicWriters.put(topicPartition, partitionWriter);
			}
		} catch (IOException e) {
			throw new ConnectException(e);
		}
	}

	private void initSecurityIfNeeded(final Configuration conf) throws IOException {
		boolean enableSecurity = config
				.getBoolean(HDFSSinkConnectorConfig.HDFS_AUTHENTICATION_KERBEROS_CONFIG);
		if (enableSecurity) {
			SecurityUtil.setAuthenticationMethod(AuthenticationMethod.KERBEROS, conf);

			final String principal = config
					.getString(HDFSSinkConnectorConfig.CONNECT_HDFS_PRINCIPAL_CONFIG);
			final String keytable = config
					.getString(HDFSSinkConnectorConfig.CONNECT_HDFS_KEYTAB_CONFIG);
			if (principal == null || keytable == null) {
				throw new ConfigException(
						"No principal or keytab provided for hadoop as kerberos enabled");
			}

			conf.set(Constants.HADOOP_SECURITY_AUTHENTICATION,
					Constants.HADOOP_SECURITY_AUTHENTICATION_KERBEROS);
			conf.set(Constants.HADOOP_SECURITY_AUTHORIZATION, Constants.TRUE_STR);

			String nnPrincipalConfig = config
					.getString(HDFSSinkConnectorConfig.HDFS_NAMENODE_PRINCIPAL_CONFIG);
			String hostname = InetAddress.getLocalHost().getCanonicalHostName();
			String nnPrincipal = SecurityUtil.getServerPrincipal(nnPrincipalConfig, hostname);
			if (conf.get(Constants.DFS_NAMENODE_KERBEROS_PRINCIPAL) == null) {
				conf.set(Constants.DFS_NAMENODE_KERBEROS_PRINCIPAL, nnPrincipal);
			}

			// log down for debugging.
			LOG.info("Hadoop NameNOde kerberos principle: {}",
					conf.get(Constants.DFS_NAMENODE_KERBEROS_PRINCIPAL));

			UserGroupInformation.setConfiguration(conf);
			final String serverPrincipal = SecurityUtil.getServerPrincipal(principal, hostname);
			UserGroupInformation.loginUserFromKeytab(serverPrincipal, keytable);
			final UserGroupInformation ugi = UserGroupInformation.getLoginUser();
			LOG.info("Login hadoop as: {}", ugi.getUserName());

			final long period = config
					.getLong(HDFSSinkConnectorConfig.KERBEROS_TICKET_RENEW_PERIOD_MS_CONFIG);
			running = true;
			tokenRefreshThread = new Thread(new Runnable() {

				@Override
				public void run() {
					synchronized (lock) {
						while (running) {
							try {
								HDFSWriter.this.wait(period);

								if (running) {
									ugi.reloginFromKeytab();
								}
							} catch (IOException e) {
								LOG.error("Failed to relogin via kerberos");
							} catch (InterruptedException ignore) {
								// ignore
							}
						}
					}
				}
			});

			LOG.info("Starting token renew thread to keep login hadoop");
			tokenRefreshThread.start();
		}
	}

	/**
	 * Format, in which, the data will be written.
	 */
	@SuppressWarnings("unchecked")
	private void initFormatter() {
		Class<? extends Formatter<HDFSSinkConnectorConfig, Path>> formatterClass = (Class<? extends Formatter<HDFSSinkConnectorConfig, Path>>) config
				.getClass(HDFSSinkConnectorConfig.FORMATTER_CLASS_CONFIG);

		formatter = FormatterFactory.getFormatter(formatterClass, hdfsStorage);
		recordWriterSupplier = formatter.getRecordWriterSupplier();
		schemaFileReader = formatter.getSchemaFileReader();
	}

	@SuppressWarnings("unchecked")
	private Partitioner getPartitioner(final HDFSSinkConnectorConfig config) {
		Class<? extends Partitioner> clazz = (Class<? extends Partitioner>) config
				.getClass(PartitionerConfig.PARTITIONER_CLASS_CONFIG);

		return PartitionerFactory.getPartitioner(clazz,
				new HashMap<String, Object>(config.plainValues()));
	}

	/**
	 * Create the folder in the underlying storage.
	 * 
	 * @param folder
	 */
	private void createFolder(final String folder) {
		final String path = url + Constants.FORWARD_SLASH_DELIM + folder;
		if (!hdfsStorage.exists(path)) {
			hdfsStorage.create(path);
		}
	}

	/**
	 * Write the collection of records into the underlying storage.
	 * 
	 * @param records
	 *            the records to be written.
	 */
	public void write(final Collection<SinkRecord> records) {
		for (SinkRecord record : records) {
			final String topic = record.topic();
			final int partition = record.kafkaPartition();
			final TopicPartition topicPartition = new TopicPartition(topic, partition);

			topicWriters.get(topicPartition).buffer(record);
		}

		for (TopicPartition topicPartition : assignment) {
			topicWriters.get(topicPartition).write();
		}
	}

	public void stop() {
		hdfsStorage.close();
		if (tokenRefreshThread != null) {
			// if authentication used, then should close this thread to refresh
			// the principles.
			synchronized (lock) {
				running = false;
				this.notifyAll();
			}
		}
	}

	public void close() {
		for (TopicPartition topicPartition : assignment) {
			try {
				topicWriters.get(topicPartition).close();
			} catch (ConnectException e) {
				// only need log down it.
				LOG.error("Failed to close writer for topic partition: {}", topicPartition, e);
			}
		}

		// clean up
		topicWriters.clear();
		assignment.clear();
	}

	/**
	 * @param partition
	 */
	public void recover(final TopicPartition partition) {
		topicWriters.get(partition).recover();
	}

	/**
	 * @param partitions
	 */
	public void open(final Collection<TopicPartition> partitions) {
		assignment = new HashSet<TopicPartition>(partitions);
		for (TopicPartition topicPartition : assignment) {
			TopicWriter partitionWriter = new TopicWriter(topicPartition, hdfsStorage,
					recordWriterSupplier, partitioner, config, context, schemaFileReader);
			topicWriters.put(topicPartition, partitionWriter);

			recover(topicPartition);
		}

	}

	/**
	 * @return
	 */
	public Map<TopicPartition, Long> getCommittedOffsets() {
		final Map<TopicPartition, Long> offsets = new HashMap<TopicPartition, Long>();
		for (TopicPartition tp : assignment) {
			long committedOffset = topicWriters.get(tp).offset();

			if (committedOffset >= 0) {
				offsets.put(tp, committedOffset);
			}
		}

		return offsets;
	}

}
