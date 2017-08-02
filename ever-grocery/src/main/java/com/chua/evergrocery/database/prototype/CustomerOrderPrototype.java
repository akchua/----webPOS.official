package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerOrderPrototype {

	/**
	 * Find all customerOrder with paging.
	 * 
	 * @param pageNumber the page number
	 * @param resultsPerPage the results per page
	 * @param searchKey the search key
	 * @param companyId the company id
	 * 
	 * @return the object list of customerOrder
	 */
	ObjectList<CustomerOrder> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey, Status[] status, Integer daysAgo);
}
