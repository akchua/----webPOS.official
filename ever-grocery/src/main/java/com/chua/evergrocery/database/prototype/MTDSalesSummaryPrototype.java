package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.MTDSalesSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 12, 2019
 */
public interface MTDSalesSummaryPrototype {

	MTDSalesSummary findByMonthId(int monthId);
}
