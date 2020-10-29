package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.CustomerMTDSalesSummary;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public interface CustomerMTDSalesSummaryPrototype {

	CustomerMTDSalesSummary findByCustomerAndMonthId(long customerId, int monthId);
}
