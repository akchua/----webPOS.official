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
	public ObjectList<Customer> findAllWithPagingByCategoryOrderByLatest(int pageNumber, int resultsPerPage,
			Long customerCategoryId) {
		return dao.findAllWithPagingByCategoryWithOrder(pageNumber, resultsPerPage, customerCategoryId, new Order[] { Order.asc("name") });
	}
	
	@Override
	public List<Customer> findAllOrderByLastName() {
		return dao.findAllWithOrder(new Order[] { Order.asc("lastName") });
	}

	@Override
	public Customer findByCardId(String cardId) {
		return dao.findByCardId(StringUtils.trimToEmpty(cardId));
	}

	@Override
	public Boolean isExistsByCardId(String cardId) {
		return dao.findByCardId(StringUtils.trimToEmpty(cardId)) != null;
	}

	@Override
	public Customer findByCode(String code) {
		return dao.findByCode(StringUtils.trimToEmpty(code));
	}

	@Override
	public Boolean isExistsByCode(String code) {
		return dao.findByCode(StringUtils.trimToEmpty(code)) != null;
	}
}
