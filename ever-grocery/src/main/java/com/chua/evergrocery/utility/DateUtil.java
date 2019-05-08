package com.chua.evergrocery.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.Days;
import org.joda.time.LocalDate;

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
	
	public static String[] getMonthNamesArray() {
		return NAME_OF_MONTH;
	}
	
	public static int getMonthId(Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return (cal.get(Calendar.YEAR) * 12) + cal.get(Calendar.MONTH);
	}
	
	public static Date monthIdToDate(int monthId) {
		Calendar cal = Calendar.getInstance();
		cal.set(monthId / 12, monthId % 12, 1, 0, 0, 0);
		return cal.getTime();
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
	
	public static boolean isToday(Date date) {
		return DateUtil.isSameDay(date, new Date());
	}
	
	public static int daysBetween(Date d1, Date d2) {
		LocalDate ld1 = new LocalDate(d1);
	    LocalDate ld2 = new LocalDate(d2);
	 
	    return Days.daysBetween(ld1, ld2).getDays();
	}
	
	public static boolean isDateInclusiveBetween(Date date1, Date date2, Date toCheck) {
		return date1.compareTo(toCheck) * toCheck.compareTo(date2) >= 0;
	}
	
	public static Date floorDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
	}
	
	public static Date ceilDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);  
        cal.set(Calendar.SECOND, 59);  
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
	}
}
