package com.chua.evergrocery.utility.format;

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
}
