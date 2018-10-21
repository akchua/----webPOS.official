package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.MTDPurchaseSummaryDAO;
import com.chua.evergrocery.database.entity.MTDPurchaseSummary;
import com.chua.evergrocery.database.service.MTDPurchaseSummaryService;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
@Service
public class MTDPurchaseSummaryServiceImpl
		extends AbstractService<MTDPurchaseSummary, Long, MTDPurchaseSummaryDAO>
		implements MTDPurchaseSummaryService {

	@Autowired
	protected MTDPurchaseSummaryServiceImpl(MTDPurchaseSummaryDAO dao) {
		super(dao);
	}

	@Override
	public MTDPurchaseSummary findByMonthId(int monthId) {
		return dao.findByMonthId(monthId);
	}

	@Override
	public List<MTDPurchaseSummary> findAllOrderByMonthId() {
		return dao.findAllWithOrder(new Order[] { Order.asc("monthId") });
	}
}
