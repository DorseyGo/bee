/**
 * File: OracleDialect.java
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
public class OracleDialect extends GenericDialect {

	public static class Supplier extends SubProtocolDialectSupplier {

		public Supplier() {
			super(OracleDialect.class.getName(), "oracle");
		}

		@Override
		public Dialect create(final AbstractConfig config) {
			return new OracleDialect(config);
		}

	}

	public OracleDialect(final AbstractConfig config) {
		super(config, new Identifier(".", "\"", "\""));
	}

	@Override
	protected String currentTimestampQuery() {
		return "SELECT CURRENT_TIMESTAMP FROM dual";
	}

	@Override
	protected String verifyConnectionQuery() {
		return "SELECT 1 FROM dual";
	}

	@Override
	protected String getTableNameAndBatchNosSql(final TableId afficheTableFqn) {
		final String sql = sqlBuilder().append(super.getTableNameAndBatchNosSql(afficheTableFqn))
				.append(" ( SELECT * FROM ").append(afficheTableFqn)
				.append(" WHERE UPDATEFLAG = 0 ORDER BY BATCHNO DESC)")
				.append(" WHERE ROWNUM <= " + maxRowsOfAffiche()).toString();

		return sql;
	}

	@Override
	protected String getInsertIntoAfficheTabSql(final String afficheTableName) {
		final String sql = sqlBuilder().append("INSERT INTO ").append(afficheTableName).append(
				"(ID, SYSTEMID, TABLENAME, BATCHNO, CREATETIME, RECORDNUM, UPDATEFLAG, UPDATETIME) VALUES(SEQ_"
						+ afficheTableName + ".nextval, ?, ?, ?, ?, ?, ?, ?)")
				.toString();

		return sql;
	}
}
