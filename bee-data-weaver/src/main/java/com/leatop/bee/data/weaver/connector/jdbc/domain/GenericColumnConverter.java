/**
 * File: GenericColumnConverter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月10日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Types;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.connector.jdbc.source.JDBCSourceConnectorConfig.NumericMapping;
import com.leatop.bee.data.weaver.connector.utils.DateTimeUtils;

public class GenericColumnConverter {

	private static final Logger log = LoggerFactory.getLogger(GenericColumnConverter.class);

	protected static final int NUMERIC_TYPE_SCALE_LOW = -84;
	protected static final int NUMERIC_TYPE_SCALE_HIGH = 127;
	protected static final int NUMERIC_TYPE_SCALE_UNSET = -127;

	public ColumnConverter createColumnConverter(final ColumnMapping mapping,
			final JDBCDriverInfo jdbcDriverInfo, final TimeZone timeZone,
			final NumericMapping mapNumerics) {
		return columnConverterFor(mapping, mapping.columnDefn(), mapping.columnNumber(),
				jdbcDriverInfo.jdbcVersionAtLeast(4, 0), timeZone, mapNumerics);
	}

	protected ColumnConverter columnConverterFor(final ColumnMapping mapping, final Column defn,
			final int col, final boolean isJdbc4, final TimeZone timeZone,
			final NumericMapping mapNumerics) {
		return new ColumnConverter() {
			@Override
			public Object convert(final ResultSet rs) throws SQLException, IOException {
				switch (mapping.columnDefn().type()) {

				case Types.BOOLEAN: {
					return rs.getBoolean(col);
				}

				case Types.BIT: {
					/**
					 * BIT should be either 0 or 1. TODO: Postgres handles this
					 * differently, returning a string "t" or "f". See the
					 * elasticsearch-jdbc plugin for an example of how this is
					 * handled
					 */
					return rs.getByte(col);
				}

				// 8 bits int
				case Types.TINYINT: {
					if (defn.isSignedNumber()) {
						return rs.getByte(col);
					} else {
						return rs.getShort(col);
					}
				}

				// 16 bits int
				case Types.SMALLINT: {
					if (defn.isSignedNumber()) {
						return rs.getShort(col);
					} else {
						return rs.getInt(col);
					}
				}

				// 32 bits int
				case Types.INTEGER: {
					if (defn.isSignedNumber()) {
						return rs.getInt(col);
					} else {
						return rs.getLong(col);
					}
				}

				// 64 bits int
				case Types.BIGINT: {
					return rs.getLong(col);
				}

				// REAL is a single precision floating point value, i.e. a Java
				// float
				case Types.REAL: {
					return rs.getFloat(col);
				}

				// FLOAT is, confusingly, double precision and effectively the
				// same as DOUBLE.
				// See REAL
				// for single precision
				case Types.FLOAT:
				case Types.DOUBLE: {
					return rs.getDouble(col);
				}

				case Types.NUMERIC:
					if (mapNumerics == NumericMapping.PRECISION_ONLY) {
						int precision = defn.precision();
						int scale = defn.scale();
						log.trace("NUMERIC with precision: '{}' and scale: '{}'", precision, scale);
						if (scale == 0 && precision < 19) { // integer
							if (precision > 9) {
								return rs.getLong(col);
							} else if (precision > 4) {
								return rs.getInt(col);
							} else if (precision > 2) {
								return rs.getShort(col);
							} else {
								return rs.getByte(col);
							}
						}
					} else if (mapNumerics == NumericMapping.BEST_FIT) {
						int precision = defn.precision();
						int scale = defn.scale();
						log.trace("NUMERIC with precision: '{}' and scale: '{}'", precision, scale);
						if (precision < 19) { // fits in primitive data types.
							if (scale < 1 && scale >= NUMERIC_TYPE_SCALE_LOW) { // integer
								if (precision > 9) {
									return rs.getLong(col);
								} else if (precision > 4) {
									return rs.getInt(col);
								} else if (precision > 2) {
									return rs.getShort(col);
								} else {
									return rs.getByte(col);
								}
							} else if (scale > 0) { // floating point - use
													// double in all cases
								return rs.getDouble(col);
							}
						}
					}
					// fallthrough

				case Types.DECIMAL: {

					// return rs.getDouble(col);

					final int scale = decimalScale(defn);
					if (scale > 0) {
						return rs.getDouble(col);
					} else {
						return rs.getLong(col);
					}
				}

				case Types.CHAR:
				case Types.VARCHAR:
				case Types.LONGVARCHAR: {
					return rs.getString(col);
				}

				case Types.NCHAR:
				case Types.NVARCHAR:
				case Types.LONGNVARCHAR: {
					return rs.getNString(col);
				}

				// Binary == fixed, VARBINARY and LONGVARBINARY == bytes
				case Types.BINARY:
				case Types.VARBINARY:
				case Types.LONGVARBINARY: {
					return rs.getBytes(col);
				}

				// Date is day + month + year
				case Types.DATE: {
					return rs.getDate(col, DateTimeUtils.getTimeZoneCalendar(timeZone));
				}

				// Time is a time of day -- hour, minute, seconds, nanoseconds
				case Types.TIME: {
					return rs.getTime(col, DateTimeUtils.getTimeZoneCalendar(timeZone));
				}

				// Timestamp is a date + time
				case Types.TIMESTAMP: {
					return rs.getTimestamp(col, DateTimeUtils.getTimeZoneCalendar(timeZone));
				}

				// Datalink is basically a URL -> string
				case Types.DATALINK: {
					URL url = rs.getURL(col);
					return (url != null ? url.toString() : null);
				}

				// BLOB == fixed
				case Types.BLOB: {
					Blob blob = rs.getBlob(col);
					if (blob == null) {
						return null;
					} else {
						try {
							if (blob.length() > Integer.MAX_VALUE) {
								throw new IOException(
										"Can't process BLOBs longer than " + Integer.MAX_VALUE);
							}
							return blob.getBytes(1, (int) blob.length());
						} finally {
							if (isJdbc4) {
								blob.free();
							}
						}
					}
				}
				case Types.CLOB: {
					Clob clob = rs.getClob(col);
					if (clob == null) {
						return null;
					} else {
						try {
							if (clob.length() > Integer.MAX_VALUE) {
								throw new IOException(
										"Can't process CLOBs longer than " + Integer.MAX_VALUE);
							}
							return clob.getSubString(1, (int) clob.length());
						} finally {
							if (isJdbc4) {
								clob.free();
							}
						}
					}
				}
				case Types.NCLOB: {
					Clob clob = rs.getNClob(col);
					if (clob == null) {
						return null;
					} else {
						try {
							if (clob.length() > Integer.MAX_VALUE) {
								throw new IOException(
										"Can't process NCLOBs longer than " + Integer.MAX_VALUE);
							}
							return clob.getSubString(1, (int) clob.length());
						} finally {
							if (isJdbc4) {
								clob.free();
							}
						}
					}
				}

				// XML -> string
				case Types.SQLXML: {
					SQLXML xml = rs.getSQLXML(col);
					return xml != null ? xml.getString() : null;
				}

				case Types.NULL:
				case Types.ARRAY:
				case Types.JAVA_OBJECT:
				case Types.OTHER:
				case Types.DISTINCT:
				case Types.STRUCT:
				case Types.REF:
				case Types.ROWID:
				default: {
					// These are not currently supported, but we don't want to
					// log something for
					// every single
					// record we translate. There will already be errors logged
					// for the schema
					// translation
					break;
				}
				}
				return null;
			}
		};

	}

	protected int decimalScale(final Column defn) {
		return defn.scale() == NUMERIC_TYPE_SCALE_UNSET ? NUMERIC_TYPE_SCALE_HIGH : defn.scale();
	}

}
