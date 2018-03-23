package com.chua.evergrocery.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
	
	public static Date getOrderCutoffDate() {
		return new GregorianCalendar(2018, Calendar.MARCH, 20).getTime();
	}
	
	public static boolean isSameDay(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
		                  cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}
}
