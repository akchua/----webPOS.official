package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 10, 2019
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuditLogType {

	SALES ("Sales"),
	
	CASH_TRANSFER("Cash Transfer");
	
	private final String displayName;
	
	public String getName() {
		return toString();
	}
	
	AuditLogType(final String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
