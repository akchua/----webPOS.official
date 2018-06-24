package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   24 Jun 2018
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SystemVariableTag {

	SERIAL_INVOICE_NUMBER("Serial Invoice Number", "SIN");
	
	private final String displayName;
	
	private final String tag;
	
	public String getName() {
		return toString();
	}
	
	SystemVariableTag(final String displayName, String tag) {
		this.displayName = displayName;
		this.tag = tag;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public String getTag() {
		return tag;
	}
}
