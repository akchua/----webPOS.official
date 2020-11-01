package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.beans.CustomerFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerHandler {

	ObjectList<Customer> getCustomerObjectList(Integer pageNumber, String searchKey);
	
	ObjectList<Customer> getCustomerListByCategory(Integer pageNumber, Long customerCategoryId);
	
	ObjectList<Customer> getOutOfScheduleCustomerList(Integer pageNumber);
	
	Customer getCustomer(Long customerId);
	
	ResultBean createCustomer(CustomerFormBean customerForm, String ip);
	
	ResultBean updateCustomer(CustomerFormBean customerForm, String ip);
	
	ResultBean removeCustomer(Long customerId, String ip);
	
	List<Customer> getCustomerList();
}
