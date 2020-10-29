package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.CustomerCategoryMTDSalesSummaryDAO;
import com.chua.evergrocery.database.entity.CustomerCategoryMTDSalesSummary;
import com.chua.evergrocery.database.service.CustomerCategoryMTDSalesSummaryService;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
@Service
public class CustomerCategoryMTDSalesSummaryServiceImpl
		extends AbstractService<CustomerCategoryMTDSalesSummary, Long, CustomerCategoryMTDSalesSummaryDAO>
		implements CustomerCategoryMTDSalesSummaryService {

	@Autowired
	protected CustomerCategoryMTDSalesSummaryServiceImpl(CustomerCategoryMTDSalesSummaryDAO dao) {
		super(dao);
	}
	
	@Override
	public CustomerCategoryMTDSalesSummary findByCustomerCategoryAndMonthId(long customerCategoryId, int monthId) {
		return dao.findByCustomerCategoryAndMonthId(customerCategoryId, monthId);
	}

	@Override
	public List<CustomerCategoryMTDSalesSummary> findAllByCustomerCategoryOrderByMonthId(Long customerCategoryId) {
		return dao.findAllByCustomerCategoryWithOrder(customerCategoryId, new Order[] { Order.asc("monthId") });
	}
}
