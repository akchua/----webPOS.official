package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.CategoryDAO;
import com.chua.evergrocery.database.entity.Category;
import com.chua.evergrocery.database.service.CategoryService;
import com.chua.evergrocery.objects.ObjectList;

@Service
public class CategoryServiceImpl
		extends AbstractService<Category, Long, CategoryDAO>
		implements CategoryService {

	@Autowired
	protected CategoryServiceImpl(CategoryDAO dao) {
		super(dao);
	}
	
	@Override
	public ObjectList<Category> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, searchKey);
	}
	
	@Override
	public List<Category> findAllOrderByName() {
		return dao.findAllWithOrder(new Order[] { Order.asc("name") });
	}
	
	@Override
	public Boolean isExistsByName(String name) {
		return dao.findByName(StringUtils.trimToEmpty(name)) != null;
	}
}
