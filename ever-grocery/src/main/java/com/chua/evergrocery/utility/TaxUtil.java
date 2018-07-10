package com.chua.evergrocery.utility;

import com.chua.evergrocery.enums.TaxType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 10, 2018
 */
public class TaxUtil {

	public static Float convertTaxType(Float amount, TaxType from, TaxType to) {
		final Float convertedAmount;
		
		if(from.equals(TaxType.VAT) && (to.equals(TaxType.VAT_EXEMPT) || to.equals(TaxType.ZERO_RATED))) {
			convertedAmount = amount / 1.12f;
		} else if (to.equals(TaxType.VAT) && (from.equals(TaxType.VAT_EXEMPT) || from.equals(TaxType.ZERO_RATED))){
			convertedAmount = amount * 1.12f;
		} else {
			convertedAmount = amount;
		}
		
		return convertedAmount;
	}
}
