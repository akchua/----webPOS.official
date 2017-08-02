package com.chua.evergrocery.database.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.ProductDAO;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.service.ProductService;
import com.chua.evergrocery.objects.ObjectList;

@Service
public class ProductServiceImpl
		extends AbstractService<Product, Long, ProductDAO>
		implements ProductService 
{
	@Autowired
	protected ProductServiceImpl(ProductDAO dao) {
		super(dao);
	}
	
	@Override
	public ObjectList<Product> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey, Long companyId) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, searchKey, companyId);
	}
	
	@Override
	public ObjectList<Product> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey,
			Long companyId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, companyId, new Order[] { Order.asc("name") });
	}
	
	@Override
	public Boolean isExistsByName(String name) {
		return dao.findByName(StringUtils.trimToEmpty(name)) != null;
	}
}
