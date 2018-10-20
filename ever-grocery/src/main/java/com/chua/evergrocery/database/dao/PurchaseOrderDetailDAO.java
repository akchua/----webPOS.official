package com.chua.evergrocery.database.dao;

import java.util.Date;
import java.util.List;

import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.database.prototype.PurchaseOrderDetailPrototype;

public interface PurchaseOrderDetailDAO extends DAO<PurchaseOrderDetail, Long>, PurchaseOrderDetailPrototype {

	List<PurchaseOrderDetail> findAllByCompanyAndDeliveryDate(long companyId, Date deliveryStart, Date deliveryEnd);
}
