/**
 * File: Dialects.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月10日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.conn.dialect;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.errors.ConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.DialectSupplier.JDBCUrlInfo;


/**
 * @author Dorsey
 *
 */
public class Dialects {

	private static final Pattern pattern = Pattern.compile("jdbc:([^:]+):(.*)");
	private static final Logger LOG = LoggerFactory.getLogger(Dialects.class);
	private static final ConcurrentMap<String, DialectSupplier> DIALECT_SUPPLIERS = new ConcurrentSkipListMap<>();

	static {
		loadAllSuppliers();
	}

	/**
	 * Load all suppliers.
	 */
	private static void loadAllSuppliers() {
		LOG.info("Attempt to load all suppliers");
		AccessController.doPrivileged(new PrivilegedAction<Void>() {

			@Override
			public Void run() {
				ServiceLoader<DialectSupplier> serviceLoader = ServiceLoader
						.load(DialectSupplier.class);
				Iterator<DialectSupplier> iter = serviceLoader.iterator();
				try {
					while (iter.hasNext()) {
						try {
							DialectSupplier supplier = iter.next();
							DIALECT_SUPPLIERS.put(supplier.getClass().getName(), supplier);
						} catch (Throwable cause) {
							LOG.debug("Skip dialect supplier load", cause);
						}
					}
					
					LOG.info("Load {} dialect suppliers: {}", DIALECT_SUPPLIERS.size(), DIALECT_SUPPLIERS);
				} catch (Throwable cause) {
					LOG.warn("Error when loading dialect supplier", cause);
				}

				return null;
			}
		});
	}

	public static Dialect findBestFor(final String jdbcUrl, final AbstractConfig config)
			throws ConnectException {
		final JDBCUrlInfo jdbcUrlInfo = extractJDBCUrlInfo(jdbcUrl);
		int basicScore = DialectSupplier.NO_MATCH_SCORE;
		
		LOG.info("Find best dialect for JDBC url info: {} with config: {}", jdbcUrlInfo, config);
		DialectSupplier bestFitSupplier = null;
		for (DialectSupplier supplier : DIALECT_SUPPLIERS.values()) {
			int score = supplier.score(jdbcUrlInfo);
			if (score > basicScore) {
				bestFitSupplier = supplier;
				basicScore = score;
			}
		}

		return bestFitSupplier.create(config);
	}

	private static JDBCUrlInfo extractJDBCUrlInfo(final String jdbcUrl) {
		Matcher matcher = pattern.matcher(jdbcUrl);
		if (matcher.matches()) {
			return new BasicJDBCUrlInfo(matcher.group(1), matcher.group(2), jdbcUrl);
		}

		throw new ConnectException("Not a valid JDBC url: " + jdbcUrl);
	}

	public static Dialect create(final String dialectName, final AbstractConfig config)
			throws ConnectException {
		LOG.info("Attempt to create dialect of {} by using config {}", dialectName, config);
		Set<String> dialectNames = new HashSet<>();
		for (DialectSupplier dialectSupplier : DIALECT_SUPPLIERS.values()) {
			if (dialectSupplier.dialectName().equalsIgnoreCase(dialectName)) {
				return dialectSupplier.create(config);
			}

			dialectNames.add(dialectSupplier.dialectName());
		}

		throw new ConnectException("Unable to find a dialect with name: " + dialectName
				+ " in available dialect names: " + dialectNames);
	}

	
	/**
	 * Return the names of all of the available dialects.
	 *
	 * @return the dialect names; never null
	 */
	
	
	public static Set<String> registeredDialectNames() {
		Set<String> dialectNames = new HashSet<>();
		for (DialectSupplier dialectSupplier : DIALECT_SUPPLIERS.values()) {
			dialectNames.add(dialectSupplier.dialectName());
		}
		return dialectNames;
	}
	 
	 
	
	/**
	 * Basic implementation of {@link JDBCUrlInfo}.
	 * 
	 * @author Dorsey
	 *
	 */
	static class BasicJDBCUrlInfo implements JDBCUrlInfo {

		private final String subProtocol;
		private final String subName;
		private final String url;

		public BasicJDBCUrlInfo(final String subProtocol, final String subName, final String url) {
			this.subProtocol = subProtocol;
			this.subName = subName;
			this.url = url;
		}

		@Override
		public String subProtocol() {
			return this.subProtocol;
		}

		@Override
		public String subName() {
			return this.subName;
		}

		@Override
		public String url() {
			return this.url;
		}

		@Override
		public String toString() {
			return "{subProtocol=" + subProtocol + ", subName=" + subName + ", url=" + url + "}";
		}
	}
}
