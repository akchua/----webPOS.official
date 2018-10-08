package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ReceiptType {

	BEFORE_VAT_AND_DISCOUNT ("Before VAT and Discount", "BV-BD"),
	
	BEFORE_VAT_AFTER_DISCOUNT ("Before VAT, After Discount", "BV-AD"),
	
	AFTER_VAT_BEFORE_DISCOUNT ("After VAT, Before Discount", "AV-BD"),
	
	AFTER_VAT_AND_DISCOUNT ("After VAT and Discount", "AV-AD");
	
	private final String displayName;
	
	private final String shortHand;
	
	public String getName() {
		return toString();
	}
	
	ReceiptType(final String displayName, final String shortHand) {
		this.displayName = displayName;
		this.shortHand = shortHand;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public String getShortHand() {
		return shortHand;
	}
}
