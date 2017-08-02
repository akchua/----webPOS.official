package com.chua.evergrocery.database.service;

import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.prototype.CustomerOrderPrototype;
import com.chua.evergrocery.enums.Status;

public interface CustomerOrderService 
		extends Service<CustomerOrder, Long>, CustomerOrderPrototype {
	
	Boolean isExistsByNameAndStatus(String name, Status[] status);
}
