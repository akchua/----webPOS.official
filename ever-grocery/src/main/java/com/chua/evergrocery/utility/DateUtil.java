package com.chua.evergrocery.utility;

import java.util.Calendar;
import java.util.Date;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	9 Feb 2017
 */
public class DateUtil {

	private static final String[] NAME_OF_MONTH = {"January", "February", "March", "April", "May", "June", "July",
	        "August", "September", "October", "November", "December"};
	
	public static String getNameOfMonth(Calendar calendar) {
		return NAME_OF_MONTH[calendar.get(Calendar.MONTH)];
	}
	
	public static Date getDefaultDate() {
		return new Date(0);
	}
	
	public static Long getDefaultDateInMillis() {
		return getDefaultDate().getTime();
	}
}
