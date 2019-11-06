/**
 * File: AfficheCriteria.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月12日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source.querier;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;

/**
 * @author DORSEy
 *
 */
public class AfficheCriteria {

	private static final Logger LOG = LoggerFactory.getLogger(AfficheCriteria.class);

	/**
	 * @param tableName
	 * @param batchNo
	 */
	public AfficheCriteria() {
		// empty for initialization
	}

	/**
	 * @param stmt
	 * @param afficheTableQuerier
	 */
	public void setQueryParameters(final PreparedStatement stmt,
			final AfficheCriteriaValues criteria) throws SQLException {
		stmt.setString(1, criteria.batchNo());

		LOG.debug("Executing prepared statement on table: {}, and batchNo: {}",
				criteria.tableName(), criteria.batchNo());
	}

	/**
	 * @param sqlBuilder
	 */
	public void whereClause(final SQLBuilder sqlBuilder) {
		sqlBuilder.append(" WHERE BATCHNO = ?");
	}

	// ~~~ enclosure classes
	// =================================================================
	/**
	 * Defines the basic unit for Affiche way to retrieve data.
	 * 
	 * @author DORSEy
	 *
	 */
	public interface AfficheCriteriaValues {

		/**
		 * Returns the table name.
		 * 
		 * @return
		 * @throws SQLException
		 */
		String tableName() throws SQLException;

		/**
		 * Returns the batch number.
		 * 
		 * @return
		 * @throws SQLException
		 */
		String batchNo() throws SQLException;

		/**
		 * Returns the last batch number.
		 * 
		 * @return
		 * @throws SQLException
		 */
		String lastBatchNo() throws SQLException;
	}

}
