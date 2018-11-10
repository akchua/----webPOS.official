package com.chua.evergrocery.database.service;

import java.util.Date;
import java.util.List;

import com.chua.evergrocery.beans.ProductPurchaseSummaryBean;
import com.chua.evergrocery.beans.PurchaseSummaryBean;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.database.prototype.PurchaseOrderDetailPrototype;

public interface PurchaseOrderDetailService
	extends Service<PurchaseOrderDetail, Long>, PurchaseOrderDetailPrototype{

	List<PurchaseOrderDetail> findAllByCompanyAndMonthId(long companyId, int monthId);
	
	List<PurchaseOrderDetail> findAllByProductAndDeliveryDate(long productId, Date deliveryStart, Date deliveryEnd);
	
	PurchaseSummaryBean getPurchaseSummaryByProductAndDeliveryDate(long productId, Date deliveryStart, Date deliveryEnd);
	
	List<ProductPurchaseSummaryBean> getAllProductPurchaseSummaryByCompanyAndMonthId(long companyId, int monthId);
}
