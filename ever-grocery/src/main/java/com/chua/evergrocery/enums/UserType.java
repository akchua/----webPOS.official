package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserType {

	ADMINISTRATOR ("Administrator", Integer.valueOf(1)),
	
	MANAGER ("Manager", Integer.valueOf(2)),
	
	ASSISTANT_MANAGER ("Assistant Manager", Integer.valueOf(3)),
	
	STORAGE_MANAGER ("Storage Manager", Integer.valueOf(3)),
	
	CASHIER ("Cashier", Integer.valueOf(5)),
	
	SENIOR_STAFF ("Senior Staff", Integer.valueOf(8)),
	
	STAFF ("Staff", Integer.valueOf(10));
	
	private final String displayName;
	
	private final Integer authority;
	
	UserType(final String displayName, final Integer authority) {
		this.displayName = displayName;
		this.authority = authority;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public Integer getAuthority() {
		return authority;
	}
}
