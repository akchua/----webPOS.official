package com.chua.evergrocery.utility.format;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 22, 2018
 */
public class NumberFormatter {

	private NumberFormatter() {
		
	}
	
	public static String toPercent(Float value) {
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMinimumFractionDigits(2);
		return percentFormat.format(value / 100);
	}
	
	public static String decimalFormat(Float value, int decimalPlaces) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(decimalPlaces);
		return df.format(value);
	}
	
	public static String SINFormat(Long SIN) {
		DecimalFormat SIN_FORMAT = new DecimalFormat("0000000000000000");
		return SIN_FORMAT.format(SIN);
	}
}
