package com.chua.evergrocery.rest.handler.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.rest.handler.ActivityLogHandler;
import com.chua.evergrocery.utility.TextWriter;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   24 Apr 2019
 */
@Component
public class ActivityLogHandlerImpl implements ActivityLogHandler {

	@Autowired
	private FileConstants fileConstants;

	@Override
	public void log(String userShortName, String message) {
		final Calendar today = Calendar.getInstance();
		String log = "" + today.getTime().toString() + "  [" + userShortName + "] --- ";
		log += message;
		String fileName = DateFormatter.fileSafeShortFormat(today.getTime()) + ".txt";
		TextWriter.write(log, fileConstants.getActivityLogHome() + fileName, Boolean.TRUE);
	}

	@Override
	public void myLog(String message) {
		this.log(UserContextHolder.getShortName(), message);
	}
}
