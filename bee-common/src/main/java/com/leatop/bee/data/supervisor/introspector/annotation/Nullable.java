/**
 * File: Nullable.java
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
 * An annotation that defines whether the value of the object it annotated
 * permits to be null or not.
 * 
 * @author Dorsey
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Nullable {

	/**
	 * Returns true if the object it annotated permits null, otherwise false.
	 * 
	 * @return true if the object it annotated permits null, otherwise false.
	 */
	boolean nullable() default true;
}
