package com.chua.evergrocery.database.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.ProductDailySalesSummaryDAO;
import com.chua.evergrocery.database.entity.ProductDailySalesSummary;
import com.chua.evergrocery.database.service.ProductDailySalesSummaryService;
import com.chua.evergrocery.utility.DateUtil;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
@Service
public class ProductDailySalesSummaryServiceImpl
		extends AbstractService<ProductDailySalesSummary, Long, ProductDailySalesSummaryDAO>
		implements ProductDailySalesSummaryService {

	@Autowired
	protected ProductDailySalesSummaryServiceImpl(ProductDailySalesSummaryDAO dao) {
		super(dao);
	}

	@Override
	public ProductDailySalesSummary findByProductAndSalesDate(long productId, Date salesDate) {
		return dao.findByProductAndSalesDate(productId, DateUtil.floorDay(salesDate));
	}
}
