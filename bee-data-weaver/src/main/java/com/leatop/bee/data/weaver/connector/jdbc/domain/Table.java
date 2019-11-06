/**
 * File: Table.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Dorsey
 *
 */
public class Table {

	// ~~~ fields
	// ===================================================================
	private final TableId tableId;
	private final Map<String, Column> colByNames = new HashMap<>();
	private final Map<String, String> pkColNames = new HashMap<>();

	// ~~~ constructors
	// ===================================================================
	public Table(final TableId tableId, final Iterable<Column> cols) {
		this.tableId = tableId;
		for (Column col : cols) {
			String colName = col.getColumnId().getColumnName();
			colByNames.put(colName, col.forTable(tableId));

			if (col.isPrimaryKey()) {
				pkColNames.put(colName, colName);
			}
		}
	}

	// ~~~ methods
	// ===================================================================
	public TableId getTableId() {
		return tableId;
	}

	@Override
	public int hashCode() {
		return tableId.hashCode() + Objects.hash(colByNames, pkColNames);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Table))
			return false;

		Table other = (Table) obj;

		return Objects.equals(tableId, other.tableId)
				&& Objects.equals(colByNames, other.colByNames)
				&& Objects.equals(pkColNames, other.pkColNames);
	}

	@Override
	public String toString() {
		return "Table [tableId=" + tableId + ", colByNames=" + colByNames + ", pkColNames="
				+ pkColNames + "]";
	}

}
