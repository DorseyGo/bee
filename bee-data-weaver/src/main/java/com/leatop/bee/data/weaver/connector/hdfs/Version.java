/**
 * File: Version.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月3日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.hdfs;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.utils.VersionUtils;

/**
 * Helper class, which is used for fetch the version on purpose.
 * 
 * @author Dorsey
 *
 */
public class Version {

	private static final Logger log = LoggerFactory.getLogger(VersionUtils.class);
	private static final String PATH = "/kafka-connect-hdfs-version.properties";
	private static String version = "UNKNOWN";

	static {
		try (InputStream stream = Version.class.getResourceAsStream(PATH)) {
			Properties props = new Properties();
			props.load(stream);
			version = props.getProperty("version", version).trim();
		} catch (Exception e) {
			log.warn("Error while loading version:", e);
		}
	}

	public static String getVersion() {
		return version;
	}
}
