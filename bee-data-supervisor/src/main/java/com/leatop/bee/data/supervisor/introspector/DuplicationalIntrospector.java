/**
 * File: DuplicationalIntrospector.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月25日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.introspector;

import java.util.HashMap;
import java.util.Map;

import com.leatop.bee.common.domain.ETCPassList;
import com.leatop.bee.common.domain.ETCTradeList;
import com.leatop.bee.common.domain.ImageList;
import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.data.supervisor.Introspector;
import com.leatop.bee.data.supervisor.exception.DataDuplicationException;
import com.leatop.bee.data.supervisor.exception.DataInvalidException;
import com.leatop.bee.data.supervisor.handler.ExceptionHandlerFactory.ExceptionType;
import com.leatop.bee.data.supervisor.introspector.annotation.Duplicational;
import com.leatop.bee.data.supervisor.validator.ETCPassListValidator;
import com.leatop.bee.data.supervisor.validator.ETCTradeListValidator;
import com.leatop.bee.data.supervisor.validator.ImageListValidator;

/**
 * An implementation of {@link Introspector}, which is used to check the
 * duplicational of the specific target.
 * 
 * @author Dorsey
 *
 */

public class DuplicationalIntrospector implements Introspector {

	@SuppressWarnings("rawtypes")
	private static final Map<Class<? extends TrafficData>, DuplicationalValidator> DUPLICATE_VALIDATOR = new HashMap<Class<? extends TrafficData>, DuplicationalIntrospector.DuplicationalValidator>();

	// registry
	static {
		DUPLICATE_VALIDATOR.put(ETCTradeList.class, new ETCTradeListValidator());
		DUPLICATE_VALIDATOR.put(ETCPassList.class, new ETCPassListValidator());
		DUPLICATE_VALIDATOR.put(ImageList.class, new ImageListValidator());
	}

	@SuppressWarnings("unchecked")
	public <ID> void introspect(final TrafficData<ID> data) throws DataInvalidException {
		final Duplicational duplicational = data.getClass().getAnnotation(Duplicational.class);
		if (duplicational == null || duplicational.allowed()) {
			return;
		}

		ID id = data.getId();
		DuplicationalValidator<ID> validator = DUPLICATE_VALIDATOR.get(data.getClass());
		boolean isDuplicated = validator.validate(id);

		if (isDuplicated) {
			throw new DataDuplicationException(ExceptionType.DUPLICATION.getCode());
		}
	}

	/**
	 * @author Dorsey
	 *
	 * @param <PK>
	 */
	public static interface DuplicationalValidator<PK> {

		/**
		 * Returns true if and only if there exist the same one according to the
		 * primary key.
		 * 
		 * @param id
		 *            the primary key.
		 * @return true if and only if there exist the exactly same one,
		 *         otherwise false.
		 */
		boolean validate(final PK id);
	}
}
