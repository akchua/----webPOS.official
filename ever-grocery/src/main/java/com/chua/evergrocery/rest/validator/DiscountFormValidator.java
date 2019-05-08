package com.chua.evergrocery.rest.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.chua.evergrocery.beans.DiscountFormBean;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   21 Mar 2019
 */
@Component
public class DiscountFormValidator extends AbstractFormValidator<DiscountFormBean>{

	@Override
	public Map<String, String> validate(DiscountFormBean discountForm) {
		final Map<String, String> errors = new HashMap<String, String>();
		
		String temp = "";
		
		temp = notNull(discountForm.getCustomerOrderId());
		if(!temp.isEmpty()) errors.put("customerOrderId", temp);
		temp = notNull(discountForm.getName());
		if(!temp.isEmpty()) errors.put("name", temp);
		temp = notNull(discountForm.getAddress());
		if(!temp.isEmpty()) errors.put("address", temp);
		temp = notNull(discountForm.getTin());
		if(!temp.isEmpty()) errors.put("tin", temp);
		temp = notNull(discountForm.getDiscountType());
		if(!temp.isEmpty()) errors.put("discountType", temp);
		temp = validateFloat(discountForm.getGrossAmountLimit(), 1, 999999);
		if(!temp.isEmpty()) errors.put("grossAmountLimit", temp);
		temp = notNull(discountForm.getDiscountIdNumber());
		if(!temp.isEmpty()) errors.put("discountIdNumber", temp);
		
		return errors;
	}
}
