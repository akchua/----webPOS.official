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
	
	ResultBean generatePurchaseOrder(Long companyId, Float daysToBook);
	
	ResultBean generateOfftake(Long companyId, Float offtakeDays, Boolean download, Boolean print);
	
	ResultBean removePurchaseOrder(Long purchaseOrderId);
	
	void refreshPurchaseOrder(Long purchaseOrderId);
	
	ObjectList<PurchaseOrderDetail> getPurchaseOrderDetailList(Integer pageNumber, Long purchaseOrderId);
	
	ObjectList<PurchaseOrderDetail> getPurchaseOrderDetailListByProduct(Integer pageNumber, Long productId);
	
	ResultBean addItemByBarcode(String barcode, Long purchaseOrderId);
	
	ResultBean addItemByProductDetailId(Long productDetailId, Long purchaseOrderId, Integer quantity);
	
	ResultBean removePurchaseOrderDetail(Long purchaseOrderDetailId);
	
	ResultBean changePurchaseOrderDetailQuantity(Long purchaseOrderDetailId, Integer quantity);
	
	ResultBean checkPurchaseOrder(Long purchaseOrderId);
	
	ResultBean printPurchaseOrderCopy(Long purchaseOrderId);
}
