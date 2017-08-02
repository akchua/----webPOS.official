package com.chua.evergrocery.database.dao;

import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.prototype.CustomerOrderPrototype;
import com.chua.evergrocery.enums.Status;

public interface CustomerOrderDAO extends DAO<CustomerOrder, Long>, CustomerOrderPrototype {

	CustomerOrder findByNameAndStatus(String name, Status[] status);
}