package com.chua.evergrocery.rest.handler.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.CustomerFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.database.service.CustomerCategoryService;
import com.chua.evergrocery.database.service.CustomerService;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.ActivityLogHandler;
import com.chua.evergrocery.rest.handler.CustomerHandler;
import com.chua.evergrocery.rest.validator.CustomerFormValidator;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.Html;

@Transactional
@Component
public class CustomerHandlerImpl implements CustomerHandler {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerCategoryService customerCategoryService;
	
	@Autowired
	private ActivityLogHandler activityLogHandler;
	
	@Autowired
	private CustomerFormValidator customerFormValidator;
	
	@Override
	public ObjectList<Customer> getCustomerObjectList(Integer pageNumber, String searchKey) {
		return customerService.findAllWithPagingOrderByRank(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public ObjectList<Customer> getCustomerListByCategory(Integer pageNumber, Long customerCategoryId) {
		return customerService.findAllWithPagingByCategoryOrderByRank(pageNumber, UserContextHolder.getItemsPerPage(), customerCategoryId);
	}

	@Override
	public ObjectList<Customer> getOutOfScheduleCustomerList(Integer pageNumber) {
		return customerService.findAllOutOfScheduleOrderByFlagDate(pageNumber, UserContextHolder.getItemsPerPage());
	}
	
	@Override
	public Customer getCustomer(Long customerId) {
		return customerService.find(customerId);
	}
	
	@Override
	public ResultBean createCustomer(CustomerFormBean customerForm, String ip) {
		final ResultBean result;
		final Map<String, String> errors = customerFormValidator.validate(customerForm);
		
		if(errors.isEmpty()) {
			if(!customerService.isExistsByCode(customerForm.getCode())) {
				final Customer customer = new Customer();
				setCustomer(customer, customerForm);
				customer.setTotalPoints(0.0f);
				customer.setUsedPoints(0.0f);
				customer.setLastPurchase(DateUtil.getDefaultDate());
				customer.setSaleValuePercentage(0.0f);
				customer.setProfitPercentage(0.0f);
				customer.setPreviousProfitRank(0);
				customer.setCurrentProfitRank(0);
				customer.setAverageSchedule(0.0f);
				customer.setOosFlag(Boolean.FALSE);
				customer.setOosLastFlag(DateUtil.getDefaultDate());
				
				result = new ResultBean();
				result.setSuccess(customerService.insert(customer) != null);
				if(result.getSuccess()) {
					result.setMessage("Customer successfully created.");
					activityLogHandler.myLog("created a customer  : " + customer.getId() + " - " + customer.getName(), ip);
				} else {
					result.setMessage("Failed to create customer.");
				}
			} else {
				result = new ResultBean(false, "Customer \"" + customerForm.getCode() + "\" already exists!");
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "");
			result.addToExtras("errors", errors);
		}
		
		return result;
	}
	
	@Override
	public ResultBean updateCustomer(CustomerFormBean customerForm, String ip) {
		final ResultBean result;
		
		final Customer customer = customerService.find(customerForm.getId());
		if(customer != null) {
			final Map<String, String> errors = customerFormValidator.validate(customerForm);
			
			if(errors.isEmpty()) {
				if(!(StringUtils.trimToEmpty(customer.getCode()).equalsIgnoreCase(customerForm.getCode())) &&
						customerService.isExistsByCode(customerForm.getCode())) {
					result = new ResultBean(false, "Customer \"" + customerForm.getCode() + "\" already exists!");
				} else {
					setCustomer(customer, customerForm);
					
					result = new ResultBean();
					result.setSuccess(customerService.update(customer));
					if(result.getSuccess()) {
						result.setMessage("Customer successfully updated.");
						activityLogHandler.myLog("updated a customer : " + customer.getId() + " - " + customer.getName(), ip);
					} else {
						result.setMessage("Failed to update customer.");
					}
				}
			} else {
				result = new ResultBean(Boolean.FALSE, "");
				result.addToExtras("errors", errors);
			}
		} else {
			result = new ResultBean(false, "Customer not found.");
		}
		
		return result;
	}

	@Override
	public ResultBean removeCustomer(Long customerId, String ip) {
		final ResultBean result;
		
		final Customer customer = customerService.find(customerId);
		if(customer != null) {
			if(customer.getAvailablePoints().equals(0.0f)) {
				result = new ResultBean();
				
				result.setSuccess(customerService.delete(customer));
				if(result.getSuccess()) {
					result.setMessage("Successfully removed Customer \"" + customer.getName() + "\".");
					activityLogHandler.myLog("removed a customer : " + customer.getId() + " - " + customer.getName(), ip);
				} else {
					result.setMessage("Failed to remove Customer \"" + customer.getName() + "\".");
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line("You are " + Html.text(Color.RED, "NOT ALLOWED") + " to delete a customer account with rewards points remaining.")
						+ Html.line("Please contact your database administrator for assistance."));
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
		customer.setCustomerCategory(customerCategoryService.find(customerForm.getCustomerCategoryId()));
		customer.setName(customerForm.getName().trim());
		customer.setStoreName(customerForm.getStoreName().trim());
		customer.setCode(customerForm.getCode().trim());
		customer.setContactNumber(customerForm.getContactNumber() != null ? customerForm.getContactNumber().trim() : "");
		customer.setAddress(customerForm.getAddress().trim());
		customer.setCardId(customerForm.getCardId() != null ? customerForm.getCardId().trim() : "");
	}
}
