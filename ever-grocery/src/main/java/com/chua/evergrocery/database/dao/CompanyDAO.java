package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.prototype.CompanyPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface CompanyDAO extends DAO<Company, Long>, CompanyPrototype {

	/**
	 * Find all using the given orders.
	 * 
	 * @param orders the orders
	 * 
	 * @return the list of company
	 */
	List<Company> findAllWithOrder(Order[] orders);
	
	ObjectList<Company> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Order[] orders);
	
	Company findByName(String name);
}
