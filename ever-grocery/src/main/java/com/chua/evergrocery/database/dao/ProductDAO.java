package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.prototype.ProductPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface ProductDAO extends DAO<Product, Long>, ProductPrototype {

	Product findByName(String name);
	
	Product findByCode(String code);
	
	ObjectList<Product> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Boolean promoOnly, Long companyId, Long categoryId, Order[] orders);
	
	List<Product> findAllByCompanyWithOrder(Long companyId, Order[] orders);
	
	List<Product> findAllByCategoryWithOrder(Long categoryId, Order[] orders);
}
