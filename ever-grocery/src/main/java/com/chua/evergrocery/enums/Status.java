package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status {

	CANCELLED("Cancelled"),
	
	LISTING("Listing"),
	
	PRINTED("Printed"),
	
	PAID("Paid"),
	
	CHECKED("Checked");
	
	private final String displayName;
	
	public String getName() {
		return toString();
	}
	
	Status(final String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
