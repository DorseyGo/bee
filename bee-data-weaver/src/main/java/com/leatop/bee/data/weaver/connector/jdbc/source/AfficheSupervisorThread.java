/**
 * File: AfficheSupervisorThread.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月16日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.connect.connector.ConnectorContext;
import org.apache.kafka.connect.errors.ConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.conn.CacheConnectionSupplier;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableNameAndBatchNo;

/**
 * A thread to monitor on the affiche table, and retrieve the records as long as
 * it has.
 * 
 * @author DORSEy
 *
 */
public class AfficheSupervisorThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(AfficheSupervisorThread.class);

	// ~~~ Fields
	// =====================================================
	private final TableId tableId;
	private final Dialect dialect;
	private final CacheConnectionSupplier connectionSupplier;
	private final ConnectorContext context;
	private final long retryBackoffs;
	private final CountDownLatch shutdownLatch;
	private final long timeout;

	private List<TableNameAndBatchNo> tableNameAndBatchNos;
	private volatile List<TableNameAndBatchNo> prevTblNameAndBatchNos;

	/**
	 * Default constructor of {@link AfficheSupervisorThread}.
	 * 
	 */
	public AfficheSupervisorThread(final String afficheTableName, final Dialect dialect,
			final CacheConnectionSupplier connectionSupplier, final ConnectorContext context,
			final long retryBackoffs, final long timeout) {
		this.dialect = dialect;
		this.connectionSupplier = connectionSupplier;
		this.context = context;
		this.retryBackoffs = retryBackoffs;
		this.tableId = dialect.parseTableId(afficheTableName);
		this.shutdownLatch = new CountDownLatch(1);
		this.timeout = timeout;
	}

	@Override
	public void run() {
		LOG.info(
				"Starting AfficheSupervisorThread to see whether new records comes in specified Affiched table {}",
				tableId);

		while (shutdownLatch.getCount() > 0) {
			try {
				if (retrieveRecordsIfPresent()) {
					context.requestTaskReconfiguration();
				}
			} catch (Exception e) {
				context.raiseError(e);
				throw e;
			}

			try {
				LOG.debug("Waiting {} ms to check for new records coming.", retryBackoffs);
				if (shutdownLatch.await(retryBackoffs, TimeUnit.MILLISECONDS)) {
					return;
				}
			} catch (InterruptedException ignore) {
				LOG.error("Unexpected InterruptedException, ignoring: ", ignore);
			}
		}
	}

	public synchronized List<TableNameAndBatchNo> tableNameAndBatchNos() {
		final long start = System.currentTimeMillis();
		long now = start;
		while ((tableNameAndBatchNos == null || tableNameAndBatchNos.isEmpty())
				&& ((now - start) < timeout)) {
			try {
				wait((timeout - (now - start)));
			} catch (InterruptedException ignore) {
				// ignore it
			}

			now = System.currentTimeMillis();
		}

		if (tableNameAndBatchNos == null || tableNameAndBatchNos.isEmpty()) {
			throw new ConnectException(
					"Records in affiche table: " + tableId + " not coming quickly enough");
		}

		return tableNameAndBatchNos;
	}

	/**
	 * @return
	 */
	private synchronized boolean retrieveRecordsIfPresent() {
		try {
			this.tableNameAndBatchNos = dialect
					.tableNameAndBatchNos(connectionSupplier.getConnection(), tableId);
			LOG.debug("Records being retrieved from affiche table: {} are {}", tableId,
					tableNameAndBatchNos);
		} catch (SQLException e) {
			LOG.error("Failed to retrieve records from affiche table: {}", tableId, e);
			connectionSupplier.close();
			return false;
		}

		boolean updated = (this.tableNameAndBatchNos == null) ? false
				: !tableNameAndBatchNos.equals(prevTblNameAndBatchNos);
		this.prevTblNameAndBatchNos = tableNameAndBatchNos;

		notifyAll();
		return updated;
	}

	public void shutdown() {
		LOG.info("Shutting down Affiche supervisor thread...");
		shutdownLatch.countDown();
	}
}
