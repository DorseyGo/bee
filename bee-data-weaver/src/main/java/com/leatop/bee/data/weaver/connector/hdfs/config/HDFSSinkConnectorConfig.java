/**
 * File: HDFSSinkConnectorConfig.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月3日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.hadoop.conf.Configuration;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.ConfigKey;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Width;
import org.apache.kafka.common.config.ConfigException;

import com.leatop.bee.data.weaver.connector.hdfs.partitioner.impl.DefaultPartitioner;
import com.leatop.bee.data.weaver.connector.hdfs.partitioner.impl.FieldPartitioner;
import com.leatop.bee.data.weaver.connector.hdfs.storage.HDFSStorage;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.formatters.AvroFormatter;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.formatters.JsonFormatter;
import com.leatop.bee.data.weaver.connector.hdfs.storage.format.formatters.TxtFormatter;

/**
 * @author Dorsey
 *
 */
public class HDFSSinkConnectorConfig extends StorageSinkConnectorConfig {

	public static final String HDFS_URL_CONFIG = "hdfs.url";
	public static final String HDFS_URL_DOC = "The HDFS connection URL.";
	public static final String HDFS_URL_DEFAULT = null;
	public static final String HDFS_URL_DISPLAY = "HDFS URL";

	public static final String HADOOP_CONF_DIR_CONFIG = "hadoop.conf.dir";
	public static final String HADOOP_CONF_DIR_DEFAULT = "";
	private static final String HADOOP_CONF_DIR_DOC = "The Hadoop configuration directory.";
	private static final String HADOOP_CONF_DIR_DISPLAY = "Hadoop Configuration Directory";

	public static final String HADOOP_HOME_CONFIG = "hadoop.home";
	public static final String HADOOP_HOME_DEFAULT = "";
	private static final String HADOOP_HOME_DOC = "The Hadoop home directory.";
	private static final String HADOOP_HOME_DISPLAY = "Hadoop home directory";

	public static final String LOGS_DIR_CONFIG = "logs.dir";
	public static final String LOGS_DIR_DOC = "Top level directory to store the write ahead logs.";
	public static final String LOGS_DIR_DEFAULT = "logs";
	public static final String LOGS_DIR_DISPLAY = "Logs directory";

	// Security group
	public static final String HDFS_AUTHENTICATION_KERBEROS_CONFIG = "hdfs.authentication.kerberos";
	private static final String HDFS_AUTHENTICATION_KERBEROS_DOC = "Indicating whether HDFS is using Kerberos for authentication. false, by default";
	private static final boolean HDFS_AUTHENTICATION_KERBEROS_DEFAULT = false;
	private static final String HDFS_AUTHENTICATION_KERBEROS_DISPLAY = "HDFS Authentication Kerberos";

	public static final String CONNECT_HDFS_PRINCIPAL_CONFIG = "connect.hdfs.principal";
	public static final String CONNECT_HDFS_PRINCIPAL_DEFAULT = "";
	private static final String CONNECT_HDFS_PRINCIPAL_DOC = "The principal to use when HDFS is using Kerberos to for authentication.";
	private static final String CONNECT_HDFS_PRINCIPAL_DISPLAY = "Connect Kerberos Principal";

	public static final String CONNECT_HDFS_KEYTAB_CONFIG = "connect.hdfs.keytab";
	public static final String CONNECT_HDFS_KEYTAB_DEFAULT = "";
	private static final String CONNECT_HDFS_KEYTAB_DOC = "The path to the keytab file for the HDFS connector principal. "
			+ "This keytab file should only be readable by the connector user.";
	private static final String CONNECT_HDFS_KEYTAB_DISPLAY = "Connect Kerberos Keytab";

	public static final String HDFS_NAMENODE_PRINCIPAL_CONFIG = "hdfs.namenode.principal";
	public static final String HDFS_NAMENODE_PRINCIPAL_DEFAULT = "";
	private static final String HDFS_NAMENODE_PRINCIPAL_DOC = "The principal for HDFS Namenode.";
	private static final String HDFS_NAMENODE_PRINCIPAL_DISPLAY = "HDFS NameNode Kerberos Principal";

