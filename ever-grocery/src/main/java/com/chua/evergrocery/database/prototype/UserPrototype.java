package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.objects.ObjectList;

public interface UserPrototype {

	User findByUsernameAndPassword(String username, String password);
	
	User findByUsername(String username);
	
	User findByUsernameOrEmail(String username, String emailAddress);
}
