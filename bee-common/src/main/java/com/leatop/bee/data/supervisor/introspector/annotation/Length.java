/**
 * File: Length.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月23日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.introspector.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that defines the length that the value of the specified object
 * should extend to.
 * 
 * <p>
 * This annotation is strongly recommended to be used to the field with type
 * assignable from {@link CharSequence}.
 * </p>
 * 
 * @author Dorsey
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Length {

	/**
	 * Returns the maximum length that the value should extend to. Defaults to
	 * <code>0</code>.
	 * 
	 * @return the maximum length of the value should extend to.
	 */
	int maxLength();

	/**
	 * Returns the minimum length that the value should extend to. Defaults to
	 * <code>0</code>.
	 * 
	 * @return the minimum length of the value should extend to.
	 */
	int minLength() default 0;
}
