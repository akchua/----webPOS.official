package com.chua.evergrocery.rest.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.chua.evergrocery.beans.CustomerFormBean;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Apr 15, 2019
 */
@Component
public class CustomerFormValidator extends AbstractFormValidator<CustomerFormBean> {

	@Override
	public Map<String, String> validate(CustomerFormBean customerForm) {
		final Map<String, String> errors = new HashMap<String, String>();
		
		String temp = "";
		
		temp = validateString(customerForm.getFirstName(), 2, 20);
		if(!temp.isEmpty()) errors.put("firstName", temp);
		temp = validateString(customerForm.getLastName(), 2, 20);
		if(!temp.isEmpty()) errors.put("lastName", temp);
		temp = notNull(customerForm.getContactNumber());
		if(!temp.isEmpty()) errors.put("contactNumber", temp);
		temp = notNull(customerForm.getAddress());
		if(!temp.isEmpty()) errors.put("address", temp);
		temp = validateString(customerForm.getCardId(), 13, 13);
		if(!temp.isEmpty()) errors.put("cardId", temp);
		
		return errors;
	}
}
