package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.MTDPurchaseSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
public interface MTDPurchaseSummaryPrototype {

	MTDPurchaseSummary findByMonthId(int monthId);
}
