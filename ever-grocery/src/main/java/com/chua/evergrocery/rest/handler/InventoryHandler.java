package com.chua.evergrocery.rest.handler;

import java.util.Date;
import java.util.List;

import com.chua.evergrocery.beans.InventoryBean;
import com.chua.evergrocery.beans.ResultBean;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 7, 2018
 */
public interface InventoryHandler {

	InventoryBean getProductInventory(Long productId);
	
	InventoryBean getProductInventory(Long productId, Date upTo);
	
	List<InventoryBean> getProductInventoryByCompany(Long companyId);
	
	List<InventoryBean> getProductInventoryByCompany(Long companyId, Date upTo);
	
	ResultBean generateInventory(Long companyId);
	
	void checkForStockAdjustment(Long customerOrderId);
}