package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.objects.ObjectList;

public interface UserPrototype {

	User findByUsernameAndPassword(String username, String password);
	
	/**
	 * Find all user with paging.
	 * 
	 * @param pageNumber the page number
	 * @param resultsPerPage the results per page
	 * @param searchKey the search key
	 * 
	 * @return the object list of user
	 */
	ObjectList<User> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
}
