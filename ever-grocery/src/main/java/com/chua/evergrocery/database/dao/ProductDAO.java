package com.chua.evergrocery.database.dao;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.prototype.ProductPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface ProductDAO extends DAO<Product, Long>, ProductPrototype {

	Product findByName(String name);
	
	ObjectList<Product> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Long companyId, Order[] orders);
}
