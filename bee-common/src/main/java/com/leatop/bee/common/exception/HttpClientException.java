/**
 * File: HttpClientException.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.exception;

/**
 * An exception thrown when error detected during processing the http response.
 * 
 * @author Dorsey
 *
 */
public class HttpClientException extends Exception {

	private static final long serialVersionUID = 7091420693900986492L;

	private final int respCode;
	private final int statusCode;

	/**
	 * @param respCode
	 * @param statusCode
	 */
	public HttpClientException(final String message, final int respCode, final int statusCode) {
		super(message);
		this.respCode = respCode;
		this.statusCode = statusCode;
	}

	/**
	 * @return the respCode
	 */
	public int getRespCode() {
		return respCode;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

}
