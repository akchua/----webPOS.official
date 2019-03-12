package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.beans.ProductSalesSummaryBean;
import com.chua.evergrocery.database.entity.ProductDailySalesSummary;
import com.chua.evergrocery.database.prototype.ProductDailySalesSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
public interface ProductDailySalesSummaryService extends Service<ProductDailySalesSummary, Long>, ProductDailySalesSummaryPrototype {

	List<ProductSalesSummaryBean> getAllProductSalesSummaryByCompanyAndMonthId(long companyId,
			int monthId);
}
