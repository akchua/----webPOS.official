package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.CustomerDAO;
import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.database.service.CustomerService;
import com.chua.evergrocery.objects.ObjectList;

@Service
public class CustomerServiceImpl 
		extends AbstractService<Customer, Long, CustomerDAO>
		implements CustomerService {
	
	@Autowired
	protected CustomerServiceImpl(CustomerDAO dao) {
		super(dao);
	}
	
	@Override
	public ObjectList<Customer> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, searchKey);
	}
	
	@Override
	public List<Customer> findAllOrderByLastName() {
		return dao.findAllWithOrder(new Order[] { Order.asc("lastName") });
	}

	@Override
	public Boolean isExistsByFullName(String firstName, String lastName) {
		return dao.findByFullName(StringUtils.trimToEmpty(firstName), StringUtils.trimToEmpty(lastName)) != null;
	}
}
