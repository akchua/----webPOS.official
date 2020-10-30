package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.database.prototype.CustomerPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerDAO extends DAO<Customer, Long>, CustomerPrototype {

	/**
	 * Find all using the given orders.
	 * 
	 * @param orders the orders
	 * 
	 * @return the list of customer
	 */
	List<Customer> findAllWithOrder(Order[] orders);
	
	ObjectList<Customer> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Order[] orders);
	
	ObjectList<Customer> findAllWithPagingByCategoryWithOrder(int pageNumber, int resultsPerPage, Long customerCategoryId, Order[] orders);
}