	public static final String KERBEROS_TICKET_RENEW_PERIOD_MS_CONFIG = "kerberos.ticket.renew.period.ms";
	public static final long KERBEROS_TICKET_RENEW_PERIOD_MS_DEFAULT = 60000 * 30;
	private static final String KERBEROS_TICKET_RENEW_PERIOD_MS_DOC = "The period in milliseconds to renew the Kerberos ticket. 30 mins, by default";
	private static final String KERBEROS_TICKET_RENEW_PERIOD_MS_DISPLAY = "Kerberos Ticket Renew measure in ms";

	// ~~~~ recommenders
	// ======================================================================
	private static final ConfigDef.Recommender hdfsAuthenticationKerberosDependentsRecommender = new BooleanParentRecommender(
			HDFS_AUTHENTICATION_KERBEROS_CONFIG);
	private static final GenericRecommender STORAGE_CLASS_RECOMMENDER = new GenericRecommender();
	private static final GenericRecommender FORMATTER_CLASS_RECOMMENDER = new GenericRecommender();
	private static final GenericRecommender PARTITIONER_CLASS_RECOMMENDER = new GenericRecommender();

	// load when load into jvm.
	static {
		STORAGE_CLASS_RECOMMENDER.addValidValues(HDFSStorage.class);

		FORMATTER_CLASS_RECOMMENDER.addValidValues(AvroFormatter.class, JsonFormatter.class,
				TxtFormatter.class);

		PARTITIONER_CLASS_RECOMMENDER.addValidValues(DefaultPartitioner.class,
				FieldPartitioner.class);
	}

	private final String name;
	private final Map<String, ComposableConfig> propertyToConfig = new HashMap<>();
	private final PartitionerConfig partitionerConfig;
	private final Set<AbstractConfig> allConfigs = new HashSet<>();
	private Configuration hadoopConfig;

	// ~~~ constructors
	// =============================================================================
	public HDFSSinkConnectorConfig(final Map<String, String> originals) {
		this(newConfigDef(), byDefault(originals));
	}

	protected HDFSSinkConnectorConfig(final ConfigDef configDef, final Map<String, String> props) {
		super(configDef, props);
		ConfigDef partitionerConfigDef = PartitionerConfig
				.newConfigDef(PARTITIONER_CLASS_RECOMMENDER);
		this.partitionerConfig = new PartitionerConfig(partitionerConfigDef, originalsStrings());
		this.name = parseName(originalsStrings());
		this.hadoopConfig = new Configuration();

		addToGlobal(partitionerConfig);
		addToGlobal(this);
	}

	// ~~~ methods
	// =============================================================================
	protected static String parseName(final Map<String, String> props) {
		String nameProp = props.get("name");
		return nameProp != null ? nameProp : "HDFS-sink";
	}

	private void addToGlobal(final AbstractConfig config) {
		allConfigs.add(config);
		addConfig(config.values(), (ComposableConfig) config);
	}

	private void addConfig(final Map<String, ?> parsedProps, final ComposableConfig config) {
		for (String key : parsedProps.keySet()) {
			propertyToConfig.put(key, config);
		}
	}

	public String getName() {
		return name;
	}

	@Override
	public Object get(final String key) {
		ComposableConfig config = propertyToConfig.get(key);
		if (config == null) {
			throw new ConfigException(String.format("Unknown configuration '%s'", key));
		}

		return config == this ? super.get(key) : config.get(key);
	}

	public Configuration getHadoopConfiguration() {
		return hadoopConfig;
	}

	public Map<String, ?> plainValues() {
		Map<String, Object> map = new HashMap<>();
		for (AbstractConfig config : allConfigs) {
			map.putAll(config.values());
		}

		return map;
	}

