/**
 * File: DataInvalidException.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.exception;

/**
 * A simple generalization of {@link Exception}, which is thrown if data is
 * detected as invalid.
 * 
 * <p>
 * A data is detected as invalid, when following condition meet,
 * <ul>
 * <li>Field not permitted to be null, is null</li>
 * <li>Field length exceed</li>
 * <li>Field value exceed</li>
 * </ul>
 * 
 * @author Dorsey
 *
 */
public class DataInvalidException extends Exception {

	private static final long serialVersionUID = 1L;
	private final int code;

	public DataInvalidException(final int code) {
		super();
		this.code = code;
	}

	public DataInvalidException(final int code, final String message) {
		super(message);
		this.code = code;
	}

	public DataInvalidException(final int code, final Throwable cause) {
		super(cause);
		this.code = code;
	}

	public DataInvalidException(final int code, final String message, final Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "code: " + code + " : " + super.toString();
	}
}
