package com.chua.evergrocery.database.service;

import java.util.Date;
import java.util.List;

import com.chua.evergrocery.beans.SalesSummaryBean;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.database.prototype.CustomerOrderDetailPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerOrderDetailService 
		extends Service<CustomerOrderDetail, Long>, CustomerOrderDetailPrototype {

	ObjectList<CustomerOrderDetail> findAllWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage, long customerOrderId);
	
	List<CustomerOrderDetail> findAllByCustomerOrderIdOrderByProductName(Long customerOrderId);
	
	SalesSummaryBean getSalesSummaryByProductAndDatePaid(long productId, Date datePaidStart, Date datePaidEnd);
}
