package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserType {

	ADMINISTRATOR ("Administrator"),
	
	MANAGER ("Manager"),
	
	ASSISTANT_MANAGER ("Assistant Manager"),
	
	STORAGE_MANAGER ("Storage Manager"),
	
	CASHIER ("Cashier"),
	
	SENIOR_STAFF ("Senior Staff"),
	
	STAFF ("Staff");
	
	private final String displayName;
	
	UserType(final String displayName) {
		this.displayName = displayName;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
