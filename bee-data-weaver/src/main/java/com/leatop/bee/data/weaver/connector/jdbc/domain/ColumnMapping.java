/**
 * File: ColumnMapping.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

import java.sql.ResultSet;
import java.util.Objects;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;

/**
 * A mapping of a {@link ColumnDefinition result set column definition} and a
 * {@link Field} within a {@link Schema}.
 */
public class ColumnMapping {

	private final Field field;
	private final Column columnDefn;
	private final int columnNumber;
	private final int hash;

	/**
	 * Create the column mapping.
	 *
	 * @param columnDefn   the definition of the column; may not be null
	 * @param columnNumber the 1-based number of the column within the result set;
	 *                     must be positive
	 * @param field        the corresponding {@link Field} within the
	 *                     {@link Schema}; may not be null
	 */
	public ColumnMapping(final Column columnDefn, final int columnNumber, final Field field) {
		assert columnDefn != null;
		assert field != null;
		assert columnNumber > 0;
		this.columnDefn = columnDefn;
		this.field = field;
		this.columnNumber = columnNumber;
		this.hash = Objects.hash(this.columnNumber, this.columnDefn, this.field);
	}

	/**
	 * Get this mapping's {@link Field}.
	 *
	 * @return the field; never null
	 */
	public Field field() {
		return field;
	}

	/**
	 * Get this mapping's {@link Column result set column definition}.
	 *
	 * @return the column definition; never null
	 */
	public Column columnDefn() {
		return columnDefn;
	}

	/**
	 * Get the 1-based number of the column within the result set. This can be used
	 * to access the corresponding value from the {@link ResultSet}.
	 *
	 * @return the column number within the {@link ResultSet}; always positive
	 */
	public int columnNumber() {
		return columnNumber;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ColumnMapping) {
			ColumnMapping that = (ColumnMapping) obj;
			return this.columnNumber == that.columnNumber && Objects.equals(this.columnDefn, that.columnDefn)
					&& Objects.equals(this.field, that.field);
		}
		return false;
	}

	@Override
	public String toString() {
		return field.name() + " (col=" + columnNumber + ", " + columnDefn + ")";
	}
}
