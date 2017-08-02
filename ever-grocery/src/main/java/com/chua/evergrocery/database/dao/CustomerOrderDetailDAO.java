package com.chua.evergrocery.database.dao;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.database.prototype.CustomerOrderDetailPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerOrderDetailDAO extends DAO<CustomerOrderDetail, Long>, CustomerOrderDetailPrototype {

	ObjectList<CustomerOrderDetail> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, long customerOrderId, Order[] orders);
}
