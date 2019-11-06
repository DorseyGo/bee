/**
 * File: DialectSupplier.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月10日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.conn.dialect;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import org.apache.kafka.common.config.AbstractConfig;

/**
 * @author Dorsey
 *
 */
public abstract class DialectSupplier {

	private final String name;
	public static final int NO_MATCH_SCORE = 0;
	public static final int BEST_MATCH_SCORE = 10;
	public static final int AVERAGE_MATCH_SCORE = 5;

	/**
	 * Constructor of {@link DialectSupplier}, with name specified.
	 * 
	 * @param name
	 *            the name.
	 */
	public DialectSupplier(final String name) {
		Objects.requireNonNull(name);
		this.name = name;
	}

	public abstract Dialect create(final AbstractConfig config);

	public abstract int score(final JDBCUrlInfo jdbcUrlInfo);

	public String dialectName() {
		return name;
	}

	@Override
	public String toString() {
		return dialectName();
	}

	public interface JDBCUrlInfo {

		public String subProtocol();

		public String subName();

		public String url();
	}

	/**
	 * Fixed score dialect supplier.
	 * 
	 * @author Dorsey
	 *
	 */
	public static abstract class FixedScoreDialectSupplier extends DialectSupplier {

		private final int score;

		protected FixedScoreDialectSupplier(final String name, final int score) {
			super(name);
			this.score = score;
		}

		@Override
		public int score(final JDBCUrlInfo jdbcUrlInfo) {
			return score;
		}
	}

	public static abstract class SubProtocolDialectSupplier extends DialectSupplier {

		private final Set<String> subProtocols;

		protected SubProtocolDialectSupplier(final String name, final String... subProtocols) {
			super(name);
			this.subProtocols = new HashSet<>(Arrays.asList(subProtocols));
		}

		protected SubProtocolDialectSupplier(final String name,
				final Collection<String> subProtocols) {
			super(name);
			this.subProtocols = new HashSet<>(subProtocols);
		}

		@Override
		public int score(final JDBCUrlInfo jdbcUrlInfo) {
			if (jdbcUrlInfo != null) {
				for (String subProtocol : subProtocols) {
					if (subProtocol.equalsIgnoreCase(jdbcUrlInfo.subProtocol())) {
						return BEST_MATCH_SCORE;
					}
				}

				String combination = jdbcUrlInfo.subProtocol() + ":" + jdbcUrlInfo.subName();
				combination = combination.toLowerCase(Locale.getDefault());
				for (String subProtocol : subProtocols) {
					if (subProtocol.equalsIgnoreCase(combination)) {
						return BEST_MATCH_SCORE;
					}
				}
			}

			return NO_MATCH_SCORE;
		}
	}
}
