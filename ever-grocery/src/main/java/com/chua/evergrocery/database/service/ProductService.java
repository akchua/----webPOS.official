package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.prototype.ProductPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface ProductService
		extends Service<Product, Long>, ProductPrototype {
	
	Boolean isExistsByName(String name);
	
	Boolean isExistsByCode(String code);
	
	ObjectList<Product> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey, Long companyId, Long categoryId);
	
	ObjectList<Product> findAllWithPagingOrderByProfit(int pageNumber, int resultsPerPage, String searchKey, Long companyId);
	
	List<Product> findAllByCompanyOrderByName(Long companyId);
	
	List<Product> findAllByCompanyOrderByProfit(Long companyId);
}
