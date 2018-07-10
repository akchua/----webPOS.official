package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 10, 2018
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DiscountType {
	
	NO_DISCOUNT("No Discount", "ND", 0.0f, 9999999.0f),

	SENIOR_DISCOUNT("Senior Citizen Discount", "SCD", 5.0f, 650.0f);
	
	private final String displayName;
	
	// max 3 characters
	private final String shortHand;
	
	private final Float percentZeroRatedDiscount;
	
	private final Float grossHardCap;
	
	DiscountType(final String displayName, final String shortHand, final Float percentZeroRatedDiscount, final Float grossHardCap) {
		this.displayName = displayName;
		this.shortHand = shortHand;
		this.percentZeroRatedDiscount = percentZeroRatedDiscount;
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

	public Float getPercentZeroRatedDiscount() {
		return percentZeroRatedDiscount;
	}

	public Float getGrossHardCap() {
		return grossHardCap;
	}
	
	public Float getTotalPercentDiscount() {
		return percentZeroRatedDiscount;
	}
}
