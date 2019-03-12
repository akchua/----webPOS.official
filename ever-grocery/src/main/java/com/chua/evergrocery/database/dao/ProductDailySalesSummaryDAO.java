package com.chua.evergrocery.database.dao;

import java.util.Date;
import java.util.List;

import com.chua.evergrocery.beans.ProductSalesSummaryBean;
import com.chua.evergrocery.database.entity.ProductDailySalesSummary;
import com.chua.evergrocery.database.prototype.ProductDailySalesSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
public interface ProductDailySalesSummaryDAO extends DAO<ProductDailySalesSummary, Long>, ProductDailySalesSummaryPrototype {

	List<ProductSalesSummaryBean> getAllProductSalesSummaryByCompanyAndPaidDate(long companyId,
			Date paidStart, Date paidEnd);
}
