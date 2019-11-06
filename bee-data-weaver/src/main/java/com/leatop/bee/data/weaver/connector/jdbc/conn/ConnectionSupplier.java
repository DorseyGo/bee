/**
 * File: ConnectionSupplier.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.conn;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Dorsey
 *
 */
public interface ConnectionSupplier extends AutoCloseable {
	
	/**
	 * Returns a connection.
	 * 
	 * @return
	 * @throws SQLException if problem detected when getting connection.
	 */
	Connection getConnection() throws SQLException;
	
	/**
	 * Close this connection supplier.
	 */
	@Override
	void close();
	
	/**
	 * Returns true if the specified connection <tt>conn</tt> is valid, otherwise false.
	 * 
	 * @param conn the connection to be tested.
	 * @param timeout the time in seconds wait for the database operation.
	 * @return true if the connection valid, otherwise false.
	 * @throws SQLException if problem detected when getting connection.
	 */
	boolean isConnectionValid(final Connection conn, final int timeout) throws SQLException;
	
	default String identifier() {
		return toString();
	}
}
