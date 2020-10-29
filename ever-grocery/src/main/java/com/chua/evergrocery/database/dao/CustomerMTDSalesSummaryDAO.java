package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.CustomerMTDSalesSummary;
import com.chua.evergrocery.database.prototype.CustomerMTDSalesSummaryPrototype;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public interface CustomerMTDSalesSummaryDAO
		extends DAO<CustomerMTDSalesSummary, Long>, CustomerMTDSalesSummaryPrototype {

	List<CustomerMTDSalesSummary> findAllByCustomerWithOrder(Long customerId, Order[] orders);
}
