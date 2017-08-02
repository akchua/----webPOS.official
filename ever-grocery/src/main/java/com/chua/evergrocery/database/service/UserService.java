package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.prototype.UserPrototype;

public interface UserService
		extends Service<User, Long>, UserPrototype {

	/**
	 * Find all user ordered by firstName.
	 * 
	 * @return the list of user
	 */
	List<User> findAllOrderByLastName();
	
	Boolean isExistsByUsername(String username);
}
