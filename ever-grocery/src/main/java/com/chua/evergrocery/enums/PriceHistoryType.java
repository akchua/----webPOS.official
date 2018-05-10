package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 10, 2018
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PriceHistoryType {

	SALE("Sale"),
	
	NET_PURCHASE("Net Purchase");
	
	private final String displayName;
	
	PriceHistoryType(final String displayName) {
		this.displayName = displayName;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
