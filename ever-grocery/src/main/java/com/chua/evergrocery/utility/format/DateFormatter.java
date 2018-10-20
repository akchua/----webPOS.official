package com.chua.evergrocery.utility.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.chua.evergrocery.utility.DateUtil;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	6 Jan 2017
 */
public class DateFormatter {
	
	private static final DateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	private static final DateFormat LONG_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	private static final DateFormat PRETTY_DATE_FORMAT = new SimpleDateFormat("MMMM dd, yyyy");
	
	private static final DateFormat FILE_SAFE_SHORT_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
	
	private static final DateFormat FILE_SAFE_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy-HH-mm");
	
	private static final DateFormat PRETTY_MONTH_FORMAT = new SimpleDateFormat("MMMMMMM yyyy");
	
	private static final DateTimeFormatter SHORT_DATETIME_FORMAT = DateTimeFormat.forPattern("MM/dd/yyyy");
	
	private static final DateTimeFormatter LONG_DATETIME_FORMAT = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
	
	private static final DateTimeFormatter PRETTY_DATETIME_FORMAT = DateTimeFormat.forPattern("MMMM dd, yyyy");
	
	private static final DateTimeFormatter FILE_SAFE_SHORT_DATETIME_FORMAT = DateTimeFormat.forPattern("MM-dd-yyyy");
	
	private static final DateTimeFormatter FILE_SAFE_DATETIME_FORMAT = DateTimeFormat.forPattern("MM-dd-yyyy-HH-mm");
	
	private DateFormatter() {
		
	}
	
	public static String shortFormat(Date toFormat) {
		return SHORT_DATE_FORMAT.format(toFormat);
	}
	
	public static String shortFormat(DateTime toFormat) {
		return toFormat.toString(SHORT_DATETIME_FORMAT);
	}
	
	public static String longFormat(Date toFormat) {
		return LONG_DATE_FORMAT.format(toFormat);
	}
	
	public static String longFormat(DateTime toFormat) {
		return toFormat.toString(LONG_DATETIME_FORMAT);
	}
	
	public static String prettyFormat(Date toFormat) {
		return PRETTY_DATE_FORMAT.format(toFormat);
	}
	
	public static String prettyFormat(DateTime toFormat) {
		return toFormat.toString(PRETTY_DATETIME_FORMAT);
	}
	
	public static String fileSafeShortFormat(Date toFormat) {
		return FILE_SAFE_SHORT_DATE_FORMAT.format(toFormat);
	}
	
	public static String fileSafeShortFormat(DateTime toFormat) {
		return toFormat.toString(FILE_SAFE_SHORT_DATETIME_FORMAT);
	}
	
	public static String fileSafeFormat(Date toFormat) {
		return FILE_SAFE_DATE_FORMAT.format(toFormat);
	}
	
	public static String fileSafeFormat(DateTime toFormat) {
		return toFormat.toString(FILE_SAFE_DATETIME_FORMAT);
	}
	
	public static String prettyMonthFormat(int monthId) {
		return PRETTY_MONTH_FORMAT.format(DateUtil.monthIdToDate(monthId));
	}
}
