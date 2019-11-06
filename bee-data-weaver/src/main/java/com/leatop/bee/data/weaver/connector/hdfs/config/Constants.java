/**
 * File: Constants.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月4日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs.config;

import java.util.regex.Pattern;

/**
 * @author Dorsey
 *
 */
public interface Constants {

	// for hadoop
	String SYS_HADOOP_HOME_DIR_CONFIG = "hadoop.home.dir";
	String CORE_SITE_XML = "core-site.xml";
	String HDFS_SITE_XML = "hdfs-site.xml";
	String HADOOP_SECURITY_AUTHENTICATION_KERBEROS = "kerberos";
	String HADOOP_SECURITY_AUTHENTICATION = "hadoop.security.authentication";
	String HADOOP_SECURITY_AUTHORIZATION = "hadoop.security.authroziation";
	String DFS_NAMENODE_KERBEROS_PRINCIPAL = "dfs.namenode.kerberos.principal";
	Pattern COMMIT_FILENAME_PATTERN = Pattern
			.compile("([a-zA-Z0-9\\._\\-]+)\\+(\\d+)\\+(\\d+)\\+(\\d+)(.\\w+)?");
	String TEMP_FILE_FOLDER = "/+tmp/";
	int PATTERN_TOPIC_GROUP = 1;
	int PATTERN_PARTITION_GROUP = 2;
	int PATTERN_START_OFFSET_GROUP = 3;
	int PATTERN_END_OFFSET_GROUP = 4;

	// commons
	String FORWARD_SLASH_DELIM = "/";
	String TRUE_STR = "true";
	String TMP_SYMBOL = "tmp";
	String UNDER_SCORE_DELIM = "_";
	String FALSE_STR = "false";
	String SCHEMAS_ENABLE_CONFIG = "schemas.enable";
	String SCHEMAS_CACHE_SIZE_CONFIG = "schemas.cache.size";
	String COMMITTED_FILENAME_SEPARATOR = "+";
}
