/**
 * File: ErrorMessage.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.client.domain;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Dorsey
 *
 */
public class ErrorMessage {

	private int errorCode;
	private String message;

	public ErrorMessage(@JsonProperty("statusCode") final int errorCode,
			@JsonProperty("message") final String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	@JsonProperty("statusCode")
	public int getErrorCode() {
		return errorCode;
	}

	@JsonProperty("statusCode")
	public void setErrorCode(final int error_code) {
		this.errorCode = error_code;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(final String message) {
		this.message = message;
	}
}
