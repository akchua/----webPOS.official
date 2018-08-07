package com.chua.evergrocery.rest.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.chua.evergrocery.beans.ProductFormBean;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Aug 7, 2018
 */
@Component
public class ProductFormValidator extends AbstractFormValidator<ProductFormBean> {

	@Override
	public Map<String, String> validate(ProductFormBean productForm) {
		final Map<String, String> errors = new HashMap<String, String>();
		
		String temp = "";
		
		temp = validateString(productForm.getName(), 3, 100);
		if(!temp.isEmpty()) errors.put("name", temp);
		temp = validateStringNull(productForm.getDisplayName(), 3, 25);
		if(!temp.isEmpty()) errors.put("displayName", temp);
		temp = notNull(productForm.getCategoryId());
		if(!temp.isEmpty()) errors.put("category", temp);
		temp = notNull(productForm.getCompanyId());
		if(!temp.isEmpty()) errors.put("company", temp);
		
		return errors;
	}
}
