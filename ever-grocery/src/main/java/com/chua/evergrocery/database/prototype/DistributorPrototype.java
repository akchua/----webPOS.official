package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.Distributor;
import com.chua.evergrocery.objects.ObjectList;

public interface DistributorPrototype extends Prototype<Distributor, Long> {

	/**
	 * Find all distributor with paging.
	 * 
	 * @param pageNumber the page number
	 * @param resultsPerPage the results per page
	 * @param searchKey the search key
	 * 
	 * @return the object list of distributor
	 */
	ObjectList<Distributor> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
}
