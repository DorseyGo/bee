/**
 * File: Range.java
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
 * An annotation that defines the value rangement of the specified object.
 * 
 * @author Dorsey
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rangement {
	
	/**
	 * Defines the rangement of the byte type field.
	 * 
	 * @author Dorsey
	 *
	 */
	@Documented
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ByteRangement {
		byte min() default Byte.MIN_VALUE;
		
		byte max() default Byte.MAX_VALUE;
	}
	
	/**
	 * Defines the rangement of the int type field.
	 * 
	 * @author Dorsey
	 *
	 */
	@Documented
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface IntRangement {
		int max() default Integer.MAX_VALUE;
		
		int min() default Integer.MIN_VALUE;
	}
	
	/**
	 * Defines the rangement of the {@link Float} type field.
	 * 
	 * @author Dorsey
	 *
	 */
	@Documented
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FloatRangement {
		float max() default Float.MAX_VALUE;
		float min() default Float.MIN_VALUE;
	}
	
	/**
	 * Defines the rangement of the {@link Double} type field.
	 * 
	 * @author Dorsey
	 *
	 */
	@Documented
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface DoubleRangement {
		double max() default Double.MAX_VALUE;
		
		double min() default Double.MIN_VALUE;
	}
	
	/**
	 * Defines the rangement of the {@link Long} type field.
	 * 
	 * @author Dorsey
	 *
	 */
	@Documented
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface LongRangement {
		long max() default Long.MAX_VALUE;
		long min() default Long.MIN_VALUE;
	}
}
