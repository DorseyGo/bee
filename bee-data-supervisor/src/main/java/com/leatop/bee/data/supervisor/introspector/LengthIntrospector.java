/**
 * File: LengthIntrospector.java
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
import com.leatop.bee.data.supervisor.introspector.annotation.Length;
import com.leatop.bee.data.supervisor.introspector.callback.FieldCallbacks.LengthIntrospection;

/**
 * An implementation of {@link Introspector}, which is used to give an
 * introspection on the length of the value.
 * 
 * <p>
 * <strong>This should only apply to String field.</strong>
 * </p>
 * 
 * @author Dorsey
 *
 */
public class LengthIntrospector implements Introspector {

	public <ID> void introspect(final TrafficData<ID> data) throws DataInvalidException {
		LengthIntrospection introspection = new LengthIntrospection(data);
		ReflectionUtils.doWithFields(data.getClass(), introspection, new LengthFieldFilter());

		if (introspection.getException() != null) {
			throw introspection.getException();
		}
	}

	private static class LengthFieldFilter implements FieldFilter {

		public boolean matches(final Field field) {
			Length annot = field.getAnnotation(Length.class);
			Class<?> type = field.getType();

			return (annot != null) && (CharSequence.class.isAssignableFrom(type));
		}
	}
}
