package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.database.prototype.PurchaseOrderDetailPrototype;

public interface PurchaseOrderDetailService
	extends Service<PurchaseOrderDetail, Long>, PurchaseOrderDetailPrototype{

	List<PurchaseOrderDetail> findAllByCompanyAndMonthId(long companyId, int monthId);
}
