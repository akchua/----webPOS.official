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

	ObjectList<CustomerOrder> getCustomerOrderListByCustomer(Integer pageNumber, Long customerId);
	
	ObjectList<CustomerOrder> getCashierCustomerOrderList(Integer pageNumber, String searchKey);
	
	ObjectList<CustomerOrder> getListingCustomerOrderList(Integer pageNumber, String searchKey);
	
	CustomerOrder getCustomerOrder(Long customerOrderId);
	
	CustomerOrder getCustomerOrderBySIN(Long serialInvoiceNumber);
	
	ResultBean createCustomerOrder(String ip);
	
	ResultBean removeCustomerOrder(Long customerOrderId, String ip);
	
	ResultBean setCustomer(Long customerOrderId, String customerCardId, String ip);
	
	ResultBean setCustomerByCode(Long customerOrderId, String customerCode, String ip);
	
	ResultBean removeCustomer(Long customerOrderId, String ip);
	
	ResultBean applyDiscount(DiscountFormBean discountForm, String ip);
	
	ObjectList<CustomerOrderDetail> getCustomerOrderDetailList(Integer pageNumber, Long customerOrderId);
	
	ObjectList<CustomerOrderDetail> getCustomerOrderDetailListByProduct(Integer pageNumber, Long productId);
	
	ResultBean addItemByBarcode(String barcode, Long customerOrderId);
	
	ResultBean addItemByProductDetailId(Long productDetailId, Long customerOrderId, Float quantity);
	
	ResultBean removeCustomerOrderDetail(Long customerOrderDetailId);
	
	ResultBean changeCustomerOrderDetailQuantity(Long customerOrderDetailId, Float quantity);
	
	void refreshCustomerOrder(Long customerOrderId);
	
	ResultBean submitCustomerOrder(Long customerOrderId, String ip);
	
	ResultBean returnCustomerOrder(Long customerOrderId, String ip);
	
	ResultBean payCustomerOrder(PaymentsFormBean paymentsForm, String ip);
	
	ResultBean printCustomerOrderCopy(Long customerOrderId);

	ResultBean generateReport(SalesReportQueryBean salesReportQuery);
	
	ResultBean generateBackendReport(Date dateFrom, Date dateTo, String ip);
	
	void printReceipt(Long customerOrderId, String footer, Boolean original, String ip);
	
	ResultBean printZReading(Date readingDate, String ip);
	
	void endOfShift(String ip);
	
	ResultBean updatePackageCount(Long customerOrderId, Integer cartonCount, Integer plasticCount, Integer bagCount);
}
