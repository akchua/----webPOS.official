package com.chua.evergrocery.database.dao;

import java.util.Date;
import java.util.List;

import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.prototype.CustomerOrderPrototype;
import com.chua.evergrocery.enums.Status;

public interface CustomerOrderDAO extends DAO<CustomerOrder, Long>, CustomerOrderPrototype {

	CustomerOrder findByNameAndStatus(String name, Status[] status);
	
	List<CustomerOrder> findAllByCashierStatusAndDatePaid(Long cashierId, Status[] status, Date dateFrom);
}