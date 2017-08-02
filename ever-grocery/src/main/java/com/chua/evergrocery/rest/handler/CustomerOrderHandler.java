package com.chua.evergrocery.rest.handler;

import com.chua.evergrocery.beans.CustomerOrderFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerOrderHandler {

	ObjectList<CustomerOrder> getCustomerOrderList(Integer pageNumber, String searchKey, Boolean showPaid, Integer daysAgo);
	
	CustomerOrder getCustomerOrder(Long customerOrderId);
	
	ResultBean createCustomerOrder(CustomerOrderFormBean customerOrderForm);
	
	ResultBean updateCustomerOrder(CustomerOrderFormBean customerOrderForm);
	
	ResultBean removeCustomerOrder(Long customerOrderId);
	
	ObjectList<CustomerOrderDetail> getCustomerOrderDetailList(Integer pageNumber, Long customerOrderId);
	
	ResultBean addItemByBarcode(String barcode, Long customerOrderId);
	
	ResultBean addItemByProductDetailId(Long productDetailId, Long customerOrderId, Float quantity);
	
	ResultBean removeCustomerOrderDetail(Long customerOrderDetailId);
	
	ResultBean changeCustomerOrderDetailQuantity(Long customerOrderDetailId, Float quantity);
	
	void refreshCustomerOrder(Long customerOrderId);
	
	ResultBean payCustomerOrder(Long customerOrderId, Float cash);
	
	ResultBean printCustomerOrderList(Long customerOrderId);
	
	void printReceipt(Long customerOrderId, Float cash);
}
