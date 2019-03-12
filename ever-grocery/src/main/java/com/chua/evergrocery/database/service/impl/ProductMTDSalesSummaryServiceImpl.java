package com.chua.evergrocery.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.ProductMTDSalesSummaryDAO;
import com.chua.evergrocery.database.entity.ProductMTDSalesSummary;
import com.chua.evergrocery.database.service.ProductMTDSalesSummaryService;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 12, 2019
 */
@Service
public class ProductMTDSalesSummaryServiceImpl
		extends AbstractService<ProductMTDSalesSummary, Long, ProductMTDSalesSummaryDAO>
		implements ProductMTDSalesSummaryService {

	@Autowired
	protected ProductMTDSalesSummaryServiceImpl(ProductMTDSalesSummaryDAO dao) {
		super(dao);
	}
	
	@Override
	public ProductMTDSalesSummary findByProductAndMonthId(long productId, int monthId) {
		return dao.findByProductAndMonthId(productId, monthId);
	}
}
