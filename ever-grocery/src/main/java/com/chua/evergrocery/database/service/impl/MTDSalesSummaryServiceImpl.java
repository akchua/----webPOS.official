package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.MTDSalesSummaryDAO;
import com.chua.evergrocery.database.entity.MTDSalesSummary;
import com.chua.evergrocery.database.service.MTDSalesSummaryService;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Mar 12, 2019
 */
@Service
public class MTDSalesSummaryServiceImpl
		extends AbstractService<MTDSalesSummary, Long, MTDSalesSummaryDAO>
		implements MTDSalesSummaryService{

	@Autowired
	protected MTDSalesSummaryServiceImpl(MTDSalesSummaryDAO dao) {
		super(dao);
	}
	
	@Override
	public MTDSalesSummary findByMonthId(int monthId) {
		return dao.findByMonthId(monthId);
	}

	@Override
	public List<MTDSalesSummary> findAllOrderByMonthId() {
		return dao.findAllWithOrder(new Order[] { Order.asc("monthId") });
	}

	@Override
	public List<MTDSalesSummary> findByYearOrderByMonthId(int year) {
		return dao.findByYearWithOrder(year, new Order[] { Order.asc("monthId") });
	}
}
