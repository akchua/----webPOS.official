package com.chua.evergrocery.enums;

public enum ReceiptType {

	BEFORE_VAT_AND_DISCOUNT ("Before VAT and Discount"),
	
	BEFORE_VAT_AFTER_DISCOUNT ("Before VAT, After Discount"),
	
	AFTER_VAT_BEFORE_DISCOUNT ("After VAT, Before Discount"),
	
	AFTER_VAT_AND_DISCOUNT ("After VAT and Discount");
	
	private final String description;
	
	ReceiptType(final String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
