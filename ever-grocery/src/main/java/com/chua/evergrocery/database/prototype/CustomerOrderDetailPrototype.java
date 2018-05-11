package com.chua.evergrocery.database.prototype;

import java.util.Date;
import java.util.List;

import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerOrderDetailPrototype {

	ObjectList<CustomerOrderDetail> findAllWithPaging(int pageNumber, int resultsPerPage, long customerOrderId);
	
	CustomerOrderDetail findByOrderAndDetailId(long customerOrderId, long productDetailId);
	
	List<CustomerOrderDetail> findAllByProductLimitByDate(Long productId, Date from);
	
	List<CustomerOrderDetail> findAllByCustomerOrderId(Long customerOrderId);
}
