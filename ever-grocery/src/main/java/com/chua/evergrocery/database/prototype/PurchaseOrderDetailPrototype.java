package com.chua.evergrocery.database.prototype;

import java.util.List;

import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.objects.ObjectList;

public interface PurchaseOrderDetailPrototype {

	ObjectList<PurchaseOrderDetail> findAllWithPaging(int pageNumber, int resultsPerPage, long purchaseOrderId);
	
	PurchaseOrderDetail findByOrderAndDetailId(long purchaseOrderId, long productDetailId);
	
	List<PurchaseOrderDetail> findAllByPurchaseOrderId(Long purchaseOrderId);
}
