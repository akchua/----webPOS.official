package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.objects.ObjectList;

public interface CompanyPrototype extends Prototype<Company, Long> {

	/**
	 * Find all company with paging.
	 * 
	 * @param pageNumber the page number
	 * @param resultsPerPage the results per page
	 * @param searchKey the search key
	 * 
	 * @return the object list of company
	 */
	ObjectList<Company> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
}
