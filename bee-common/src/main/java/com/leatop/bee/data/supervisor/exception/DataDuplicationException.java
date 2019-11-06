/**
 * File: DataDuplicationException.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.exception;

/**
 * A simple generalization of {@link DataInvalidException}, which thrown if data
 * is detected duplicated.
 * 
 * @author Dorsey
 *
 */
public class DataDuplicationException extends DataInvalidException {

	private static final long serialVersionUID = 1L;

	public DataDuplicationException(final int code) {
		super(code);
	}

	public DataDuplicationException(final int code, final String message) {
		super(code, message);
	}

	public DataDuplicationException(final int code, final Throwable cause) {
		super(code, cause);
	}

	public DataDuplicationException(final int code, final String message, final Throwable cause) {
		super(code, message, cause);
	}
}
