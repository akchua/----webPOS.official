package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.Brand;
import com.chua.evergrocery.objects.ObjectList;

public interface BrandPrototype extends Prototype<Brand, Long> {

	/**
	 * Find all brand with paging.
	 * 
	 * @param pageNumber the page number
	 * @param resultsPerPage the results per page
	 * @param searchKey the search key
	 * 
	 * @return the object list of brand
	 */
	ObjectList<Brand> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
}
