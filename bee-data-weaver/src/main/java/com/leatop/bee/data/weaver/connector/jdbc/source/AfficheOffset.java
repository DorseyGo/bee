/**
 * File: AfficheBatchNoOffset.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月14日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Strings;
import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.GenericDialect.Constants;

/**
 * @author DORSEy
 *
 */
public class AfficheOffset {
	private static final String BATCH_NO_FIELD = "batch_no";
	private static final String TABLE_NAME_FIELD = "table_name";
	private final String batchNo;
	private final String tableName;

	/**
	 * @param batchNo
	 */
	public AfficheOffset(final String tableName, final String batchNo) {
		this.batchNo = batchNo;
		this.tableName = tableName;
	}

	public String batchNo() {
		return (batchNo == null) ? Constants.EMPTY_STRING : batchNo;
	}

	public String tableName() {
		return (tableName == null) ? "" : tableName;
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> res = new HashMap<String, Object>(2);
		if (batchNo != null) {
			res.put(BATCH_NO_FIELD, batchNo);
		}

		if (!Strings.isNullOrEmpty(tableName)) {
			res.put(TABLE_NAME_FIELD, tableName);
		}

		return res;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof AfficheOffset))
			return false;

		AfficheOffset other = (AfficheOffset) obj;
		return Objects.equals(batchNo, other.batchNo) && Objects.equals(tableName, other.tableName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(batchNo, tableName);
	}
}
