package com.chua.evergrocery.database.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.beans.ProductPurchaseSummaryBean;
import com.chua.evergrocery.beans.PurchaseSummaryBean;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.database.prototype.PurchaseOrderDetailPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface PurchaseOrderDetailDAO extends DAO<PurchaseOrderDetail, Long>, PurchaseOrderDetailPrototype {

	ObjectList<PurchaseOrderDetail> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, long purchaseOrderId, Order[] orders);
	
	List<PurchaseOrderDetail> findAllByProductAndDeliveryDate(long productId, Date deliveryStart, Date deliveryEnd);
	
	List<PurchaseOrderDetail> findAllByPurchaseOrderIdWithOrder(Long purchaseOrderId, Order[] orders);
	
	List<PurchaseOrderDetail> findAllByCompanyAndDeliveryDate(long companyId, Date deliveryStart, Date deliveryEnd);
	
	PurchaseSummaryBean getPurchaseSummaryByProductAndDeliveryDate(long productId, Date deliveryStart, Date deliveryEnd);
	
	List<ProductPurchaseSummaryBean> getAllProductPurchaseSummaryByCompanyAndDeliveryDate(long companyId, Date deliveryStart, Date deliveryEnd);
}
