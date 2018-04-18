package com.chua.evergrocery.database.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.database.prototype.PurchaseOrderPrototype;

public interface PurchaseOrderDAO extends DAO<PurchaseOrder, Long>, PurchaseOrderPrototype {

	List<PurchaseOrder> findAllByCompanyAndDaysWithOrder(Long companyId, Date cutoff, Order[] orders);
}
