package com.chua.evergrocery.rest.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.chua.evergrocery.beans.CustomerCategoryFormBean;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 31, 2020
 */
@Component
public class CustomerCategoryFormValidator extends AbstractFormValidator<CustomerCategoryFormBean> {

	@Override
	public Map<String, String> validate(CustomerCategoryFormBean customerCategoryForm) {
		final Map<String, String> errors = new HashMap<String, String>();
		
		String temp = "";
		
		temp = validateString(customerCategoryForm.getName(), 2, 30);
		if(!temp.isEmpty()) errors.put("name", temp);
		temp = validateString(customerCategoryForm.getCode(), 2, 10);
		if(!temp.isEmpty()) errors.put("code", temp);
		
		return errors;
	}
}
