/**
 * File: ExceptionHandlerFactory.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.handler;

import java.lang.reflect.Constructor;

import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.common.log.LogFacade;
import com.leatop.bee.data.supervisor.ExceptionHandler;

/**
 * @author Dorsey
 *
 */
public class ExceptionHandlerFactory {

	private static final LogFacade LOG_FACADE = LogFacade.getFacade(ExceptionHandlerFactory.class);

	@SuppressWarnings("unchecked")
	public static <T extends ExceptionHandler> T getHandler(final int code) {
		final ExceptionType exceptionType = ExceptionType.valueOf(code);
		Class<? extends ExceptionHandler> handlerType = exceptionType.getHandlerType();
		try {
			Constructor<? extends ExceptionHandler> constor = handlerType.getDeclaredConstructor();
			constor.setAccessible(true);
			return (T) constor.newInstance();
		} catch (Exception e) {
			LOG_FACADE.warn("Failed to instantiate the exception handler class ["
					+ handlerType.getCanonicalName() + "]", e);
			
			return (T) new DeafExceptionHandler();
		}
	}

	/**
	 * Exception type sets.
	 * 
	 * @author Dorsey
	 *
	 */
	public enum ExceptionType {
		INVALID(0, DataInvalidExceptionHandler.class), DUPLICATION(1,
				DataDuplicationExceptionHandler.class), UNKNOWN(-1, DeafExceptionHandler.class);

		/**
		 * The code, which represent the exception type.
		 */
		private final int code;

		/**
		 * The handler type.
		 */
		private Class<? extends ExceptionHandler> handlerType;

		/**
		 * Sole constructor of {@link ExceptionType}, which code specified.
		 * 
		 * @param code
		 *            the code.
		 */
		private ExceptionType(final int code, final Class<? extends ExceptionHandler> handlerType) {
			this.code = code;
			this.handlerType = handlerType;
		}

		/**
		 * @return the code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * Returns the exception type, represented by the given
		 * <code>code</code>. Returns {@link ExceptionType#UNKNOWN} if no
		 * exception type found accordingly.
		 * 
		 * @param code
		 *            the code.
		 * @return the exception type, or UNKNOWN if no appropriate exception
		 *         type found.
		 */
		public static ExceptionType valueOf(final int code) {
			final ExceptionType[] types = ExceptionType.values();
			for (ExceptionType type : types) {
				if (code == type.getCode()) {
					return type;
				}
			}

			return UNKNOWN;
		}

		/**
		 * @return the handlerType
		 */
		public Class<? extends ExceptionHandler> getHandlerType() {
			return handlerType;
		}
	}

	public static class DeafExceptionHandler implements ExceptionHandler {

		public <ID> void handle(final TrafficData<ID> exception) {
			// no action performed in this handler.
		}

	}
}
