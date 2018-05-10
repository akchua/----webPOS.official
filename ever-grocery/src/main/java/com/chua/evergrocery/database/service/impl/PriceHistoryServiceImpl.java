package com.chua.evergrocery.database.service.impl;

import java.util.Calendar;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.PriceHistoryDAO;
import com.chua.evergrocery.database.entity.PriceHistory;
import com.chua.evergrocery.database.service.PriceHistoryService;
import com.chua.evergrocery.enums.PriceHistoryType;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 10, 2018
 */
@Service
public class PriceHistoryServiceImpl
		extends AbstractService<PriceHistory, Long, PriceHistoryDAO>
		implements PriceHistoryService {

	@Autowired
	protected PriceHistoryServiceImpl(PriceHistoryDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<PriceHistory> findAllSaleTypeWithin30DaysOrderByCreatedOn(int pageNumber, int resultsPerPage) {
		Calendar thirtyDaysAgo = Calendar.getInstance();
		thirtyDaysAgo.add(Calendar.DAY_OF_MONTH, -30);
		
		return dao.findAllWithPagingAndOrderLimitByDate(pageNumber, resultsPerPage, PriceHistoryType.SALE, thirtyDaysAgo.getTime(), new Order[] { Order.desc("createdOn") });
	}
}
