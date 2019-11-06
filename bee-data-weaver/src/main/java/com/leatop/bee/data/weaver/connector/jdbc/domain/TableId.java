/**
 * File: TableId.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

import java.util.Objects;

import com.leatop.bee.data.weaver.connector.utils.QuoteMethod;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder.SQLize;

/**
 * Container, which contains the basic information which can identifies out the
 * table in database..
 * 
 * @author Dorsey
 *
 */
public class TableId implements Comparable<TableId>, SQLize {

	// database (1) -> schema (*) -> table (*)
	private final String catelogName;
	private final String schemaName;
	private final String tableName;
	private final int hashCode;

	/**
	 * Constructor of {@link Table}, with schema name and table name specified.
	 * 
	 * @param schemaName
	 *            the schema name.
	 * @param tableName
	 *            the table name.
	 */
	public TableId(final String catelogName, final String schemaName, final String tableName) {
		this.catelogName = (catelogName == null || catelogName.isEmpty()) ? null : catelogName;
		this.schemaName = (schemaName == null || schemaName.isEmpty()) ? null : schemaName;
		this.tableName = tableName;
		this.hashCode = Objects.hash(catelogName, schemaName, tableName);
	}

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	public String catelogName() {
		return catelogName;
	}

	@Override
	public int compareTo(final TableId other) {
		if (this == other)
			return 0;
		int diff = (tableName.compareTo(other.tableName));
		if (diff != 0) {
			return diff;
		}

		if (schemaName == null) {
			if (other.schemaName != null) {
				return -1;
			}
		} else {
			if (other.schemaName == null) {
				return 1;
			}

			diff = schemaName.compareTo(other.schemaName);
			if (diff != 0) {
				return diff;
			}
		}

		if (catelogName == null) {
			if (other.catelogName != null) {
				return -1;
			}
		} else {
			if (other.catelogName == null) {
				return 1;
			}

			diff = catelogName.compareTo(other.catelogName);
			if (diff != 0) {
				return diff;
			}
		}

		return 0;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof TableId))
			return false;

		TableId other = (TableId) obj;

		return Objects.equals(catelogName, other.catelogName)
				&& Objects.equals(schemaName, other.schemaName)
				&& Objects.equals(tableName, other.tableName);
	}

	@Override
	public String toString() {
		return SQLBuilder.create().append(this).toString();
	}

	@Override
	public void appendTo(final SQLBuilder builder, final boolean useQuotes) {
		appendTo(builder, (useQuotes) ? QuoteMethod.ALWAYS : QuoteMethod.NEVER);
	}

	@Override
	public void appendTo(final SQLBuilder builder, final QuoteMethod quoteMethod) {
		if (catelogName != null) {
			builder.appendIdentifier(catelogName, quoteMethod);
			builder.appendIdentifierDelim();
		}
		
		if (schemaName != null) {
			builder.appendIdentifier(schemaName, quoteMethod);
			builder.appendIdentifierDelim();
		}
		
		builder.appendTableName(tableName);
	}
}
