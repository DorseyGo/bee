/**
 * File: MySQLDialect.java
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
public class MySQLDialect extends GenericDialect {

	public static class Supplier extends SubProtocolDialectSupplier {

		public Supplier() {
			super(MySQLDialect.class.getName(), "mysql", "mariadb");
		}

		@Override
		public Dialect create(final AbstractConfig config) {
			return new MySQLDialect(config);
		}

	}

	public MySQLDialect(final AbstractConfig config) {
		super(config, new Identifier(".", "`"));
	}

	@Override
	protected String getTableNameAndBatchNosSql(final TableId afficheTableFqn) {
		final String header = super.getTableNameAndBatchNosSql(afficheTableFqn);
		final String sql = sqlBuilder().append(header).append(afficheTableFqn)
				.append(" WHERE UPDATEFLAG = 0 ORDER BY BATCHNO DESC LIMIT 0," + maxRowsOfAffiche())
				.toString();

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
