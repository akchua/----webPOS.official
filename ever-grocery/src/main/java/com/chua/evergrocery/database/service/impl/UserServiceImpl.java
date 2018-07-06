package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.UserDAO;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.enums.UserType;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 30, 2017
 */
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
	public boolean isExistByUsername(String username) {
		return dao.findByUsername(username) != null;
	}

	@Override
	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}
	
	@Override
	public User findByUsernameOrEmail(String username, String emailAddress) {
		return dao.findByUsernameOrEmail(username, emailAddress);
	}

	@Override
	public ObjectList<User> findAllOrderByNameAndUserType(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, new Order[] { Order.asc("userType"), Order.asc("lastName"), Order.asc("firstName") });
	}

	@Override
	public List<User> findAllOrderByName() {
		return dao.findAllWithOrder(new Order[] { Order.asc("lastName"), Order.asc("firstName")});
	}

	@Override
	public List<User> findAllManagerOrderByName() {
		return dao.findAllByUserTypeWithOrder(new UserType[] { UserType.MANAGER, UserType.ADMINISTRATOR }, new Order[] { Order.asc("lastName"), Order.asc("firstName") });
	}
}
