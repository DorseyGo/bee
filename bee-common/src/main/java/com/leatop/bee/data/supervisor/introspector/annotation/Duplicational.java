/**
 * File: Duplicational.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月25日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.introspector.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation, which declares whether the target it annotated allowed to be duplicated.
 * 
 * @author Dorsey
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Duplicational {
	
	/**
	 * Returns true if the target it annotated allowed duplication, otherwise false.
	 * 
	 * @return true if the target it annotated allowed duplication, otherwise false.
	 */
	boolean allowed() default false;
}
