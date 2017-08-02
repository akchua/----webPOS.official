package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.UserDAO;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.objects.ObjectList;

@Service
public class UserServiceImpl 
		extends AbstractService<User, Long, UserDAO>
		implements UserService {

	@Autowired
	protected UserServiceImpl(UserDAO dao) {
		super(dao);
	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		return dao.findByUsernameAndPassword(username, password);
	}
	
	@Override
	public ObjectList<User> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, searchKey);
	}

	@Override
	public List<User> findAllOrderByLastName() {
		return dao.findAllWithOrder(new Order[] { Order.asc("lastName") });
	}

	@Override
	public Boolean isExistsByUsername(String username) {
		return dao.findByUsername(StringUtils.trimToEmpty(username)) != null;
	}
}
