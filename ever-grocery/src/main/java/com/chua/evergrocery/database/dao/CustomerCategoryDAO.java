package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.CustomerCategory;
import com.chua.evergrocery.database.prototype.CustomerCategoryPrototype;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public interface CustomerCategoryDAO extends DAO<CustomerCategory, Long>, CustomerCategoryPrototype {

	List<CustomerCategory> findAllWithOrder(Order[] orders);
	
	ObjectList<CustomerCategory> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Order[] orders);
	
	CustomerCategory findByName(String name);
}
