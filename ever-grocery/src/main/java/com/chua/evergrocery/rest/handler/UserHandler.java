package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.UserFormBean;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.objects.ObjectList;

public interface UserHandler {

	ObjectList<User> getUserObjectList(Integer pageNumber, String searchKey);
	
	User getUser(Long userId);
	
	ResultBean createUser(UserFormBean userForm);
	
	ResultBean updateUser(UserFormBean userForm);
	
	ResultBean removeUser(Long userId);
	
	List<User> getUserList();
	
	List<String> getUserTypeList();
}
