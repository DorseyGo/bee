/**
 * File: JDBCDriverInfo.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月9日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.jdbc.domain;

/**
 * Container which contains the basic information on JDBC driver.
 * 
 * @author Dorsey
 *
 */
public class JDBCDriverInfo {

	// ~~~ fields
	// =========================================================
	private final int majorVersion;
	private final int minorVersion;
	private final String driverName;
	private final String productName;
	private final String productVersion;

	// ~~~ constructors
	// =========================================================
	public JDBCDriverInfo(final int majorVersion, final int minorVersion, final String driverName,
			final String productName, final String productVersion) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.driverName = driverName;
		this.productName = productName;
		this.productVersion = productVersion;
	}

	// ~~~ methods
	// =========================================================
	public int majorVersion() {
		return majorVersion;
	}

	public int minorVersion() {
		return minorVersion;
	}

	public String driverName() {
		return driverName;
	}

	public String productName() {
		return productName;
	}

	public String productVersion() {
		return productVersion;
	}

	/**
	 * Returns true if current jdbc driver supports the specified major version
	 * and minor version, otherwise false.
	 * 
	 * @param majorVersion
	 *            the major version.
	 * @param minorVersion
	 *            the minor version.
	 * @return true if it supports the specified major and minor version,
	 *         otherwise false.
	 */
	public boolean supports(final int majorVersion, final int minorVersion) {
		if (majorVersion() > majorVersion) {
			return true;
		}

		if (majorVersion() == majorVersion && minorVersion() >= minorVersion) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return "JDBCDriverInfo [majorVersion=" + majorVersion + ", minorVersion=" + minorVersion
				+ ", driverName=" + driverName + ", productName=" + productName
				+ ", productVersion=" + productVersion + "]";
	}
	
	

	  /**
	   * Determine if the JDBC driver supports at least the specified major and minor version of the
	   * JDBC specifications. This can be used to determine whether or not to call JDBC methods.
	   *
	   * @param jdbcMajorVersion the required major version of the JDBC specification
	   * @param jdbcMinorVersion the required minor version of the JDBC specification
	   * @return true if the driver supports at least the specified version of the JDBC specification,
	   *     or false if the driver supports an older version of the JDBC specification
	   */
	public boolean jdbcVersionAtLeast(final int majorVersion, final int minorVersion) {
		if (this.majorVersion() > majorVersion) {
			return true;
		}
		if (majorVersion == this.majorVersion() && this.minorVersion() >= minorVersion) {
			return true;
		}
		return false;
	}

}
