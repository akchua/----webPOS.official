package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.CustomerCategoryMTDSalesSummary;
import com.chua.evergrocery.database.prototype.CustomerCategoryMTDSalesSummaryPrototype;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public interface CustomerCategoryMTDSalesSummaryDAO
		extends DAO<CustomerCategoryMTDSalesSummary, Long>, CustomerCategoryMTDSalesSummaryPrototype {

	List<CustomerCategoryMTDSalesSummary> findAllByCustomerCategoryWithOrder(Long customerCategoryId, Order[] orders);
}
