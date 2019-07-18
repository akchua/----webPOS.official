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
	
	private static final DateFormat PRETTY_DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");
	
	private static final DateFormat FILE_SAFE_SHORT_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
	
	private static final DateFormat FILE_SAFE_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy-HH-mm");
	
	private static final DateFormat PRETTY_MONTH_FORMAT = new SimpleDateFormat("MMMMMMM yyyy");
	
	private static final DateFormat TIME_ONLY_FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	private static final DateFormat PRETTY_DAY_FORMAT = new SimpleDateFormat("MMM dd");
	
	private static final DateFormat ENCRYPTED_DATE_FORMAT = new SimpleDateFormat("ssmmHHyyddMM");
	
	private static final DateTimeFormatter SHORT_DATETIME_FORMAT = DateTimeFormat.forPattern("MM/dd/yyyy");
	
	private static final DateTimeFormatter LONG_DATETIME_FORMAT = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
	
	private static final DateTimeFormatter PRETTY_DATETIME_FORMAT = DateTimeFormat.forPattern("MMM dd, yyyy");
	
	private static final DateTimeFormatter FILE_SAFE_SHORT_DATETIME_FORMAT = DateTimeFormat.forPattern("MM-dd-yyyy");
	
	private static final DateTimeFormatter FILE_SAFE_DATETIME_FORMAT = DateTimeFormat.forPattern("MM-dd-yyyy-HH-mm");
	
	private static final DateTimeFormatter TIME_ONLY_DATETIME_FORMAT = DateTimeFormat.forPattern("HH:mm:ss");
	
	private static final DateTimeFormatter PRETTY_DAY_DATETIME_FORMAT = DateTimeFormat.forPattern("MMM dd");
	
	private static final DateTimeFormatter ENCRYPTED_DATETIME_FORMAT = DateTimeFormat.forPattern("ssmmHHyyddMM");
	
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
	
	public static String timeOnlyFormat(Date toFormat) {
		return TIME_ONLY_FORMAT.format(toFormat);
	}
	
	public static String prettyDayFormat(DateTime toFormat) {
		return toFormat.toString(PRETTY_DAY_DATETIME_FORMAT);
	}
	
	public static String prettyDayFormat(Date toFormat) {
		return PRETTY_DAY_FORMAT.format(toFormat);
	}
	
	public static String encryptedDayFormat(DateTime toFormat) {
		return toFormat.toString(ENCRYPTED_DATETIME_FORMAT);
	}
	
	public static String encryptedDayFormat(Date toFormat) {
		return ENCRYPTED_DATE_FORMAT.format(toFormat);
	}
	
	public static String timeOnlyFormat(DateTime toFormat) {
		return toFormat.toString(TIME_ONLY_DATETIME_FORMAT);
	}
	
	public static String prettyMonthFormat(int monthId) {
		return PRETTY_MONTH_FORMAT.format(DateUtil.monthIdToDate(monthId));
	}
}
