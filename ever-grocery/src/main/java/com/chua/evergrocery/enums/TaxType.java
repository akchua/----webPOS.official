package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   7 Jul 2018
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaxType {

	VAT("Vat", 'V'),
	
	VAT_EXEMPT("Vat-Exempt", 'E'),
	
	ZERO_RATED("Zero Rated", 'Z');
	
	private final String displayName;
	
	private final Character symbol;
	
	TaxType(final String displayName, final Character symbol) {
		this.displayName = displayName;
		this.symbol = symbol;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public Character getSymbol() {
		return symbol;
	}
}
