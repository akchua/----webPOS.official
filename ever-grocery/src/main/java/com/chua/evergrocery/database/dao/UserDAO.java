package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.prototype.UserPrototype;
import com.chua.evergrocery.enums.UserType;
import com.chua.evergrocery.objects.ObjectList;

public interface UserDAO extends DAO<User, Long>, UserPrototype {

	ObjectList<User> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
	
	ObjectList<User> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Order[] orders);
	
	List<User> findAllWithOrder(Order[] orders);
	
	List<User> findAllByUserTypeWithOrder(UserType[] userTypes, Order[] orders);
}
