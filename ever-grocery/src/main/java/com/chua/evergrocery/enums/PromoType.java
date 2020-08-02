package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Jul 31, 2020
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PromoType {

	OUTRIGHT ("Outright", "O"),
	
	POINTS ("Points", "P");
	
	private final String displayName;
	
	private final String shortHand;
	
	public String getName() {
		return toString();
	}
	
	PromoType(final String displayName, final String shortHand) {
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
