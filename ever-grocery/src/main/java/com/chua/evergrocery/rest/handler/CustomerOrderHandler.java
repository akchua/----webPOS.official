package com.chua.evergrocery.rest.handler;

import java.util.Date;

import com.chua.evergrocery.beans.DiscountFormBean;
import com.chua.evergrocery.beans.PaymentsFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerOrderHandler {

	ObjectList<CustomerOrder> getCustomerOrderList(Integer pageNumber, String searchKey, Boolean showPaid, Integer daysAgo);
	
	ObjectList<CustomerOrder> getCashierCustomerOrderList(Integer pageNumber, String searchKey);
	
	ObjectList<CustomerOrder> getListingCustomerOrderList(Integer pageNumber, String searchKey);
	
	CustomerOrder getCustomerOrder(Long customerOrderId);
	
	CustomerOrder getCustomerOrderBySIN(Long serialInvoiceNumber);
	
	ResultBean createCustomerOrder();
	
	ResultBean removeCustomerOrder(Long customerOrderId);
	
	ResultBean applyDiscount(DiscountFormBean discountForm);
	
	ObjectList<CustomerOrderDetail> getCustomerOrderDetailList(Integer pageNumber, Long customerOrderId);
	
	ResultBean addItemByBarcode(String barcode, Long customerOrderId);
	
	ResultBean addItemByProductDetailId(Long productDetailId, Long customerOrderId, Float quantity);
	
	ResultBean removeCustomerOrderDetail(Long customerOrderDetailId);
	
	ResultBean changeCustomerOrderDetailQuantity(Long customerOrderDetailId, Float quantity);
	
	void refreshCustomerOrder(Long customerOrderId);
	
	ResultBean submitCustomerOrder(Long customerOrderId);
	
	ResultBean returnCustomerOrder(Long customerOrderId);
	
	ResultBean payCustomerOrder(PaymentsFormBean paymentsForm);
	
	ResultBean printCustomerOrderCopy(Long customerOrderId);

	ResultBean generateReport(SalesReportQueryBean salesReportQuery);
	
	void printReceipt(Long customerOrderId, String footer, Boolean original);
	
	ResultBean printZReading(Date readingDate);
	
	void endOfShift();
}
