package com.chua.evergrocery.database.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.beans.BIRSalesSummaryBean;
import com.chua.evergrocery.beans.DailySalesReportBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;
import com.chua.evergrocery.database.dao.CustomerOrderDAO;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.service.CustomerOrderService;
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.utility.DateUtil;

@Service
public class CustomerOrderServiceImpl
		extends AbstractService<CustomerOrder, Long, CustomerOrderDAO>
		implements CustomerOrderService 
{
	@Autowired
	protected CustomerOrderServiceImpl(CustomerOrderDAO dao) {
	super(dao);
	}
	
	@Override
	public ObjectList<CustomerOrder> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey, Status[] status, Integer daysAgo) {
	return dao.findAllWithPaging(pageNumber, resultsPerPage, searchKey, status, daysAgo);
	}

	@Override
	public List<CustomerOrder> findAllPaidByCashierAndDateFromToNow(Long cashierId, Date dateFrom) {
		return dao.findAllByCashierStatusAndDatePaidWithOrder(cashierId, new Status[] { Status.PAID }, dateFrom, new Date(), null);
	}
	
	@Override
	public List<CustomerOrder> findAllDiscountedByDatePaid(Date datePaid) {
		return dao.findAllByCashierStatusAndDatePaidWithOrder(null, new Status[] { Status.PAID }, DateUtil.floorDay(datePaid), DateUtil.ceilDay(datePaid), null);
	}
	
	@Override
	public List<CustomerOrder> findAllDiscountedByDatePaidAndCashier(Date datePaidStart, Date datePaidEnd,
			Long cashierId) {
		return dao.findAllByCashierStatusAndDatePaidWithOrder(cashierId, new Status[] { Status.PAID }, datePaidStart, datePaidEnd, null);
	}
	
	@Override
	public Integer getPaidCountToday() {
		return dao.findAllByCashierStatusAndDatePaidWithOrder(null, new Status[] { Status.PAID }, DateUtil.floorDay(new Date()), DateUtil.ceilDay(new Date()), null).size();
	}

	@Override
	public List<CustomerOrder> findAllBySalesReportQueryBean(SalesReportQueryBean salesReportQuery) {
		return dao.findAllByCashierStatusAndDatePaidWithOrder(null, new Status[] { Status.PAID }, salesReportQuery.getFrom(), salesReportQuery.getTo(), new Order[] { Order.asc("paidOn") });
	}

	@Override
	public CustomerOrder findBySerialInvoiceNumber(Long serialInvoiceNumber) {
		return dao.findBySerialInvoiceNumber(serialInvoiceNumber);
	}

	@Override
	public List<DailySalesReportBean> getDailySalesReportByDateRange(Date startDate, Date endDate) {
		return getDailySalesReportByDateRangeAndDiscountType(startDate, endDate, null, null);
	}

	@Override
	public List<DailySalesReportBean> getDailySalesReportByDateRangeAndDiscountType(Date startDate, Date endDate,
			List<DiscountType> discountType, Boolean returnsOnly) {
		final List<DailySalesReportBean> dailySalesReports = new ArrayList<DailySalesReportBean>();
		final Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(startDate);
		final Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		end.add(Calendar.DAY_OF_MONTH, 1);
		while(!DateUtil.isSameDay(currentDate.getTime(), end.getTime())) {
			final DailySalesReportBean dailySalesReport = new DailySalesReportBean();
			
			final Date dateFrom = DateUtil.floorDay(currentDate.getTime());
			final Date dateTo = DateUtil.ceilDay(currentDate.getTime());
			
			dailySalesReport.setSaleDate(dateFrom);
			dailySalesReport.setSinRange(dao.getSINRangeByDate(dateFrom, dateTo));
			dailySalesReport.setCashierSalesSummaries(dao.findAllCashierSalesSummaryByDatePaidAndDiscountType(dateFrom, dateTo, discountType, returnsOnly));
			
			dailySalesReports.add(dailySalesReport);
			currentDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return dailySalesReports;
	}

	@Override
	public BIRSalesSummaryBean getSalesSummaryByDatePaid(Date datePaid) {
		return dao.getSalesSummaryByDatePaidAndCashier(DateUtil.floorDay(datePaid), DateUtil.ceilDay(datePaid), null, Boolean.FALSE, Boolean.FALSE);
	}
	
	@Override
	public BIRSalesSummaryBean getSalesSummaryByDatePaidAndCashier(Date datePaidStart, Date datePaidEnd,
			Long cashierId) {
		return dao.getSalesSummaryByDatePaidAndCashier(datePaidStart, datePaidEnd, cashierId, Boolean.FALSE, Boolean.FALSE);
	}

	@Override
	public BIRSalesSummaryBean getSalesSummaryByDatePaidAndExceptCashier(Date datePaidStart, Date datePaidEnd,
			Long exceptCashierId) {
		return dao.getSalesSummaryByDatePaidAndCashier(datePaidStart, datePaidEnd, exceptCashierId, Boolean.TRUE, Boolean.FALSE);
	}

	@Override
	public BIRSalesSummaryBean getRefundSummaryByDatePaid(Date datePaid) {
		return dao.getSalesSummaryByDatePaidAndCashier(DateUtil.floorDay(datePaid), DateUtil.ceilDay(datePaid), null, Boolean.FALSE, Boolean.TRUE);
	}

	@Override
	public BIRSalesSummaryBean getRefundSummaryByDatePaidAndCashier(Date datePaidStart, Date datePaidEnd,
			Long cashierId) {
		return dao.getSalesSummaryByDatePaidAndCashier(datePaidStart, datePaidEnd, cashierId, Boolean.FALSE, Boolean.TRUE);
	}

	@Override
	public BIRSalesSummaryBean getRefundSummaryByDatePaidAndExceptCashier(Date datePaidStart, Date datePaidEnd,
			Long exceptCashierId) {
		return dao.getSalesSummaryByDatePaidAndCashier(datePaidStart, datePaidEnd, exceptCashierId, Boolean.TRUE, Boolean.TRUE);
	}
}
