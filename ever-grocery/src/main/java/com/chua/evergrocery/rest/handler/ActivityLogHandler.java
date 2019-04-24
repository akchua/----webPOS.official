package com.chua.evergrocery.rest.handler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   24 Apr 2019
 */
public interface ActivityLogHandler {

	void log(String userShortName, String message);
	
	void myLog(String message);
}
