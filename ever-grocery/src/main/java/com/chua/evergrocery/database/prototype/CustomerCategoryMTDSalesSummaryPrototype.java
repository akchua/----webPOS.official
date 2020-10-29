package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.CustomerCategoryMTDSalesSummary;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public interface CustomerCategoryMTDSalesSummaryPrototype {

	CustomerCategoryMTDSalesSummary findByCustomerCategoryAndMonthId(long customerCategoryId, int monthId);
}