	public static ConfigDef getConfig() {
		Set<String> skip = new HashSet<>(2);
		skip.add(STORAGE_CLASS_CONFIG);
		skip.add(FORMATTER_CLASS_CONFIG);

		ConfigDef config = new ConfigDef();
		addAllConfigKeys(config, newConfigDef(), skip);
		// already add, no need reload again.
		// addAllConfigKeys(config, StorageSinkConnectorConfig
		// .newConfigDef(FORMATTER_CLASS_RECOMMENDER,
		// STORAGE_CLASS_RECOMMENDER), skip);
		addAllConfigKeys(config, PartitionerConfig.newConfigDef(PARTITIONER_CLASS_RECOMMENDER),
				skip);

		config.define(STORAGE_CLASS_CONFIG, Type.CLASS, HDFSStorage.class.getName(),
				Importance.HIGH, STORAGE_CLASS_DOC, "Storage", 1, Width.NONE, STORAGE_CLASS_DISPLAY,
				STORAGE_CLASS_RECOMMENDER);

		config.define(FORMATTER_CLASS_CONFIG, Type.CLASS, AvroFormatter.class.getName(),
				Importance.HIGH, FORMATTER_CLASS_DOC, "Storage", 1, Width.NONE,
				FORMATTER_CLASS_DISPLAY, FORMATTER_CLASS_RECOMMENDER);

		return config;
	}

	private static void addAllConfigKeys(final ConfigDef container, final ConfigDef other,
			final Set<String> skip) {
		for (ConfigDef.ConfigKey key : other.configKeys().values()) {
			if (skip != null && !skip.contains(key.name)) {
				container.define(key);
			}
		}
	}

	/**
	 * @return
	 */
	private static ConfigDef newConfigDef() {
		ConfigDef configDef = new ConfigDef();
		{
			// segment of HDFS configuration.
			final String group = "HDFS";
			int orderInGroup = 0;

			configDef.define(HDFS_URL_CONFIG, Type.STRING, HDFS_URL_DEFAULT, Importance.HIGH,
					HDFS_URL_DOC, group, ++orderInGroup, Width.MEDIUM, HDFS_URL_DISPLAY);
			configDef.define(HADOOP_CONF_DIR_CONFIG, Type.STRING, HADOOP_CONF_DIR_DEFAULT,
					Importance.HIGH, HADOOP_CONF_DIR_DOC, group, ++orderInGroup, Width.MEDIUM,
					HADOOP_CONF_DIR_DISPLAY);
			configDef.define(HADOOP_HOME_CONFIG, Type.STRING, HADOOP_HOME_DEFAULT, Importance.HIGH,
					HADOOP_HOME_DOC, group, ++orderInGroup, Width.SHORT, HADOOP_HOME_DISPLAY);
			configDef.define(LOGS_DIR_CONFIG, Type.STRING, LOGS_DIR_DEFAULT, Importance.HIGH,
					LOGS_DIR_DOC, group, ++orderInGroup, Width.SHORT, LOGS_DIR_DISPLAY);
		}

		// for security.
		{
			final String group = "Security";
			int orderInGroup = 0;
			configDef.define(HDFS_AUTHENTICATION_KERBEROS_CONFIG, Type.BOOLEAN,
					HDFS_AUTHENTICATION_KERBEROS_DEFAULT, Importance.HIGH,
					HDFS_AUTHENTICATION_KERBEROS_DOC, group, ++orderInGroup, Width.SHORT,
					HDFS_AUTHENTICATION_KERBEROS_DISPLAY,
					Arrays.asList(CONNECT_HDFS_PRINCIPAL_CONFIG, CONNECT_HDFS_KEYTAB_CONFIG,
							HDFS_NAMENODE_PRINCIPAL_CONFIG,
							KERBEROS_TICKET_RENEW_PERIOD_MS_CONFIG));
			configDef.define(CONNECT_HDFS_PRINCIPAL_CONFIG, Type.STRING,
					CONNECT_HDFS_PRINCIPAL_DEFAULT, Importance.HIGH, CONNECT_HDFS_PRINCIPAL_DOC,
					group, ++orderInGroup, Width.MEDIUM, CONNECT_HDFS_PRINCIPAL_DISPLAY,
					hdfsAuthenticationKerberosDependentsRecommender);
			configDef.define(CONNECT_HDFS_KEYTAB_CONFIG, Type.STRING, CONNECT_HDFS_KEYTAB_DEFAULT,
					Importance.HIGH, CONNECT_HDFS_KEYTAB_DOC, group, ++orderInGroup, Width.MEDIUM,
					CONNECT_HDFS_KEYTAB_DISPLAY, hdfsAuthenticationKerberosDependentsRecommender);
			configDef.define(HDFS_NAMENODE_PRINCIPAL_CONFIG, Type.STRING,
					HDFS_NAMENODE_PRINCIPAL_DEFAULT, Importance.HIGH, HDFS_NAMENODE_PRINCIPAL_DOC,
					group, ++orderInGroup, Width.MEDIUM, HDFS_NAMENODE_PRINCIPAL_DISPLAY,
					hdfsAuthenticationKerberosDependentsRecommender);
			configDef.define(KERBEROS_TICKET_RENEW_PERIOD_MS_CONFIG, Type.LONG,
					KERBEROS_TICKET_RENEW_PERIOD_MS_DEFAULT, Importance.LOW,
					KERBEROS_TICKET_RENEW_PERIOD_MS_DOC, group, ++orderInGroup, Width.SHORT,
					KERBEROS_TICKET_RENEW_PERIOD_MS_DISPLAY,
					hdfsAuthenticationKerberosDependentsRecommender);
		}

		// add its parent's configuration.
		ConfigDef storageConfigDef = StorageSinkConnectorConfig
				.newConfigDef(FORMATTER_CLASS_RECOMMENDER, STORAGE_CLASS_RECOMMENDER);
		for (ConfigKey key : storageConfigDef.configKeys().values()) {
			configDef.define(key);
		}

		return configDef;
	}

