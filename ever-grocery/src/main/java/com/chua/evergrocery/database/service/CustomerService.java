package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.database.prototype.CustomerPrototype;

public interface CustomerService 
		extends Service<Customer, Long>, CustomerPrototype {
	/**
	 * Find all customer ordered by name.
	 * 
	 * @return the list of customer
	 */
	List<Customer> findAllOrderByLastName();
	
	Boolean isExistsByFullName(String firstName, String lastName);
}
