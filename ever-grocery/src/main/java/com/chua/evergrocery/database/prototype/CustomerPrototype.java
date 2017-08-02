package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerPrototype extends Prototype<Customer, Long> {

	/**
	 * Find all customer with paging.
	 * 
	 * @param pageNumber the page number
	 * @param resultsPerPage the results per page
	 * @param searchKey the search key
	 * 
	 * @return the object list of customer
	 */
	ObjectList<Customer> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
}
