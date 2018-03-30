package com.chua.evergrocery.rest.handler.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.constants.SystemConstants;
import com.chua.evergrocery.enums.UserType;
import com.chua.evergrocery.rest.handler.ConstantsHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   6 Dec 2017
 */
@Component
public class ConstantsHandlerImpl implements ConstantsHandler {

	@Autowired
	private SystemConstants systemConstants;
	
	@Override
	public String getVersion() {
		return systemConstants.getVersion();
	}

	@Override
	public List<UserType> getUserTypeList() {
		return Stream.of(UserType.values()).collect(Collectors.toList());
	}
}
