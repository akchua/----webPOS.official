package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.CustomerCategoryDAO;
import com.chua.evergrocery.database.entity.CustomerCategory;
import com.chua.evergrocery.database.service.CustomerCategoryService;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
@Service
public class CustomerCategoryServiceImpl
		extends AbstractService<CustomerCategory, Long, CustomerCategoryDAO>
		implements CustomerCategoryService {

	@Autowired
	protected CustomerCategoryServiceImpl(CustomerCategoryDAO dao) {
		super(dao);
	}
	
	@Override
	public ObjectList<CustomerCategory> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, searchKey);
	}
	
	@Override
	public ObjectList<CustomerCategory> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, new Order[] { Order.asc("name") });
	}
	
	@Override
	public List<CustomerCategory> findAllOrderByName() {
		return dao.findAllWithOrder(new Order[] { Order.asc("name") });
	}
	
	@Override
	public Boolean isExistsByName(String name) {
		return dao.findByName(StringUtils.trimToEmpty(name)) != null;
	}
}
