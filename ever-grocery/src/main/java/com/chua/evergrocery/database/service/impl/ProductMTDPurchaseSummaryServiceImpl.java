package com.chua.evergrocery.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.ProductMTDPurchaseSummaryDAO;
import com.chua.evergrocery.database.entity.ProductMTDPurchaseSummary;
import com.chua.evergrocery.database.service.ProductMTDPurchaseSummaryService;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
@Service
public class ProductMTDPurchaseSummaryServiceImpl
		extends AbstractService<ProductMTDPurchaseSummary, Long, ProductMTDPurchaseSummaryDAO>
		implements ProductMTDPurchaseSummaryService {

	@Autowired
	protected ProductMTDPurchaseSummaryServiceImpl(ProductMTDPurchaseSummaryDAO dao) {
		super(dao);
	}

	@Override
	public ProductMTDPurchaseSummary findByProductAndMonthId(long productId, int monthId) {
		return dao.findByProductAndMonthId(productId, monthId);
	}
}
