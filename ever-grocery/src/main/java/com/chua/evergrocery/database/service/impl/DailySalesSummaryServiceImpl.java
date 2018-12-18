package com.chua.evergrocery.database.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.DailySalesSummaryDAO;
import com.chua.evergrocery.database.entity.DailySalesSummary;
import com.chua.evergrocery.database.service.DailySalesSummaryService;
import com.chua.evergrocery.utility.DateUtil;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
@Service
public class DailySalesSummaryServiceImpl
		extends AbstractService<DailySalesSummary, Long, DailySalesSummaryDAO>
		implements DailySalesSummaryService {

	@Autowired
	protected DailySalesSummaryServiceImpl(DailySalesSummaryDAO dao) {
		super(dao);
	}

	@Override
	public List<DailySalesSummary> findAllOrderBySalesDate() {
		return dao.findAllWithOrder(new Order[] { Order.asc("salesDate") });
	}

	@Override
	public List<DailySalesSummary> findByRangeOrderBySalesDate(Date startDate, Date endDate) {
		return dao.findByRangeWithOrder(startDate, endDate, new Order[] { Order.asc("salesDate") });
	}

	@Override
	public DailySalesSummary findBySalesDate(Date salesDate) {
		return dao.findBySalesDate(DateUtil.floorDay(salesDate));
	}
}
