/**
 * File: SQLServerDialect.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月10日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.conn.dialect;

import org.apache.kafka.common.config.AbstractConfig;

import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.DialectSupplier.SubProtocolDialectSupplier;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.utils.Identifier;

/**
 * @author Dorsey
 *
 */
public class SQLServerDialect extends GenericDialect {

	public static class Supplier extends SubProtocolDialectSupplier {

		public Supplier() {
			super(SQLServerDialect.class.getName(), "microsoft:sqlserver", "sqlserver",
					"jtds:sqlserver");
		}

		@Override
		public Dialect create(final AbstractConfig config) {
			return new SQLServerDialect(config);
		}

	}

	/**
	 * @param config
	 */
	public SQLServerDialect(final AbstractConfig config) {
		super(config, new Identifier(".", "[", "]"));
	}

	@Override
	protected boolean useCatelog() {
		return true;
	}

	@Override
	protected String getTableNameAndBatchNosSql(final TableId afficheTableFqn) {
		final String sql = sqlBuilder().append("SELECT TOP " + maxRowsOfAffiche())
				.append(" TABLENAME, BATCHNO FROM ").append(afficheTableFqn)
				.append(" WHERE UPDATEFLAG = 0 ORDER BY BATCHNO DESC").toString();

		return sql;
	}

	@Override
	protected String getInsertIntoAfficheTabSql(final String afficheTableName) {
		final String sql = sqlBuilder().append("INSERT INTO ").append(afficheTableName).append(
				"(SYSTEMID, TABLENAME, BATCHNO, CREATETIME, RECORDNUM, UPDATEFLAG, UPDATETIME) VALUES(?, ?, ?, ?, ?, ?, ?)")
				.toString();

		return sql;
	}
}
