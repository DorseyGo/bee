/**
 * File: VersionUtils.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.utils;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for fetching the version.
 * 
 * @author Dorsey
 *
 */
public class VersionUtils {

	private static final Logger log = LoggerFactory.getLogger(VersionUtils.class);
	private static final String PATH = "/kafka-connect-jdbc-version.properties";
	private static String version = "UNKNOWN";

	static {
		try (InputStream stream = VersionUtils.class.getResourceAsStream(PATH)) {
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
