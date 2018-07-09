package com.chua.evergrocery.rest.handler;

import java.util.List;

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
	
	List<Status> getCashTransferStatusList();
}
