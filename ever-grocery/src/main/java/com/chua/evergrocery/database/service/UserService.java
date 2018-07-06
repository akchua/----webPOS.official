package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.prototype.UserPrototype;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 30, 2017
 */
public interface UserService
		extends Service<User, Long>, UserPrototype {

	boolean isExistByUsername(String username);
	
	ObjectList<User> findAllOrderByNameAndUserType(int pageNumber, int resultsPerPage, String searchKey);
	
	List<User> findAllOrderByName();
	
	List<User> findAllManagerOrderByName();
}
