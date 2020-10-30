package com.chua.evergrocery.rest.handler.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.annotations.CheckAuthority;
import com.chua.evergrocery.beans.CustomerCategoryFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.CustomerCategory;
import com.chua.evergrocery.database.service.CustomerCategoryService;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.ActivityLogHandler;
import com.chua.evergrocery.rest.handler.CustomerCategoryHandler;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
@Transactional
@Component
public class CustomerCategoryHandlerImpl implements CustomerCategoryHandler {

	@Autowired
	private CustomerCategoryService customerCategoryService;
	
	@Autowired
	private ActivityLogHandler activityLogHandler;

	@Override
	public ObjectList<CustomerCategory> getCustomerCategoryObjectList(Integer pageNumber, String searchKey) {
		return customerCategoryService.findAllWithPagingOrderByRank(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public CustomerCategory getCustomerCategory(Long customerCategoryId) {
		return customerCategoryService.find(customerCategoryId);
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 3)
	public ResultBean createCustomerCategory(CustomerCategoryFormBean customerCategoryForm, String ip) {
		final ResultBean result;
		
		if(!customerCategoryService.isExistsByName(customerCategoryForm.getName())) {
			final CustomerCategory customerCategory = new CustomerCategory();
			setCustomerCategory(customerCategory, customerCategoryForm);
			customerCategory.setSaleValuePercentage(0.0f);
			customerCategory.setProfitPercentage(0.0f);
			customerCategory.setPreviousProfitRank(0);
			customerCategory.setCurrentProfitRank(0);
			
			result = new ResultBean();
			result.setSuccess(customerCategoryService.insert(customerCategory) != null);
			if(result.getSuccess()) {
				result.setMessage("Customer category successfully created.");
				activityLogHandler.myLog("created a customer category : " + customerCategory.getId() + " - " + customerCategory.getName(), ip);
			} else {
				result.setMessage("Failed to create customer category.");
			}
		} else {
			result = new ResultBean(false, "Customer category \"" + customerCategoryForm.getName() + "\" already exists!");
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 3)
	public ResultBean updateCustomerCategory(CustomerCategoryFormBean customerCategoryForm, String ip) {
		final ResultBean result;
		
		final CustomerCategory customerCategory = customerCategoryService.find(customerCategoryForm.getId());
		if(customerCategory != null) {
			if(!(StringUtils.trimToEmpty(customerCategory.getName()).equalsIgnoreCase(customerCategoryForm.getName())) &&
					customerCategoryService.isExistsByName(customerCategoryForm.getName())) {
				result = new ResultBean(false, "CustomerCategory \"" + customerCategoryForm.getName() + "\" already exists!");
			} else {
				setCustomerCategory(customerCategory, customerCategoryForm);
				
				result = new ResultBean();
				result.setSuccess(customerCategoryService.update(customerCategory));
				if(result.getSuccess()) {
					result.setMessage("CustomerCategory successfully updated.");
					activityLogHandler.myLog("updated a customerCategory : " + customerCategory.getId() + " - " + customerCategory.getName(), ip);
				} else {
					result.setMessage("Failed to update customerCategory.");
				}
			}
		} else {
			result = new ResultBean(false, "CustomerCategory not found.");
		}
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean removeCustomerCategory(Long customerCategoryId, String ip) {
		final ResultBean result;
		
		final CustomerCategory customerCategory = customerCategoryService.find(customerCategoryId);
		if(customerCategory != null) {
			
			result = new ResultBean();
			
			result.setSuccess(customerCategoryService.delete(customerCategory));
			if(result.getSuccess()) {
				result.setMessage("Successfully removed CustomerCategory \"" + customerCategory.getName() + "\".");
				activityLogHandler.myLog("removed a customerCategory : " + customerCategory.getId() + " - " + customerCategory.getName(), ip);
			} else {
				result.setMessage("Failed to remove CustomerCategory \"" + customerCategory.getName() + "\".");
			}
		} else {
			result = new ResultBean(false, "CustomerCategory not found.");
		}
		
		return result;
	}
	
	@Override
	public List<CustomerCategory> getCustomerCategoryList() {
		return customerCategoryService.findAllOrderByName();
	}
	
	private void setCustomerCategory(CustomerCategory customerCategory, CustomerCategoryFormBean customerCategoryForm) {
		customerCategory.setName(customerCategoryForm.getName().trim());
	}
}
