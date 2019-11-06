package com.leatop.bee.data.weaver.connector.jdbc.domain;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ColumnConverter {

	/**
	 * Get the column's value from the row at the current position in the result
	 * set, and convert it to a value that should be included in the corresponding
	 * {@link Field} in the {@link Struct} for the row.
	 *
	 * @param resultSet the result set; never null
	 * @return the value of the {@link Field} as converted from the column value
	 * @throws SQLException if there is an error with the database connection
	 * @throws IOException  if there is an error accessing a streaming value from
	 *                      the result set
	 */
	Object convert(ResultSet resultSet) throws SQLException, IOException;
}