package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.Brand;
import com.chua.evergrocery.database.prototype.BrandPrototype;

public interface BrandDAO extends DAO<Brand, Long>, BrandPrototype {

	/**
	 * Find all using the given orders.
	 * 
	 * @param orders the orders
	 * 
	 * @return the list of brand
	 */
	List<Brand> findAllWithOrder(Order[] orders);
	
	Brand findByName(String name);
}
