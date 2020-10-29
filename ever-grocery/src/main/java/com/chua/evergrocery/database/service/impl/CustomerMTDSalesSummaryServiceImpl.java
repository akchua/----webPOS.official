package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.CustomerMTDSalesSummaryDAO;
import com.chua.evergrocery.database.entity.CustomerMTDSalesSummary;
import com.chua.evergrocery.database.service.CustomerMTDSalesSummaryService;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
@Service
public class CustomerMTDSalesSummaryServiceImpl
		extends AbstractService<CustomerMTDSalesSummary, Long, CustomerMTDSalesSummaryDAO>
		implements CustomerMTDSalesSummaryService {

	@Autowired
	protected CustomerMTDSalesSummaryServiceImpl(CustomerMTDSalesSummaryDAO dao) {
		super(dao);
	}
	
	@Override
	public CustomerMTDSalesSummary findByCustomerAndMonthId(long customerId, int monthId) {
		return dao.findByCustomerAndMonthId(customerId, monthId);
	}

	@Override
	public List<CustomerMTDSalesSummary> findAllByCustomerOrderByMonthId(Long customerId) {
		return dao.findAllByCustomerWithOrder(customerId, new Order[] { Order.asc("monthId") });
	}
}
