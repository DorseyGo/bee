/**
 * File: NullableIntrospector.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.introspector;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.data.supervisor.Introspector;
import com.leatop.bee.data.supervisor.exception.DataInvalidException;
import com.leatop.bee.data.supervisor.handler.ExceptionHandlerFactory.ExceptionType;
import com.leatop.bee.data.supervisor.introspector.annotation.Nullable;
import com.leatop.bee.data.supervisor.introspector.callback.FieldCallbacks.NullableIntrospection;

/**
 * A simple implementation of {@link Introspector}.
 * <p>
 * It mainly focuse on introspect the null field of the passed in data, and
 * throws {@link DataInvalidException} if null field detected.
 * </p>
 * 
 * @author Dorsey
 *
 */
public class NullableIntrospector implements Introspector {
	
	/**
	 * Empty constructor of {@link NullableIntrospector}.
	 */
	public NullableIntrospector() {
		super();
	}

	public <ID> void introspect(final TrafficData<ID> data) throws DataInvalidException {
		if (data == null) {
			throw new DataInvalidException(ExceptionType.INVALID.getCode());
		}

		NullableIntrospection introspector = new NullableIntrospection(data);
		ReflectionUtils.doWithFields(data.getClass(), introspector, new NullableFieldFilter());
		if (introspector.getException() != null) {
			throw introspector.getException();
		}
	}

	/**
	 * An implementation of {@link FieldFilter}, which is used to filter those
	 * field annotated with {@link Nullable}.
	 * 
	 * @author Dorsey
	 *
	 */
	private static class NullableFieldFilter implements FieldFilter {

		public boolean matches(final Field field) {
			Nullable annot = field.getAnnotation(Nullable.class);

			return field.isAnnotationPresent(Nullable.class) && !annot.nullable();
		}

	}

}
