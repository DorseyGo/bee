/**
 * File: Tables.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.leatop.bee.common.log.LogFacade;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;

/**
 * @author Dorsey
 *
 */
public class Tables {

	private final LogFacade LOG = LogFacade.getFacade(Tables.class);
	private final Map<TableId, Table> buffer = new HashMap<>();
	private final Dialect dialect;

	public Tables(final Dialect dialect) {
		this.dialect = dialect;
	}

	public Table get(final Connection connection, final TableId tableId) throws SQLException {
		Table table = buffer.get(tableId);
		if (table == null) {
			// if not exists in buffer
			if (dialect.tableExists(connection, tableId)) {
				table = dialect.describeTable(connection, tableId);
				if (table != null) {
					LOG.info("Load metadata for table {} to {}", tableId, table);
					buffer.put(tableId, table);
				}
			}
		}

		return table;
	}

	public Table refresh(final Connection connection, final TableId tableId) throws SQLException {
		Table table = dialect.describeTable(connection, tableId);
		LOG.info("Refresh the metadata for table {} to {}", tableId, table);
		
		buffer.put(tableId, table);
		return table;
	}
}
