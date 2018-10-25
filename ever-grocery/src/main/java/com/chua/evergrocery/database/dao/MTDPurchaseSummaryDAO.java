package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.MTDPurchaseSummary;
import com.chua.evergrocery.database.prototype.MTDPurchaseSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
public interface MTDPurchaseSummaryDAO extends DAO<MTDPurchaseSummary, Long>, MTDPurchaseSummaryPrototype {

	List<MTDPurchaseSummary> findAllWithOrder(Order[] orders);
	
	List<MTDPurchaseSummary> findByYearWithOrder(int year, Order[] orders);
}
