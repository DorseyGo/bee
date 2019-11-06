/**
 * File: ReqHeaderInfo.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.domain;

import java.util.Objects;

/**
 * @author Dorsey
 *
 */
public class ReqHeaderInfo {

	private final String md5;
	private final boolean gzip;
	private final String authCode;

	public ReqHeaderInfo(final String md5, final boolean gzip, final String authCode) {
		this.md5 = md5;
		this.gzip = gzip;
		this.authCode = authCode;
	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * @return the gzip
	 */
	public boolean isGzip() {
		return gzip;
	}

	/**
	 * @return the authCode
	 */
	public String getAuthCode() {
		return authCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(md5, gzip, authCode);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof ReqHeaderInfo))
			return false;

		ReqHeaderInfo other = (ReqHeaderInfo) obj;
		return (gzip == other.gzip) && Objects.equals(md5, other.md5)
				&& Objects.equals(authCode, other.authCode);
	}

}
