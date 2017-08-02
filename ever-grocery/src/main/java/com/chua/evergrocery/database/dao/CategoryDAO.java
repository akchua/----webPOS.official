package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.Category;
import com.chua.evergrocery.database.prototype.CategoryPrototype;

public interface CategoryDAO extends DAO<Category, Long>, CategoryPrototype {

	/**
	 * Find all using the given orders.
	 * 
	 * @param orders the orders
	 * 
	 * @return the list of category
	 */
	List<Category> findAllWithOrder(Order[] orders);
	
	Category findByName(String name);
}
