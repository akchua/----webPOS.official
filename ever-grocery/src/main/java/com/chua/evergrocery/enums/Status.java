package com.chua.evergrocery.enums;

public enum Status {

	CANCELLED("Cancelled"),
	
	LISTING("Listing"),
	
	PRINTED("Printed"),
	
	PAID("Paid"),
	
	CHECKED("Checked");
	
	private final String description;
	
	Status(final String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
