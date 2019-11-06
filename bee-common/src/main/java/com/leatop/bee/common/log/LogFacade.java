/**
 * File: LogFacade.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facade, which is used to log down the message.
 * 
 * <p>
 * Underleath, it is using <code>logback</code> for implementation.
 * </p>
 * 
 * @author Dorsey
 *
 */
public class LogFacade {

	// ~~~~~ fields
	// ==================================================================================
	private final Logger log;

	// ~~~~~ constructors
	// ==================================================================================

	/**
	 * Sole constructor of {@link LogFacade}, with the target specified.
	 * 
	 * @param target,
	 *            on which object that the log should be performed on.
	 */
	private LogFacade(final Class<?> target) {
		this.log = LoggerFactory.getLogger(target);
	}

	// ~~~~~ methods
	// ==================================================================================

	/**
	 * Returns the instance of current object, with <code>target</code>
	 * specified.
	 * 
	 * @param target
	 *            the target, on which the message log down.
	 * @return the instance.
	 */
	public static LogFacade getFacade(final Class<?> target) {
		return new LogFacade(target);
	}
	
	/**
	 * Log a message at the ERROR level
	 * 
	 * @param message
	 *            the message string to be logged
	 */
	public void error(final String message) {
		this.log.error(message);
	}
	
	/**
	 * Log an exception (throwable) at the ERROR level with an accompanying
	 * message
	 * 
	 * @param message
	 *            the message accompanying the exception
	 * @param cause
	 *            the exception (throwable) to log
	 */
	public void error(final String message, final Throwable cause) {
		this.log.error(message, cause);
	}
	
	/**
	 * Log a message at the ERROR level according to the specified format and
	 * arguments.
	 * 
	 * This form avoids superfluous string concatenation when the logger is
	 * disabled for the INFO level. However, this variant incurs the hidden (and
	 * relatively small) cost of creating an Object[] before invoking the
	 * method, even if this logger is disabled for INFO. The variants taking one
	 * and two arguments exist solely in order to avoid this hidden cost.
	 * 
	 * @param format
	 *            the format string
	 * @param args
	 *            a list of 3 or more arguments
	 */
	public void error(final String format, final Object... args) {
		this.log.error(format, args);
	}

	/**
	 * Log an exception (throwable) at the WARN level with an accompanying
	 * message
	 * 
	 * @param message
	 *            the message accompanying the exception
	 * @param cause
	 *            the exception (throwable) to log
	 */
	public void warn(final String message, final Throwable cause) {
		this.log.warn(message, cause);
	}
	
	/**
	 * Log a message at the WARN level
	 * 
	 * @param message
	 *            the message string to be logged
	 */
	public void warn(final String message) {
		this.log.warn(message);
	}
	
	/**
	 * Log a message at the WARN level according to the specified format and
	 * arguments.
	 * 
	 * This form avoids superfluous string concatenation when the logger is
	 * disabled for the INFO level. However, this variant incurs the hidden (and
	 * relatively small) cost of creating an Object[] before invoking the
	 * method, even if this logger is disabled for INFO. The variants taking one
	 * and two arguments exist solely in order to avoid this hidden cost.
	 * 
	 * @param format
	 *            the format string
	 * @param args
	 *            a list of 3 or more arguments
	 */
	public void warn(final String format, final Object... args) {
		this.log.warn(format, args);
	}

	/**
	 * Log an exception (throwable) at the INFO level with an accompanying
	 * message
	 * 
	 * @param message
	 *            the message accompanying the exception
	 * @param cause
	 *            the exception (throwable) to log
	 */
	public void info(final String message, final Throwable cause) {
		this.log.info(message, cause);
	}

	/**
	 * Log a message at the INFO level
	 * 
	 * @param message
	 *            the message string to be logged
	 */
	public void info(final String message) {
		this.log.info(message);
	}

	/**
	 * Log a message at the INFO level according to the specified format and
	 * arguments.
	 * 
	 * This form avoids superfluous string concatenation when the logger is
	 * disabled for the INFO level. However, this variant incurs the hidden (and
	 * relatively small) cost of creating an Object[] before invoking the
	 * method, even if this logger is disabled for INFO. The variants taking one
	 * and two arguments exist solely in order to avoid this hidden cost.
	 * 
	 * @param format
	 *            the format string
	 * @param args
	 *            a list of 3 or more arguments
	 */
	public void info(final String format, final Object... args) {
		this.log.info(format, args);
	}

	/**
	 * Log an exception (throwable) at the DEBUG level with an accompanying
	 * message
	 * 
	 * @param message
	 *            the message accompanying the exception
	 * @param cause
	 *            the exception (throwable) to log
	 */
	public void debug(final String message, final Throwable cause) {
		this.log.debug(message, cause);
	}

	/**
	 * Log a message at the DEBUG level
	 * 
	 * @param message
	 *            the message string to be logged
	 */
	public void debug(final String message) {
		this.log.debug(message);
	}
	
	/**
	 * Log a message at the DEBUG level according to the specified format and
	 * arguments.
	 * 
	 * This form avoids superfluous string concatenation when the logger is
	 * disabled for the INFO level. However, this variant incurs the hidden (and
	 * relatively small) cost of creating an Object[] before invoking the
	 * method, even if this logger is disabled for INFO. The variants taking one
	 * and two arguments exist solely in order to avoid this hidden cost.
	 * 
	 * @param format
	 *            the format string
	 * @param args
	 *            a list of 3 or more arguments
	 */
	public void debug(final String format, final Object... args) {
		this.log.debug(format, args);
	}
	
	/**
	 * Log a message at the TRACE level
	 * 
	 * @param message
	 *            the message string to be logged
	 */
	public void trace(final String message) {
		this.log.trace(message);
	}

	/**
	 * Log a message at the TRACE level according to the specified format and
	 * arguments.
	 * 
	 * This form avoids superfluous string concatenation when the logger is
	 * disabled for the TRACE level. However, this variant incurs the hidden (and
	 * relatively small) cost of creating an Object[] before invoking the
	 * method, even if this logger is disabled for TRACE. The variants taking one
	 * and two arguments exist solely in order to avoid this hidden cost.
	 * 
	 * @param format
	 *            the format string
	 * @param args
	 *            a list of 3 or more arguments
	 */
	public void trace(final String format, final Object... args) {
		this.log.trace(format, args);
	}
}
