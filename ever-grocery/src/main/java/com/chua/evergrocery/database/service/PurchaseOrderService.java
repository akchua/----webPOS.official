package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.database.prototype.PurchaseOrderPrototype;

public interface PurchaseOrderService
	extends Service<PurchaseOrder, Long>, PurchaseOrderPrototype {

	List<PurchaseOrder> findDeliveredWithinNinetyDaysByCompanyOrderByDeliveryDate(Long companyId);
}
