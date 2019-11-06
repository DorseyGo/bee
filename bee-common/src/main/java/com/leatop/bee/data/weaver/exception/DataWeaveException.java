/**
 * File: DataWeaveException.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.exception;

/**
 * A simple generalization of {@link Exception}, which is thrown if data failed
 * to weave in or out.
 * 
 * @author Dorsey
 *
 */
public class DataWeaveException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataWeaveException() {
		super();
	}

	public DataWeaveException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DataWeaveException(final String message) {
		super(message);
	}

	public DataWeaveException(final Throwable cause) {
		super(cause);
	}

}
