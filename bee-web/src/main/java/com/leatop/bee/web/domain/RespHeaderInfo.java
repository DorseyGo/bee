/**
 * File: RespHeaderInfo.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月29日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.domain;

/**
 * @author Dorsey
 *
 */
public class RespHeaderInfo {

	private static final String DEFAULT_CONTENT_DISPOSITION = "attachment;filename=";
	private static final String DEFAULT_CONTENT_ENCODING = "gzip";
	private String md5;
	private String contentDisposition;
	private final String contentEncoding;
	private int contentLength;
	private final int statusCode;

	/**
	 * Constructor of {@link RespHeaderInfo}, with status code specified.
	 * 
	 * @param statusCode
	 */
	public RespHeaderInfo(final int statusCode) {
		this.statusCode = statusCode;
		this.contentEncoding = DEFAULT_CONTENT_ENCODING;
	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * @param md5
	 *            the md5 to set
	 */
	public RespHeaderInfo setMd5(final String md5) {
		this.md5 = md5;
		return this;
	}

	/**
	 * @return the contentDisposition
	 */
	public String getContentDisposition() {
		return contentDisposition;
	}

	/**
	 * @param contentDisposition
	 *            the contentDisposition to set
	 */
	public void setContentDisposition(final String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}
	
	public RespHeaderInfo setContentDispositionWithFilename(final String filename) {
		setContentDisposition(DEFAULT_CONTENT_DISPOSITION + filename);
		return this;
	}

	/**
	 * @return the contentEncoding
	 */
	public String getContentEncoding() {
		return contentEncoding;
	}

	/**
	 * @return the contentLength
	 */
	public int getContentLength() {
		return contentLength;
	}

	/**
	 * @param contentLength
	 *            the contentLength to set
	 */
	public RespHeaderInfo setContentLength(final int contentLength) {
		this.contentLength = contentLength;
		return this;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

}
