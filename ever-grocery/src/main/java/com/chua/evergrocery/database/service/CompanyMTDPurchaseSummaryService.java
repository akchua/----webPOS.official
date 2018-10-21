package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;
import com.chua.evergrocery.database.prototype.CompanyMTDPurchaseSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
public interface CompanyMTDPurchaseSummaryService 
	extends Service<CompanyMTDPurchaseSummary, Long>, CompanyMTDPurchaseSummaryPrototype {

	List<CompanyMTDPurchaseSummary> findAllByCompanyOrderByMonthId(Long companyId);
}
