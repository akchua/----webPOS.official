package com.chua.evergrocery.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	6 Feb 2017
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface CheckAuthority {

	public int minimumAuthority() default 1;
	
	public int maximumAuthority() default 1;
	
	public String authority() default "";
}
