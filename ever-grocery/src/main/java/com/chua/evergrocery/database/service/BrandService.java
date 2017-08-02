package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.Brand;
import com.chua.evergrocery.database.prototype.BrandPrototype;

public interface BrandService
		extends Service<Brand, Long>, BrandPrototype {
	/**
	 * Find all brand ordered by name.
	 * 
	 * @return the list of brand
	 */
	List<Brand> findAllOrderByName();
	
	Boolean isExistsByName(String name);
}
