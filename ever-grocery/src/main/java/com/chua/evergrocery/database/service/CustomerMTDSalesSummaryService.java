package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.CustomerMTDSalesSummary;
import com.chua.evergrocery.database.prototype.CustomerMTDSalesSummaryPrototype;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public interface CustomerMTDSalesSummaryService
		extends Service<CustomerMTDSalesSummary, Long>, CustomerMTDSalesSummaryPrototype{

	List<CustomerMTDSalesSummary> findAllByCustomerOrderByMonthId(Long customerId);
}
