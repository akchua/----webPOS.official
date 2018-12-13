package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 13, 2018
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ReportType {
	
	FULL("Full Report"),
	
	REGULAR("Regular Sales Only"),
	
	SCD("Senior Discount Sales Only"),
	
	RETURNS("Returns Only");

	private final String displayName;
	
	ReportType(final String displayName) {
		this.displayName = displayName;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
