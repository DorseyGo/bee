/**
 * File: CachConnectionSupplier.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.conn;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.leatop.bee.common.log.LogFacade;
import com.leatop.bee.data.weaver.connector.jdbc.sink.JDBCSinkConfig;

/**
 * An implementation of {@link ConnectionSupplier}, which buffers a connection
 * obtaining.
 * 
 * @author Dorsey
 *
 */
public class CacheConnectionSupplier implements ConnectionSupplier {

	private static final LogFacade LOG = LogFacade.getFacade(CacheConnectionSupplier.class);
	private static final int VALIDITY_CHECK_S = 5;
	private Connection connection;
	private int tries = 0;
	private final ConnectionSupplier connectionSupplier;
	private final int maxConnectionRetries;
	private final Object lock = new Object();

	/**
	 * Time in milliseconds to wait for obtaining another connection.
	 */
	private final long connectionRetryBackoff;

	/**
	 * Constructor of {@link CacheConnectionSupplier}, with connection supplier
	 * specified.
	 * 
	 * @param connectionSupplier
	 */
	public CacheConnectionSupplier(final ConnectionSupplier connectionSupplier) {
		this(connectionSupplier, JDBCSinkConfig.CONNECTION_RETRIES_DEFAULT,
				JDBCSinkConfig.CONNECTION_RETRY_BACKOFF_MS_DEFAULT);
	}

	public CacheConnectionSupplier(final ConnectionSupplier connectionSupplier,
			final int maxConnectionRetries, final long connectionRetryBackoff) {
		this.connectionSupplier = connectionSupplier;
		this.maxConnectionRetries = maxConnectionRetries;
		this.connectionRetryBackoff = connectionRetryBackoff;
	}

	@Override
	public Connection getConnection() throws SQLException {
		synchronized (lock) {
			try {
				if (connection == null) {
					// if no connection available, then create one.
					newConnection();
					onConnect(connection);

					return connection;
				}
				if (!isConnectionValid(connection, VALIDITY_CHECK_S)) {
					// if connection invalid, then create one.
					close();
					newConnection();

					return connection;
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}

			return connection;
		}
	}

	/**
	 * @param conn
	 */
	protected void onConnect(final Connection conn) throws SQLException {
		// empty
	}

	/**
	 * Create a new connection. Try
	 * {@link CacheConnectionSupplier#maxConnectionRetries} times to obtain the
	 * connection.
	 */
	private void newConnection() throws SQLException {
		int attempts = 0;
		while (attempts < maxConnectionRetries) {
			try {
				++tries;
				LOG.info("Try to open connection to #{} to {}", tries, connectionSupplier);
				connection = connectionSupplier.getConnection();
				onConnect(connection);

				return;
			} catch (SQLException e) {
				attempts++;
				if (attempts < maxConnectionRetries) {
					LOG.info("Unable to connect to database on attempt {}/{}, will retry after {}",
							attempts, maxConnectionRetries, connectionRetryBackoff);

					try {
						TimeUnit.MILLISECONDS.sleep(connectionRetryBackoff);
					} catch (InterruptedException ignore) {
						// ignore this
					}

					continue;
				}

				// rethrow this exception.
				throw new SQLException(e);
			}
		}
	}

	@Override
	public void close() {
		synchronized (lock) {
			try {
				if (connection != null) {
					LOG.info("Attempt to close the connection #{} to {}", tries,
							connectionSupplier);
					connection.close();
				}
			} catch (SQLException e) {
				LOG.warn("Error on closing connection", e);
			} finally {
				connection = null;
				if (connectionSupplier != null) {
					connectionSupplier.close();
				}
			}
		}
	}

	@Override
	public boolean isConnectionValid(final Connection conn, final int timeout) throws SQLException {
		return connectionSupplier.isConnectionValid(connection, timeout);
	}

	@Override
	public String identifier() {
		return connectionSupplier.identifier();
	}
}
