package com.chua.evergrocery.rest.validator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.chua.evergrocery.beans.PurchaseOrderFormBean;
import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 8, 2018
 */
@Component
public class PurchaseOrderFormValidator extends AbstractFormValidator<PurchaseOrderFormBean> {

	@Override
	public Map<String, String> validate(PurchaseOrderFormBean purchaseOrderForm) {
		final Map<String, String> errors = new HashMap<String, String>();
		
		String temp = "";
		
		temp = notNull(purchaseOrderForm.getCompanyId());
		if(!temp.isEmpty()) errors.put("companyId", temp);
		temp = notNull(purchaseOrderForm.getDeliveredOn());
		if(!temp.isEmpty()) errors.put("deliveredOn", temp);
		
		return errors;
	}
	
	public Map<String, String> validateDeliveryDateWithCompanyLastPurchaseDate(Date deliveredOn, Company company) {
		final Map<String, String> errors = new HashMap<String, String>();
		
		String temp = "";
		
		if(company.isAutoOrderActive() && !DateUtil.isDateInclusiveBetween(company.getLastPurchaseOrderDate(), new Date(), deliveredOn)) {
			temp = "Invalid Delivery Date. Valid delivery dates are from " + DateFormatter.prettyFormat(company.getLastPurchaseOrderDate()) + " to " + DateFormatter.prettyFormat(new Date()) + " only.";
		}
		if(!temp.isEmpty()) errors.put("deliveredOn", temp);
		
		return errors;
	}
}
