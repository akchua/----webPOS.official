package com.chua.evergrocery.utility.print;

import org.apache.velocity.app.VelocityEngine;

/**
 * @author Kevin Roy K. Chua
 *
 * @version 1.0, Jul 9, 2016
 * @since 1.0, Jul 9, 2016
 *
 */
public interface Printable
{
	/**
	 * 
	 * @param velocityEngine
	 * @param request
	 * @param servletContext
	 * @throws Exception
	 */
	public void print(VelocityEngine velocityEngine) throws Exception;
}
