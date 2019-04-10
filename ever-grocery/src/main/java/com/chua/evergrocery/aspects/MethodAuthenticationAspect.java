package com.chua.evergrocery.aspects;

import javax.ws.rs.NotAuthorizedException;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.annotations.CheckAuthority;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	6 Feb 2017
 */
@Aspect
public class MethodAuthenticationAspect {

	/**
	 * Checks authority of all methods annotated with @CheckAuthority.
	 * The value of authority will supersede the values of minimumAuthority and maximumAuthority.
	 * The value of authority must be a regex
	 * @param checkAuthority
	 */
	@Before("@annotation(checkAuthority) && execution(* *(..))")
	public void checkUserAuthority(CheckAuthority checkAuthority) {
		final Integer userAuthority = UserContextHolder.getUser().getUserType().getAuthority();
		if(checkAuthority.authority().isEmpty()) {
			if(userAuthority > checkAuthority.minimumAuthority() || userAuthority < checkAuthority.maximumAuthority()) {
				throw new NotAuthorizedException("User is not authenticated.");
			}
		} else {
			if(!userAuthority.toString().matches(checkAuthority.authority())) {
				throw new NotAuthorizedException("User is not authenticated.");
			}
		}
	}
}
