package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.database.prototype.CustomerPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerService 
		extends Service<Customer, Long>, CustomerPrototype {
	
	ObjectList<Customer> findAllWithPagingOrderByRank(int pageNumber, int resultsPerPage, String searchKey);
	
	ObjectList<Customer> findAllWithPagingByCategoryOrderByName(int pageNumber, int resultsPerPage, Long customerCategoryId);
	
	ObjectList<Customer> findAllWithPagingByCategoryOrderByRank(int pageNumber, int resultsPerPage, Long customerCategoryId);
	
	ObjectList<Customer> findAllOutOfScheduleOrderByFlagDate(int pageNumber, int resultsPerPage);
	
	/**
	 * Find all customer ordered by name.
	 * 
	 * @return the list of customer
	 */
	List<Customer> findAllOrderByLastName();
	
	List<Customer> findAllOrderByProfit();
	
	Boolean isExistsByCardId(String cardId);
	
	Boolean isExistsByCode(String code);
}
