package com.chua.evergrocery.database.service;

import java.util.Date;
import java.util.List;

import com.chua.evergrocery.beans.SalesReportQueryBean;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.prototype.CustomerOrderPrototype;
import com.chua.evergrocery.enums.Status;

public interface CustomerOrderService 
		extends Service<CustomerOrder, Long>, CustomerOrderPrototype {
	
	Boolean isExistsByNameAndStatus(String name, Status[] status);
	
	List<CustomerOrder> findAllPaidByCashierAndDateFromToNow(Long cashierId, Date dateFrom);
	
	List<CustomerOrder> findAllBySalesReportQueryBean(SalesReportQueryBean salesReportQuery);
}
