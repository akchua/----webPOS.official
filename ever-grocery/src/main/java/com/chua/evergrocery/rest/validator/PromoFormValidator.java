package com.chua.evergrocery.rest.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.chua.evergrocery.beans.PromoFormBean;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Aug 1, 2020
 */
@Component
public class PromoFormValidator extends AbstractFormValidator<PromoFormBean> {

	@Override
	public Map<String, String> validate(PromoFormBean promoForm) {
		final Map<String, String> errors = new HashMap<String, String>();
		
		String temp = "";
		
		temp = notNull(promoForm.getProductId());
		if(!temp.isEmpty()) errors.put("product", temp);
		temp = notNull(promoForm.getStartDate());
		if(!temp.isEmpty()) errors.put("startDate", temp);
		if(promoForm.getStartDate() != null && promoForm.getStartDate().after(promoForm.getEndDate()))
		errors.put("startDate", "Invalid duration");
		temp = notNull(promoForm.getEndDate());
		if(!temp.isEmpty()) errors.put("endDate", temp);
		temp = validateFloat(promoForm.getBudget(), 0, 999999);
		if(!temp.isEmpty()) errors.put("budget", temp);
		temp = validateFloat(promoForm.getUsedBudget(), 0, 999999);
		if(!temp.isEmpty()) errors.put("usedBudget", temp);
		temp = validateFloat(promoForm.getDiscountPercent(), 0.01f, 99);
		if(!temp.isEmpty()) errors.put("discountPercent", temp);
		temp = notNull(promoForm.getPromoType());
		if(!temp.isEmpty()) errors.put("promoType", temp);
		
		return errors;
	}
}
