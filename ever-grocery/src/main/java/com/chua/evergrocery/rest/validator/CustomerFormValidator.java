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
		
		temp = notNull(customerForm.getCustomerCategoryId());
		if(!temp.isEmpty()) errors.put("customerCategoryId", temp);
		temp = validateString(customerForm.getName(), 2, 30);
		if(!temp.isEmpty()) errors.put("name", temp);
		temp = validateStringNull(customerForm.getStoreName(), 2, 30);
		if(!temp.isEmpty()) errors.put("storeName", temp);
		temp = validateString(customerForm.getCode(), 2, 10);
		if(!temp.isEmpty()) errors.put("code", temp);
		temp = validateString(customerForm.getAddress(), 5, 100);
		if(!temp.isEmpty()) errors.put("address", temp);
		temp = validateStringNull(customerForm.getCardId(), 13, 13);
		if(!temp.isEmpty()) errors.put("cardId", temp);
		
		return errors;
	}
}
