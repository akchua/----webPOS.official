package com.chua.evergrocery.rest.handler.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.CustomerFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.database.service.CustomerService;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.CustomerHandler;

@Transactional
@Component
public class CustomerHandlerImpl implements CustomerHandler {

	@Autowired
	private CustomerService customerService;

	@Override
	public ObjectList<Customer> getCustomerObjectList(Integer pageNumber, String searchKey) {
		return customerService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public Customer getCustomer(Long customerId) {
		return customerService.find(customerId);
	}
	
	@Override
	public ResultBean createCustomer(CustomerFormBean customerForm) {
		final ResultBean result;
		
		if(!customerService.isExistsByFullName(customerForm.getFirstName(), customerForm.getLastName())) {
			final Customer customer = new Customer();
			setCustomer(customer, customerForm);
			
			result = new ResultBean();
			result.setSuccess(customerService.insert(customer) != null);
			if(result.getSuccess()) {
				result.setMessage("Customer successfully created.");
			} else {
				result.setMessage("Failed to create customer.");
			}
		} else {
			result = new ResultBean(false, "Customer \"" + customerForm.getLastName() + ", " + customerForm.getFirstName() + "\" already exists!");
		}
		
		return result;
	}
	
	@Override
	public ResultBean updateCustomer(CustomerFormBean customerForm) {
		final ResultBean result;
		
		final Customer customer = customerService.find(customerForm.getId());
		if(customer != null) {
			if(!(StringUtils.trimToEmpty(customer.getFirstName()).equalsIgnoreCase(customerForm.getFirstName()) &&
					StringUtils.trimToEmpty(customer.getLastName()).equalsIgnoreCase(customerForm.getLastName())) &&
					customerService.isExistsByFullName(customerForm.getFirstName(), customerForm.getLastName())) {
				result = new ResultBean(false, "Customer \"" + customerForm.getLastName() + ", " + customerForm.getFirstName() + "\" already exists!");
			} else {
				setCustomer(customer, customerForm);
				
				result = new ResultBean();
				result.setSuccess(customerService.update(customer));
				if(result.getSuccess()) {
					result.setMessage("Customer successfully updated.");
				} else {
					result.setMessage("Failed to update customer.");
				}
			}
		} else {
			result = new ResultBean(false, "Customer not found.");
		}
		
		return result;
	}

	@Override
	public ResultBean removeCustomer(Long customerId) {
		final ResultBean result;
		
		final Customer customer = customerService.find(customerId);
		if(customer != null) {
			result = new ResultBean();
			
			result.setSuccess(customerService.delete(customer));
			if(result.getSuccess()) {
				result.setMessage("Successfully removed Customer \"" + customer.getLastName() + ", " + customer.getFirstName() + "\".");
			} else {
				result.setMessage("Failed to remove Customer \"" + customer.getLastName() + ", " + customer.getFirstName() + "\".");
			}
		} else {
			result = new ResultBean(false, "Customer not found.");
		}
		
		return result;
	}
	
	@Override
	public List<Customer> getCustomerList() {
		return customerService.findAllOrderByLastName();
	}
	
	private void setCustomer(Customer customer, CustomerFormBean customerForm) {
		customer.setFirstName(customerForm.getFirstName());
		customer.setLastName(customerForm.getLastName());
		customer.setAddress(customerForm.getAddress());
	}
}
