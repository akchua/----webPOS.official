package com.chua.evergrocery.database.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.beans.CashierSalesSummaryBean;
import com.chua.evergrocery.beans.SINRangeBean;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.prototype.CustomerOrderPrototype;
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.Status;

public interface CustomerOrderDAO extends DAO<CustomerOrder, Long>, CustomerOrderPrototype {

	CustomerOrder findByNameAndStatus(String name, Status[] status);
	
	List<CustomerOrder> findAllByCashierStatusAndDatePaidWithOrder(Long cashierId, Status[] status, Date dateFrom, Date dateTo, Order[] orders);
	
	List<CashierSalesSummaryBean> findAllCashierSalesSummaryByDatePaidAndDiscountType(Date dateFrom, Date dateTo, List<DiscountType> discountTypes, Boolean returnsOnly);
	
	SINRangeBean getSINRangeByDate(Date dateFrom, Date dateTo);
}