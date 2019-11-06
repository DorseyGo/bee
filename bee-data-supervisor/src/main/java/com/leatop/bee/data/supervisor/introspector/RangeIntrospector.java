/**
 * File: RangeIntrospector.java
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
import com.leatop.bee.data.supervisor.introspector.annotation.Rangement;
import com.leatop.bee.data.supervisor.introspector.callback.FieldCallbacks.RangementIntrospection;

/**
 * @author Dorsey
 *
 */
public class RangeIntrospector implements Introspector {

	/**
	 * Empty constructor of {@link RangeIntrospector}.
	 */
	public RangeIntrospector() {
		super();
	}

	public <ID> void introspect(final TrafficData<ID> data) throws DataInvalidException {
		RangementIntrospection introspector = new RangementIntrospection(data);
		ReflectionUtils.doWithFields(data.getClass(), introspector, new RangeFieldFilter());

		// if failed, then throw the exception.
		if (introspector.getException() != null) {
			throw introspector.getException();
		}
	}

	private static class RangeFieldFilter implements FieldFilter {

		public boolean matches(final Field field) {
			boolean matches = true;
			matches = matches && (field.isAnnotationPresent(Rangement.ByteRangement.class)
					|| field.isAnnotationPresent(Rangement.IntRangement.class)
					|| field.isAnnotationPresent(Rangement.FloatRangement.class)
					|| field.isAnnotationPresent(Rangement.DoubleRangement.class)
					|| field.isAnnotationPresent(Rangement.LongRangement.class));

			return (matches);
		}

	}

}
