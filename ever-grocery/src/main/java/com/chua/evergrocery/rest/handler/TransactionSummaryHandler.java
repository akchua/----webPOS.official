package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;
import com.chua.evergrocery.database.entity.MTDPurchaseSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 21, 2018
 */
public interface TransactionSummaryHandler {

	List<MTDPurchaseSummary> getMTDPurchaseSummaryList();
	
	List<MTDPurchaseSummary> getMTDPurchaseSummaryListByYear(int year);
	
	List<CompanyMTDPurchaseSummary> getCompanyMTDPurchaseSummaryList(Long companyId);
	
	void updateAllPurchaseSummaries(int includedMonthsAgo);
}
