package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.enums.UserType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   6 Dec 2017
 */
public interface ConstantsHandler {

	String getVersion();
	
	List<UserType> getUserTypeList();
}
