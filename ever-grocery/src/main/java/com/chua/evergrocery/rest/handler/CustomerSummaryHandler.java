package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.database.entity.CustomerCategoryMTDSalesSummary;
import com.chua.evergrocery.database.entity.CustomerMTDSalesSummary;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public interface CustomerSummaryHandler {

	List<CustomerMTDSalesSummary> getCustomerMTDSalesSummaryList(Long customerId);
	
	List<CustomerCategoryMTDSalesSummary> getCustomerCategoryMTDSalesSummaryList(Long customerCategoryId);
	
	void updateMonthlyCustomerSummaries(int includedMonthsAgo);
	
	void updateMonthlyCustomerSchedule();
	
	void updateDailyCustomerOOSFlag();
}
