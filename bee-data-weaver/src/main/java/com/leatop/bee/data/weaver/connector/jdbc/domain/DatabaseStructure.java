/**
 * File: DatabaseStructure.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月10日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

import com.leatop.bee.data.weaver.connector.jdbc.conn.dialect.Dialect;

/**
 * @author Dorsey
 *
 */
public class DatabaseStructure {

	private final Dialect dialect;
	private final Tables tables;

	public DatabaseStructure(final Dialect dialect) {
		this.dialect = dialect;
		this.tables = new Tables(dialect);
	}
}
