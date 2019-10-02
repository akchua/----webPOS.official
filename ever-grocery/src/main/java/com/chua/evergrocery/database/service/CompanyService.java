package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.prototype.CompanyPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface CompanyService
		extends Service<Company, Long>, CompanyPrototype {
	/**
	 * Find all company ordered by name.
	 * 
	 * @return the list of company
	 */
	List<Company> findAllOrderByName();
	
	List<Company> findAllOrderByProfit();
	
	ObjectList<Company> findAllWithPagingOrderByProfit(int pageNumber, int resultsPerPage, String searchKey);
	
	Boolean isExistsByName(String name);
}
