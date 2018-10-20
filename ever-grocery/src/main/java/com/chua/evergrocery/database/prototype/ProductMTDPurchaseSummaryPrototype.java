package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.ProductMTDPurchaseSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
public interface ProductMTDPurchaseSummaryPrototype {

	ProductMTDPurchaseSummary findByProductAndMonthId(long productId, int monthId);
}
