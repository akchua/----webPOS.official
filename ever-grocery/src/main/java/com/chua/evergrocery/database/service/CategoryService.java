package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.Category;
import com.chua.evergrocery.database.prototype.CategoryPrototype;

public interface CategoryService 
		extends Service<Category, Long>, CategoryPrototype {
	/**
	 * Find all category ordered by name.
	 * 
	 * @return the list of category
	 */
	List<Category> findAllOrderByName();
	
	Boolean isExistsByName(String name);
}
