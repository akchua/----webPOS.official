package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.prototype.CompanyPrototype;

public interface CompanyService
		extends Service<Company, Long>, CompanyPrototype {
	/**
	 * Find all company ordered by name.
	 * 
	 * @return the list of company
	 */
	List<Company> findAllOrderByName();
	
	Boolean isExistsByName(String name);
}
