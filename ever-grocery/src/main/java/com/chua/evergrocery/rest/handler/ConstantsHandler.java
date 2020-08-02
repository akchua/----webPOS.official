package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.PromoType;
import com.chua.evergrocery.enums.ReportType;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.enums.TaxType;
import com.chua.evergrocery.enums.UserType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   6 Dec 2017
 */
public interface ConstantsHandler {

	String getVersion();

	TaxType getDefaultTaxType();
	
	List<UserType> getUserTypeList();
	
	List<TaxType> getTaxTypeList();
	
	List<DiscountType> getDiscountTypeList();
	
	List<PromoType> getPromoTypeList();
	
	List<ReportType> getReportTypeList();
	
	List<Status> getCashTransferStatusList();
}
