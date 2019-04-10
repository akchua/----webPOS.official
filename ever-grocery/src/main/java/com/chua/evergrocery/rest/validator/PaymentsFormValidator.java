package com.chua.evergrocery.rest.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.chua.evergrocery.beans.PaymentsFormBean;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   22 Mar 2019
 */
@Component
public class PaymentsFormValidator extends AbstractFormValidator<PaymentsFormBean>  {

	@Override
	public Map<String, String> validate(PaymentsFormBean paymentsForm) {
		final Map<String, String> errors = new HashMap<String, String>();
		
		String temp = "";
		
		temp = notNull(paymentsForm.getCustomerOrderId());
		if(!temp.isEmpty()) errors.put("customerOrderId", temp);
		temp = validateFloat(paymentsForm.getCash(), -999999, 999999);
		if(!temp.isEmpty()) errors.put("cash", temp);
		if(paymentsForm.getCheckAmount() == null) paymentsForm.setCheckAmount(0.0f);
		temp = validateFloat(paymentsForm.getCheckAmount(), 0, 999999);
		if(!temp.isEmpty()) errors.put("checkAmount", temp);
		if(!paymentsForm.getCheckAmount().equals(0.0f)) {
			temp = notNull(paymentsForm.getCheckAccountNumber());
			if(!temp.isEmpty()) errors.put("checkAccountNumber", temp);
			temp = notNull(paymentsForm.getCheckNumber());
			if(!temp.isEmpty()) errors.put("checkNumber", temp);
		}
		temp = validateFloat(paymentsForm.getCardAmount(), 0, 999999);
		if(!temp.isEmpty()) errors.put("cardAmount", temp);
		if(!paymentsForm.getCardAmount().equals(0.0f)) {
			temp = notNull(paymentsForm.getCardTransactionNumber());
			if(!temp.isEmpty()) errors.put("cardTransactionNumber", temp);
		}
		
		return errors;
	}
}
