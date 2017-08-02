package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.prototype.UserPrototype;

public interface UserDAO extends DAO<User, Long>, UserPrototype {

	/**
	 * Find all using the given orders.
	 * 
	 * @param orders the orders
	 * 
	 * @return the list of user
	 */
	List<User> findAllWithOrder(Order[] orders);
	
	User findByUsername(String username);
}
