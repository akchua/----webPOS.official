package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.beans.CustomerCategoryFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.CustomerCategory;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public interface CustomerCategoryHandler {

	ObjectList<CustomerCategory> getCustomerCategoryObjectList(Integer pageNumber, String searchKey);
	
	CustomerCategory getCustomerCategory(Long customerCategoryId);
	
	ResultBean createCustomerCategory(CustomerCategoryFormBean customerCategoryForm, String ip);
	
	ResultBean updateCustomerCategory(CustomerCategoryFormBean customerCategoryForm, String ip);
	
	ResultBean removeCustomerCategory(Long customerCategoryId, String ip);
	
	List<CustomerCategory> getCustomerCategoryList();
}
