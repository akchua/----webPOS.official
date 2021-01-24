package com.chua.evergrocery.rest.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.chua.evergrocery.beans.CashTransferFormBean;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 3, 2018
 */
@Component
public class CashTransferValidator extends AbstractFormValidator<CashTransferFormBean> {

	@Override
	public Map<String, String> validate(CashTransferFormBean cashTransferForm) {
		final Map<String, String> errors = new HashMap<String, String>();
		
		String temp = "";
		
		temp = notNull(cashTransferForm.getCashToId());
		if(!temp.isEmpty()) errors.put("cashToId", temp);
		temp = validateAmount(cashTransferForm.getAmount());
		if(!temp.isEmpty()) errors.put("amount", temp);
		
		return errors;
	}
	
	private String validateAmount(Float amount) {
		return validateFloat(amount, 1, 10000000);
	}
}
