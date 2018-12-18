package com.chua.evergrocery.database.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.DailySalesSummary;
import com.chua.evergrocery.database.prototype.DailySalesSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
public interface DailySalesSummaryDAO extends DAO<DailySalesSummary, Long>, DailySalesSummaryPrototype {
	
	List<DailySalesSummary> findAllWithOrder(Order[] orders);

	List<DailySalesSummary> findByRangeWithOrder(Date startDate, Date endDate, Order[] orders);
}
