package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.Category;
import com.chua.evergrocery.objects.ObjectList;

public interface CategoryPrototype extends Prototype<Category, Long> {

	/**
	 * Find all category with paging.
	 * 
	 * @param pageNumber the page number
	 * @param resultsPerPage the results per page
	 * @param searchKey the search key
	 * 
	 * @return the object list of category
	 */
	ObjectList<Category> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
}
