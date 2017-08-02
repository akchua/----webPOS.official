package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.beans.CustomerFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerHandler {

	ObjectList<Customer> getCustomerObjectList(Integer pageNumber, String searchKey);
	
	Customer getCustomer(Long customerId);
	
	ResultBean createCustomer(CustomerFormBean customerForm);
	
	ResultBean updateCustomer(CustomerFormBean customerForm);
	
	ResultBean removeCustomer(Long customerId);
	
	List<Customer> getCustomerList();
}
