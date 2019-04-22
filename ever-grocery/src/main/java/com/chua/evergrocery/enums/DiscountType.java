package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 10, 2018
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DiscountType {
	
	NO_DISCOUNT("No Discount", "ND", 0.0f, Float.MAX_VALUE),
	
	PWD_DISCOUNT("PWD Discount", "PWD", 5.0f, 650.0f),
	
	PWD_MEDICINE_DISCOUNT("PWD Medicine Discount", "MPD", 20.0f, 1300.0f),
	
	SENIOR_DISCOUNT("Senior Citizen Discount", "SCD", 5.0f, 650.0f),
	
	SENIOR_MEDICINE_DISCOUNT("Senior Medicine Discount", "MSC", 20.0f, 1300.0f),
	
	EMPLOYEE_DISCOUNT("Employee Discount", "EMP", 20.0f, 2000.0f),
	
	ZERO_RATED("Zero Rated", "ZER", 0.0f, 2000.0f);
	
	private final String displayName;
	
	// max 3 characters
	private final String shortHand;
	
	private final Float percentDiscount;
	
	private final Float grossHardCap;
	
	DiscountType(final String displayName, final String shortHand, final Float percentDiscount, final Float grossHardCap) {
		this.displayName = displayName;
		this.shortHand = shortHand;
		this.percentDiscount = percentDiscount;
		this.grossHardCap = grossHardCap;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public String getShortHand() {
		return shortHand;
	}

	public Float getPercentDiscount() {
		return percentDiscount;
	}

	public Float getGrossHardCap() {
		return grossHardCap;
	}
}
