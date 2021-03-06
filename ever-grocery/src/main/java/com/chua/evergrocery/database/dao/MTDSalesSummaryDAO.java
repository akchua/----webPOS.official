package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.MTDSalesSummary;
import com.chua.evergrocery.database.prototype.MTDSalesSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 12, 2019
 */
public interface MTDSalesSummaryDAO extends DAO<MTDSalesSummary, Long>, MTDSalesSummaryPrototype {

	List<MTDSalesSummary> findAllWithOrder(Order[] orders);
	
	List<MTDSalesSummary> findByYearWithOrder(int year, Order[] orders);
}