	/**
	 * Get a copy of <code>originals</code>, and make some default values for
	 * the following field:
	 * <ul>
	 * <li>${@link StorageSinkConnectorConfig#STORAGE_CLASS_CONFIG}</li>
	 * <li>${@link StorageSinkConnectorConfig#FORMATTER_CLASS_CONFIG}</li>
	 * </ul>
	 * 
	 * @param originals
	 *            the original configs.
	 * @return
	 */
	private static Map<String, String> byDefault(final Map<String, String> originals) {
		Map<String, String> defaults = new ConcurrentHashMap<>(originals);
		defaults.put(STORAGE_CLASS_CONFIG, HDFSStorage.class.getName());
		defaults.put(FORMATTER_CLASS_CONFIG, AvroFormatter.class.getName());

		return defaults;
	}

	/**
	 * A generic implementation of {@link ConfigDef.Recommender}.
	 * 
	 * @author Dorsey
	 *
	 */
	public static class GenericRecommender implements ConfigDef.Recommender {

		private Set<Object> valids = new CopyOnWriteArraySet<>();

		public void addValidValues(final Object... validVals) {
			valids.addAll(Arrays.asList(validVals));
		}

		@Override
		public List<Object> validValues(final String name, final Map<String, Object> parsedConfig) {
			return new ArrayList<>(valids);
		}

		@Override
		public boolean visible(final String name, final Map<String, Object> parsedConfig) {
			return true;
		}

	}

	private static class BooleanParentRecommender implements ConfigDef.Recommender {

		protected String parentConfigName;

		public BooleanParentRecommender(final String parentConfigName) {
			this.parentConfigName = parentConfigName;
		}

		@Override
		public List<Object> validValues(final String name,
				final Map<String, Object> connectorConfigs) {
			return new LinkedList<>();
		}

		@Override
		public boolean visible(final String name, final Map<String, Object> connectorConfigs) {
			return (Boolean) connectorConfigs.get(parentConfigName);
		}
	}
}
