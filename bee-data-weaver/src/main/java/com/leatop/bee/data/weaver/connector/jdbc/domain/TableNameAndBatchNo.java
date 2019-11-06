/**
 * File: TableNameAndBatchNo.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月16日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

import java.util.Objects;

import com.leatop.bee.data.weaver.connector.utils.QuoteMethod;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder.SQLize;

/**
 * Container, contains only table name and batch number.
 * 
 * @author DORSEy
 *
 */
public class TableNameAndBatchNo implements SQLize {
	private final String tableName;
	private final String batchNo;

	// ~~~ public fields
	// =====================================================================
	public static final String DELIM_COLON = ":";

	/**
	 * @param tableName
	 * @param batchNo
	 */
	public TableNameAndBatchNo(final String tableName, final String batchNo) {
		this.tableName = tableName;
		this.batchNo = batchNo;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @return the batchNo
	 */
	public String getBatchNo() {
		return batchNo;
	}

	@Override
	public String toString() {
		return "Table name: " + tableName + ", batch number: " + batchNo;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof TableNameAndBatchNo))
			return false;

		TableNameAndBatchNo other = (TableNameAndBatchNo) obj;
		return Objects.equals(tableName, other.tableName) && Objects.equals(batchNo, other.batchNo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tableName, batchNo);
	}

	@Override
	public void appendTo(final SQLBuilder builder, final boolean useQuotes) {
		appendTo(builder, useQuotes ? QuoteMethod.ALWAYS : QuoteMethod.NEVER);
	}

	@Override
	public void appendTo(final SQLBuilder builder, final QuoteMethod quoteMethod) {
		if (tableName != null) {
			builder.append(tableName);
			builder.append(DELIM_COLON);
		}

		builder.append(batchNo);
	}
}
