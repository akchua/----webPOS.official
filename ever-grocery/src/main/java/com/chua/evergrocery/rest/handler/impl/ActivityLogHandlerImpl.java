package com.chua.evergrocery.rest.handler.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.rest.handler.ActivityLogHandler;
import com.chua.evergrocery.utility.TextWriter;

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
	public void log(String userShortName, String message, String ip) {
		List<String> allowedIp = new ArrayList<String>();
		allowedIp.add("n/a");
		allowedIp.add("0:0:0:0:0:0:0:1");
		for(int i = 2; i < 25; i++) allowedIp.add("192.168.0." + i);
		
		if(allowedIp.contains(ip)) {
			final Calendar today = Calendar.getInstance();
			String log = "" + today.getTime().toString() + "  [" + userShortName + "] (" + ip + ") --- ";
			log += message;
			String fileName = "activity_log.txt";
			TextWriter.write(log, fileConstants.getActivityLogHome() + fileName, Boolean.TRUE);
		}
	}

	@Override
	public void myLog(String message, String ip) {
		this.log(UserContextHolder.getShortName(), message, ip);
	}
}
