package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.CustomerCategory;
import com.chua.evergrocery.database.prototype.CustomerCategoryPrototype;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public interface CustomerCategoryService extends Service<CustomerCategory, Long>, CustomerCategoryPrototype {

	List<CustomerCategory> findAllOrderByName();
	
	ObjectList<CustomerCategory> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey);
	
	Boolean isExistsByName(String name);
}
