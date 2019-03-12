package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.CompanyMTDSalesSummary;
import com.chua.evergrocery.database.prototype.CompanyMTDSalesSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 12, 2019
 */
public interface CompanyMTDSalesSummaryService extends Service<CompanyMTDSalesSummary, Long>, CompanyMTDSalesSummaryPrototype{

	List<CompanyMTDSalesSummary> findAllByCompanyOrderByMonthId(Long companyId);
}
