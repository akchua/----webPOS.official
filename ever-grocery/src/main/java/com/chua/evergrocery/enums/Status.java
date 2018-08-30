package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status {

	CANCELLED("Cancelled"),
	
	LISTING("Listing"),

	SUBMITTED("Submitted"),
	
	PAID("Paid"),
	
	CHECKED("Checked"),
	
	// CASH FLOW
	REQUESTING("Requesting"),
	
	DECLINED("Declined"),
	
	TRANSFERRED("Transferred");
	
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
