package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.CustomerCategoryMTDSalesSummary;
import com.chua.evergrocery.database.prototype.CustomerCategoryMTDSalesSummaryPrototype;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public interface CustomerCategoryMTDSalesSummaryService
		extends Service<CustomerCategoryMTDSalesSummary, Long>, CustomerCategoryMTDSalesSummaryPrototype {

	List<CustomerCategoryMTDSalesSummary> findAllByCustomerCategoryOrderByMonthId(Long customerCategoryId);
}
