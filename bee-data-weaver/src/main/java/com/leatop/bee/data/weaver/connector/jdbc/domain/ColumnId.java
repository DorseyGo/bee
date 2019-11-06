/**
 * File: ColumnId.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

import java.util.Objects;

/**
 * Container which contains the basic information that identifies the column.
 * 
 * @author Dorsey
 *
 */
public class ColumnId implements Comparable<ColumnId> {

	// ~~~ fields
	// ===================================================================
	private final TableId tableId;
	private final String columnName;
	private final String columnAlias;
	private final int hash;

	// ~~~ constructors
	// ===================================================================
	public ColumnId(final TableId tableId, final String columnName) {
		this(tableId, columnName, null);
	}

	public ColumnId(final TableId tableId, final String columnName, final String columnAlias) {
		this.tableId = tableId;
		this.columnName = columnName;
		// if null, then same with column name.
		this.columnAlias = (columnAlias == null || columnAlias.isEmpty()) ? columnName
				: columnAlias;

		this.hash = Objects.hash(tableId, columnName, columnAlias);
	}

	// ~~~ methods
	// ===================================================================

	@Override
	public int compareTo(final ColumnId other) {
		if (this == other)
			return 0;
		if (columnName == null && other.columnName != null)
			return -1;
		if (columnName != null && other.columnName == null)
			return 1;
		int diff = columnName.compareTo(other.columnName);
		if (diff != 0)
			return diff;

		if (tableId == null && other.tableId != null)
			return -1;
		if (tableId != null && other.tableId == null)
			return 1;
		diff = tableId.compareTo(other.tableId);
		if (diff != 0)
			return diff;

		return 0;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @return the columnAlias
	 */
	public String getColumnAlias() {
		return columnAlias;
	}

	/**
	 * @return the tableId
	 */
	public TableId getTableId() {
		return tableId;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof ColumnId))
			return false;

		ColumnId other = (ColumnId) obj;
		return Objects.equals(tableId, other.tableId)
				&& Objects.equals(columnName, other.columnName)
				&& Objects.equals(columnAlias, other.columnAlias);
	}

	@Override
	public String toString() {
		return "ColumnId [tableId=" + tableId + ", columnName=" + columnName + ", columnAlias="
				+ columnAlias + "]";
	}

}
