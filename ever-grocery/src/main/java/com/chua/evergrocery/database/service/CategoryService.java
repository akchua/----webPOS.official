package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.Category;
import com.chua.evergrocery.database.prototype.CategoryPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface CategoryService 
		extends Service<Category, Long>, CategoryPrototype {
	/**
	 * Find all category ordered by name.
	 * 
	 * @return the list of category
	 */
	List<Category> findAllOrderByName();
	
	ObjectList<Category> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey);
	
	Boolean isExistsByName(String name);
}
