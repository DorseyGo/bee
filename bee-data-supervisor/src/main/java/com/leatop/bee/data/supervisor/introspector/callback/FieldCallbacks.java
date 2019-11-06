/**
 * File: FieldCallbacks.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.introspector.callback;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.leatop.bee.data.supervisor.exception.DataInvalidException;
import com.leatop.bee.data.supervisor.handler.ExceptionHandlerFactory.ExceptionType;
import com.leatop.bee.data.supervisor.introspector.annotation.Length;
import com.leatop.bee.data.supervisor.introspector.annotation.Nullable;
import com.leatop.bee.data.supervisor.introspector.annotation.Rangement;
import com.leatop.bee.data.supervisor.introspector.annotation.Rangement.ByteRangement;
import com.leatop.bee.data.supervisor.introspector.annotation.Rangement.DoubleRangement;
import com.leatop.bee.data.supervisor.introspector.annotation.Rangement.FloatRangement;
import com.leatop.bee.data.supervisor.introspector.annotation.Rangement.IntRangement;
import com.leatop.bee.data.supervisor.introspector.annotation.Rangement.LongRangement;

/**
 * @author Dorsey
 *
 */
public class FieldCallbacks {

	public static abstract class AbstractFieldCallback implements FieldCallback {

		protected final Object target;
		protected DataInvalidException exception;

		protected AbstractFieldCallback(final Object target) {
			this.target = target;
			this.exception = null;
		}

		/**
		 * @return the exception
		 */
		public DataInvalidException getException() {
			return exception;
		}

		/**
		 * @param exception
		 *            the exception to set
		 */
		public void setException(final DataInvalidException exception) {
			this.exception = exception;
		}

		public void doWith(final Field field)
				throws IllegalArgumentException, IllegalAccessException {
			ReflectionUtils.makeAccessible(field);
			Object val = ReflectionUtils.getField(field, target);

			boolean isValid = validate(field, val);
			if (!isValid) {
				shipWithException();
			}
		}

		/**
		 * Encapsulate with the exception if voilation detected.
		 */
		protected void shipWithException() {
			setException(new DataInvalidException(ExceptionType.INVALID.getCode()));
		}

		/**
		 * Returns true if the validation passed, otherwise false.
		 * 
		 * @param field
		 *            the target.
		 * @param val
		 *            the value of the given field.
		 * @return true if the validation passed, otherwise false.
		 */
		protected abstract boolean validate(final Field field, final Object val);
	}

	/**
	 * Introspector, which is used to introspect whether the value of the target
	 * is null.
	 * 
	 * @author Dorsey
	 *
	 */
	public static class NullableIntrospection extends AbstractFieldCallback {

		public NullableIntrospection(final Object target) {
			super(target);
		}

		@Override
		protected boolean validate(final Field field, final Object val) {
			Nullable nullable = field.getAnnotation(Nullable.class);

			return (!nullable.nullable()) && (val != null);
		}

	}

	/**
	 * Introspect the rangement of the value.
	 * 
	 * @author Dorsey
	 *
	 */
	public static class RangementIntrospection extends AbstractFieldCallback {

		private static final Map<Class<?>, NumberValidator> VALIDATORS = new HashMap<Class<?>, FieldCallbacks.RangementIntrospection.NumberValidator>();
		
		static {
			VALIDATORS.put(Byte.TYPE, new ByteValidator());
			VALIDATORS.put(Integer.TYPE, new IntValidator());
			VALIDATORS.put(Float.TYPE, new FloatValidator());
			VALIDATORS.put(Double.TYPE, new DoubleValidator());
			VALIDATORS.put(Long.TYPE, new LongValidator());
		}

		public RangementIntrospection(final Object target) {
			super(target);
		}

		@Override
		protected boolean validate(final Field field, final Object val) {
			Class<?> fieldType = field.getType();
			NumberValidator validator = VALIDATORS.get(fieldType);
			
			return validator.validate(field, val);
		}

		private static interface NumberValidator {

			boolean validate(final Field field, final Object value);
		}

		private static class ByteValidator implements NumberValidator {

			public boolean validate(final Field field, final Object value) {
				ByteRangement rangement = field.getAnnotation(Rangement.ByteRangement.class);
				Assert.isTrue(rangement != null,
						"The field [" + field + "] is byte, ByteRangement annotation expected");

				byte maxPermitted = rangement.max();
				byte minPermitted = rangement.min();
				byte curValue = Byte.valueOf(value.toString());

				return (curValue >= minPermitted) && (curValue <= maxPermitted);
			}

		}
		
		private static class IntValidator implements NumberValidator {

			public boolean validate(final Field field, final Object value) {
				IntRangement rangement = field.getAnnotation(Rangement.IntRangement.class);
				Assert.isTrue(rangement != null,
						"The field [" + field + "] is integer, IntRangement annotation expected");

				int maxPermitted = rangement.max();
				int minPermitted = rangement.min();
				int curValue = Integer.valueOf(value.toString());

				return (curValue >= minPermitted) && (curValue <= maxPermitted);
			}

		}
		
		private static class FloatValidator implements NumberValidator {

			public boolean validate(final Field field, final Object value) {
				FloatRangement rangement = field.getAnnotation(Rangement.FloatRangement.class);
				Assert.isTrue(rangement != null,
						"The field [" + field + "] is integer, IntRangement annotation expected");

				float maxPermitted = rangement.max();
				float minPermitted = rangement.min();
				float curValue = Float.valueOf(value.toString());

				return (curValue >= minPermitted) && (curValue <= maxPermitted);
			}

		}
		
		private static class DoubleValidator implements NumberValidator {

			public boolean validate(final Field field, final Object value) {
				DoubleRangement rangement = field.getAnnotation(Rangement.DoubleRangement.class);
				Assert.isTrue(rangement != null,
						"The field [" + field + "] is integer, IntRangement annotation expected");

				double maxPermitted = rangement.max();
				double minPermitted = rangement.min();
				double curValue = Float.valueOf(value.toString());

				return (curValue >= minPermitted) && (curValue <= maxPermitted);
			}

		}
		
		private static class LongValidator implements NumberValidator {

			public boolean validate(final Field field, final Object value) {
				LongRangement rangement = field.getAnnotation(Rangement.LongRangement.class);
				Assert.isTrue(rangement != null,
						"The field [" + field + "] is integer, IntRangement annotation expected");

				long maxPermitted = rangement.max();
				long minPermitted = rangement.min();
				long curValue = Long.valueOf(value.toString());

				return (curValue >= minPermitted) && (curValue <= maxPermitted);
			}

		}

	}

	/**
	 * Introspect the length of the value.
	 * 
	 * @author Dorsey
	 *
	 */
	public static class LengthIntrospection extends AbstractFieldCallback {

		public LengthIntrospection(final Object target) {
			super(target);
		}

		@Override
		protected boolean validate(final Field field, final Object val) {
			CharSequence value = (CharSequence) val;
			Length annot = field.getAnnotation(Length.class);
			int maxLength = annot.maxLength();
			int minLength = annot.minLength();

			boolean isValid = (value.length() >= minLength) && (value.length() <= maxLength);
			return isValid;
		}

	}
}
