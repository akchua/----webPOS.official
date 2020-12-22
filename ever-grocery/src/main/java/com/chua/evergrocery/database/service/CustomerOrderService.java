package com.chua.evergrocery.database.service;

import java.util.Date;
import java.util.List;

import com.chua.evergrocery.beans.BIRSalesSummaryBean;
import com.chua.evergrocery.beans.DailySalesReportBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.prototype.CustomerOrderPrototype;
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerOrderService 
		extends Service<CustomerOrder, Long>, CustomerOrderPrototype {
	
	ObjectList<CustomerOrder> findAllWithPagingByCustomerOrderByLatest(int pageNumber, int resultsPerPage, Long customerId);
	
	ObjectList<CustomerOrder> findAllWithPagingOrderByLatest(int pageNumber, int resultsPerPage, String searchKey, Status[] status, Integer daysAgo);
	
	List<CustomerOrder> findAllPaidByCashierAndDateFromToNow(Long cashierId, Date dateFrom);
	
	List<CustomerOrder> findAllDiscountedByDatePaid(Date datePaid);
	
	List<CustomerOrder> findAllDiscountedByDatePaidAndCashier(Date datePaidStart, Date datePaidEnd, Long cashierId);
	
	Integer getPaidCountToday();
	
	List<CustomerOrder> findAllBySalesReportQueryBean(SalesReportQueryBean salesReportQuery);
	
	List<DailySalesReportBean> getDailySalesReportByDateRange(Date startDate, Date endDate);
	
	List<DailySalesReportBean> getDailySalesReportByDateRangeAndDiscountType(Date startDate, Date endDate, List<DiscountType> discountType, Boolean returnsOnly);
	
	BIRSalesSummaryBean getSalesSummaryByDatePaid(Date datePaid);
	
	BIRSalesSummaryBean getSalesSummaryByDatePaidAndCashier(Date datePaidStart, Date datePaidEnd, Long cashierId);
	
	BIRSalesSummaryBean getSalesSummaryByDatePaidAndExceptCashier(Date datePaidStart, Date datePaidEnd, Long exceptCashierId);
	
	BIRSalesSummaryBean getRefundSummaryByDatePaid(Date datePaid);
	
	BIRSalesSummaryBean getRefundSummaryByDatePaidAndCashier(Date datePaidStart, Date datePaidEnd, Long cashierId);
	
	BIRSalesSummaryBean getRefundSummaryByDatePaidAndExceptCashier(Date datePaidStart, Date datePaidEnd, Long exceptCashierId);
}
