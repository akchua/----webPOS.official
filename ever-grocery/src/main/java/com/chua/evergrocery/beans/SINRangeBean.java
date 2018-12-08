package com.chua.evergrocery.beans;

import java.text.DecimalFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 8, 2018
 */
public class SINRangeBean {

	private Long startSIN;
	
	private Long endSIN;
	
	public SINRangeBean() {
		this.startSIN = 0l;
		this.endSIN = 0l;
	}

	public Long getStartSIN() {
		return startSIN;
	}
	
	public String getFormattedStartSIN() {
		if(startSIN != null) {
			DecimalFormat SIN_FORMAT = new DecimalFormat("0000000000");
			return SIN_FORMAT.format(startSIN);
		} else {
			return "n/a";
		}
	}

	public void setStartSIN(Long startSIN) {
		this.startSIN = startSIN;
	}
	
	public Long getEndSIN() {
		return endSIN;
	}
	
	public String getFormattedEndSIN() {
		if(startSIN != null) {
			DecimalFormat SIN_FORMAT = new DecimalFormat("0000000000");
			return SIN_FORMAT.format(endSIN);
		} else {
			return "n/a";
		}
	}

	public void setEndSIN(Long endSIN) {
		this.endSIN = endSIN;
	}
}
