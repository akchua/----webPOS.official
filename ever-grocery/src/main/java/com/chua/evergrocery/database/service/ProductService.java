package com.chua.evergrocery.database.service;

import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.prototype.ProductPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface ProductService
		extends Service<Product, Long>, ProductPrototype {
	
	Boolean isExistsByName(String name);
	
	ObjectList<Product> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey, Long companyId);
}
