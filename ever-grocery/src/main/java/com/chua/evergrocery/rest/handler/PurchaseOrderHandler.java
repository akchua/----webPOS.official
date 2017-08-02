package com.chua.evergrocery.rest.handler;

import com.chua.evergrocery.beans.PurchaseOrderFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.objects.ObjectList;

public interface PurchaseOrderHandler {

	ObjectList<PurchaseOrder> getPurchaseOrderList(Integer pageNumber, Long companyId, Boolean showChecked);
	
	PurchaseOrder getPurchaseOrder(Long purchaseOrderId);
	
	ResultBean createPurchaseOrder(PurchaseOrderFormBean purchaseOrderForm);
	
	ResultBean removePurchaseOrder(Long purchaseOrderId);
	
	void refreshPurchaseOrder(Long purchaseOrderId);
	
	ObjectList<PurchaseOrderDetail> getPurchaseOrderDetailList(Integer pageNumber, Long purchaseOrderId);
	
	ResultBean addItemByProductDetailId(Long productDetailId, Long purchaseOrderId, Integer quantity);
	
	ResultBean removePurchaseOrderDetail(Long purchaseOrderDetailId);
	
	ResultBean changePurchaseOrderDetailQuantity(Long purchaseOrderDetailId, Integer quantity);
	
	ResultBean checkPurchaseOrder(Long purchaseOrderId);
}
