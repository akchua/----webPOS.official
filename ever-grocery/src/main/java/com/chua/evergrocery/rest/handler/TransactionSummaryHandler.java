package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.beans.SalesSummaryBean;
import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;
import com.chua.evergrocery.database.entity.CompanyMTDSalesSummary;
import com.chua.evergrocery.database.entity.DailySalesSummary;
import com.chua.evergrocery.database.entity.MTDPurchaseSummary;
import com.chua.evergrocery.database.entity.MTDSalesSummary;
import com.chua.evergrocery.database.entity.ProductDailySalesSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 21, 2018
 */
public interface TransactionSummaryHandler {

	List<MTDPurchaseSummary> getMTDPurchaseSummaryList();
	
	List<MTDPurchaseSummary> getMTDPurchaseSummaryListByYear(int year);
	
	List<CompanyMTDPurchaseSummary> getCompanyMTDPurchaseSummaryList(Long companyId);
	
	List<MTDSalesSummary> getMTDSalesSummaryList();
	
	List<MTDSalesSummary> getMTDSalesSummaryListByYear(int year);
	
	List<CompanyMTDSalesSummary> getCompanyMTDSalesSummaryList(Long companyId);
	
	List<DailySalesSummary> getDailySalesSummaryList(int daysAgo);
	
	List<ProductDailySalesSummary> getProductDailySalesSummaryList(Long productId, int daysAgo);
	
	SalesSummaryBean getLiveSalesSummary();
	
	Integer getPaidCountToday();
	
	void updateAllPurchaseSummaries(int includedMonthsAgo);
	
	void updateMonthlySalesSummaries(int includedMonthsAgo);
	
	void updateDailySalesSummaries(int includedDaysAgo);
}
