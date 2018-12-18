package com.chua.evergrocery.database.service;

import java.util.Date;
import java.util.List;

import com.chua.evergrocery.database.entity.CompanyDailySalesSummary;
import com.chua.evergrocery.database.prototype.CompanyDailySalesSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
public interface CompanyDailySalesSummaryService extends Service<CompanyDailySalesSummary, Long>, CompanyDailySalesSummaryPrototype {
	
	CompanyDailySalesSummary findByCompanyAndSalesDate(long companyId, Date salesDate);

	List<CompanyDailySalesSummary> findAllByCompanyAndRangeOrderBySalesDate(Long companyId, Date startDate, Date endDate);
}
