package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.database.prototype.CustomerPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerService 
		extends Service<Customer, Long>, CustomerPrototype {
	
	ObjectList<Customer> findAllWithPagingByCategoryOrderByLatest(int pageNumber, int resultsPerPage, Long customerCategoryId);
	
	/**
	 * Find all customer ordered by name.
	 * 
	 * @return the list of customer
	 */
	List<Customer> findAllOrderByLastName();
	
	Boolean isExistsByCardId(String cardId);
	
	Boolean isExistsByCode(String code);
}
