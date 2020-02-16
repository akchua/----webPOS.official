package com.chua.evergrocery.rest.handler.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.beans.BIRSalesSummaryBean;
import com.chua.evergrocery.beans.DailySalesReportBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;
import com.chua.evergrocery.constants.BusinessConstants;
import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.entity.XReading;
import com.chua.evergrocery.database.entity.ZReading;
import com.chua.evergrocery.database.service.CustomerOrderService;
import com.chua.evergrocery.database.service.XReadingService;
import com.chua.evergrocery.database.service.ZReadingService;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.rest.handler.ActivityLogHandler;
import com.chua.evergrocery.rest.handler.SalesReportHandler;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.Html;
import com.chua.evergrocery.utility.SimplePdfWriter;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.chua.evergrocery.utility.template.SalesReportTemplate;
import com.chua.evergrocery.utility.template.excel.BIRBackendReport;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   9 Jul 2018
 */
@Component
@Transactional
public class SalesReportHandlerImpl implements SalesReportHandler {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CustomerOrderService customerOrderService;
	
	@Autowired
	private ZReadingService zReadingService;
	
	@Autowired
	private XReadingService xReadingService;
	
	@Autowired
	private ActivityLogHandler activityLogHandler;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Autowired
	private BusinessConstants businessConstants;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Override
	public void updateZReading() {
		ZReading latestZReading = zReadingService.getLatestZReading();
		
		final Calendar today = Calendar.getInstance();
		today.setTime(DateUtil.floorDay(new Date()));
		today.add(Calendar.DAY_OF_MONTH, 1);
		
		final Calendar currentReadingDate = Calendar.getInstance();
		currentReadingDate.setTime(latestZReading.getReadingDate());
		currentReadingDate.add(Calendar.DAY_OF_MONTH, 1);
		
		while(currentReadingDate.before(today) ||
				(DateUtil.isSameDay(today.getTime(), currentReadingDate.getTime()) && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > businessConstants.getBusinessCutoffHour())) {
			LOG.info("Updating z reading of : " + DateFormatter.prettyFormat(currentReadingDate.getTime()));
			latestZReading = zReadingService.getLatestZReading();
			
			final ZReading zReading = new ZReading();
			zReading.setReadingDate(currentReadingDate.getTime());
			zReading.setCounter(latestZReading.getCounter() + 1);
			
			// salesSummary does not include refunds
			final BIRSalesSummaryBean salesSummary = customerOrderService.getSalesSummaryByDatePaid(currentReadingDate.getTime());
			ensureBIRSalesSummaryBeanNotNull(salesSummary);
			zReading.setBeginningSIN(
					(salesSummary.getBeginningSIN() == null || salesSummary.getBeginningSIN().equals(0l))
							? latestZReading.getEndingSIN() 
							: salesSummary.getBeginningSIN());
			zReading.setEndingSIN(
					(salesSummary.getEndingSIN() == null || salesSummary.getEndingSIN().equals(0l))
							? zReading.getBeginningSIN()
							: salesSummary.getEndingSIN());
			
			final BIRSalesSummaryBean refundSummary = customerOrderService.getRefundSummaryByDatePaid(currentReadingDate.getTime());
			ensureBIRSalesSummaryBeanNotNull(refundSummary);
			zReading.setBeginningRefundNumber(
					(refundSummary.getBeginningRefundNumber() == null || refundSummary.getBeginningRefundNumber().equals(0l))
							? latestZReading.getEndingRefundNumber() 
							: refundSummary.getBeginningRefundNumber());
			zReading.setEndingRefundNumber(
					(refundSummary.getEndingRefundNumber() == null || refundSummary.getEndingRefundNumber().equals(0l))
							? zReading.getBeginningRefundNumber() 
							: refundSummary.getEndingRefundNumber());
			
			zReading.setBeginningBalance(latestZReading.getEndingBalance());
			zReading.setVatSales((salesSummary.getVatSales() + refundSummary.getVatSales()) * businessConstants.getBusinessIncome());
			zReading.setVatExSales(salesSummary.getVatExSales() + refundSummary.getVatExSales());
			zReading.setZeroRatedSales(salesSummary.getZeroRatedSales() + refundSummary.getZeroRatedSales());
			zReading.setZeroRatedRemovedVat((salesSummary.getZeroRatedSales() + refundSummary.getZeroRatedSales()) * 0.12f); 
			
			zReading.setVatDiscount((salesSummary.getVatDiscount() + refundSummary.getVatDiscount()));
			zReading.setVatExDiscount(salesSummary.getVatExDiscount() + refundSummary.getVatExDiscount());
			zReading.setZeroRatedDiscount(salesSummary.getZeroRatedDiscount() + refundSummary.getZeroRatedDiscount());
			
			final List<CustomerOrder> discountedCustomerOrders = customerOrderService.findAllDiscountedByDatePaid(currentReadingDate.getTime());
			Float regularDiscountAmount = 0.0f;
			Float seniorDiscountAmount = 0.0f;
			Float pwdDiscountAmount = 0.0f;
			
			for(CustomerOrder dco : discountedCustomerOrders) {
				switch(dco.getDiscountType()) {
				case NO_DISCOUNT:
					break;
				case SENIOR_DISCOUNT:
				case SENIOR_MEDICINE_DISCOUNT:
					seniorDiscountAmount += dco.getTotalDiscountAmount();
					break;
				case PWD_DISCOUNT:
				case PWD_MEDICINE_DISCOUNT:
					pwdDiscountAmount += dco.getTotalDiscountAmount();
					break;
				default:
					regularDiscountAmount += dco.getTotalDiscountAmount();
				}
			}
			
			zReading.setRegularDiscountAmount(regularDiscountAmount);
			zReading.setSeniorDiscountAmount(seniorDiscountAmount);
			zReading.setPwdDiscountAmount(pwdDiscountAmount);
			zReading.setBeginningRefundAmount(latestZReading.getEndingRefundAmount());
			zReading.setRefundAmount(refundSummary.getNetSales());
			zReading.setTotalCheckPayment((salesSummary.getCheckAmount() != null ? salesSummary.getCheckAmount() : 0.0f) + 
							(refundSummary.getCheckAmount() != null ? refundSummary.getCheckAmount() : 0.0f));
			zReading.setTotalCardPayment((salesSummary.getCardAmount() != null ? salesSummary.getCardAmount() : 0.0f) + 
					(refundSummary.getCardAmount() != null ? refundSummary.getCardAmount() : 0.0f));
			zReading.setTotalPointsPayment((salesSummary.getPointsAmount() != null ? salesSummary.getPointsAmount() : 0.0f) + 
					(refundSummary.getPointsAmount() != null ? refundSummary.getPointsAmount() : 0.0f));
			
			zReadingService.insert(zReading);
			LOG.info("Completed z reading update of : " + DateFormatter.prettyFormat(currentReadingDate.getTime()));
			currentReadingDate.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
	
	@Override
	public XReading getXReadingByCashier(User cashier) {
		final XReading xReading = new XReading();
		
		if(cashier != null) {
			xReading.setCashier(cashier);
			xReading.setReadingDate(new Date());
			
			final XReading latestXReading = xReadingService.getLatestXReading();
			
			final Calendar openingTime = Calendar.getInstance();
			openingTime.setTime(DateUtil.floorDay(new Date()));
			openingTime.set(Calendar.HOUR_OF_DAY, businessConstants.getBusinessOpeningHour());
			
			final Date latestReadingDate = DateUtil.isSameDay(latestXReading.getReadingDate(), xReading.getReadingDate()) ? latestXReading.getReadingDate() : openingTime.getTime();
			
			final BIRSalesSummaryBean salesSummary = customerOrderService.getSalesSummaryByDatePaidAndCashier(latestReadingDate, xReading.getReadingDate(), cashier.getId());
			final BIRSalesSummaryBean refundSummary = customerOrderService.getRefundSummaryByDatePaidAndCashier(latestReadingDate, xReading.getReadingDate(), cashier.getId());
			Float netSales = salesSummary.getNetSales() + refundSummary.getNetSales();
			final BIRSalesSummaryBean othersSalesSummary = customerOrderService.getSalesSummaryByDatePaidAndExceptCashier(latestReadingDate, xReading.getReadingDate(), cashier.getId());
			final BIRSalesSummaryBean othersRefundSummary = customerOrderService.getRefundSummaryByDatePaidAndExceptCashier(latestReadingDate, xReading.getReadingDate(), cashier.getId());
			Float othersNetSales = othersSalesSummary.getNetSales() + othersRefundSummary.getNetSales();
			
			// ZED
			final Boolean zed = (xReading.getReadingDate().getTime() - latestReadingDate.getTime()) / 1000.0f / 60.0f < businessConstants.getZedMinutes();
			if(!zed) netSales *= businessConstants.getBusinessIncome();
			if(!zed) othersNetSales *= businessConstants.getBusinessIncome();
			
			xReading.setNetSales(netSales);
			xReading.setZeroRatedRemovedVat((salesSummary.getZeroRatedSales() + refundSummary.getZeroRatedSales()) * 0.12f);
			
			if(DateUtil.isSameDay(latestXReading.getReadingDate(), xReading.getReadingDate())) {
				xReading.setBeginningBalance(latestXReading.getEndingBalance() + othersNetSales);
			} else {
				updateZReading();
				final ZReading latestZReading = zReadingService.getLatestZReading();
				xReading.setBeginningBalance(latestZReading.getEndingBalance() + othersNetSales);
			}
			
			final List<CustomerOrder> discountedCustomerOrders = customerOrderService.findAllDiscountedByDatePaidAndCashier(latestReadingDate, xReading.getReadingDate(), cashier.getId());
			Float regularDiscountAmount = 0.0f;
			Float seniorDiscountAmount = 0.0f;
			Float pwdDiscountAmount = 0.0f;
			
			for(CustomerOrder dco : discountedCustomerOrders) {
				switch(dco.getDiscountType()) {
				case NO_DISCOUNT:
					break;
				case SENIOR_DISCOUNT:
				case SENIOR_MEDICINE_DISCOUNT:
					seniorDiscountAmount += dco.getTotalDiscountAmount();
					break;
				case PWD_DISCOUNT:
				case PWD_MEDICINE_DISCOUNT:
					pwdDiscountAmount += dco.getTotalDiscountAmount();
					break;
				default:
					regularDiscountAmount += dco.getTotalDiscountAmount();
				}
			}
			
			xReading.setRegularDiscountAmount(regularDiscountAmount);
			xReading.setSeniorDiscountAmount(seniorDiscountAmount);
			xReading.setPwdDiscountAmount(pwdDiscountAmount);
			xReading.setRefundAmount(refundSummary.getNetSales());
			xReading.setTotalCheckPayment((salesSummary.getCheckAmount() != null ? salesSummary.getCheckAmount() : 0.0f) + 
					(refundSummary.getCheckAmount() != null ? refundSummary.getCheckAmount() : 0.0f));
			xReading.setTotalCardPayment((salesSummary.getCardAmount() != null ? salesSummary.getCardAmount() : 0.0f) + 
					(refundSummary.getCardAmount() != null ? refundSummary.getCardAmount() : 0.0f));
			xReading.setTotalPointsPayment((salesSummary.getPointsAmount() != null ? salesSummary.getPointsAmount() : 0.0f) + 
					(refundSummary.getPointsAmount() != null ? refundSummary.getPointsAmount() : 0.0f));
			
			xReadingService.insert(xReading);
		}
		
		return xReading;
	}
	
	@Override
	public ResultBean generateReport(SalesReportQueryBean salesReportQuery) {
		final ResultBean result;
		
		if(salesReportQuery.getReportType() != null) {
			final List<DailySalesReportBean> dailySalesReports;
			
			switch(salesReportQuery.getReportType()) {
				case FULL:
					dailySalesReports = customerOrderService.getDailySalesReportByDateRange(salesReportQuery.getFrom(), salesReportQuery.getTo());
					break;
				case REGULAR:
					dailySalesReports = customerOrderService.getDailySalesReportByDateRangeAndDiscountType(salesReportQuery.getFrom(), salesReportQuery.getTo(), null, false);
					break;
				case SCD:
					final List<DiscountType> discountTypes = new ArrayList<DiscountType>();
					discountTypes.add(DiscountType.SENIOR_DISCOUNT);
					dailySalesReports = customerOrderService.getDailySalesReportByDateRangeAndDiscountType(salesReportQuery.getFrom(), salesReportQuery.getTo(), discountTypes, null);
					break;
				case RETURNS:
					dailySalesReports = customerOrderService.getDailySalesReportByDateRangeAndDiscountType(salesReportQuery.getFrom(), salesReportQuery.getTo(), null, true);
					break;
				default:
					dailySalesReports = new ArrayList<DailySalesReportBean>();
			}
			
					
			String fileName = "";
			fileName += salesReportQuery.getReportType().getName() + "_";
			fileName += DateFormatter.fileSafeFormat(salesReportQuery.getFrom()) + "_to_";
			fileName += DateFormatter.fileSafeFormat(salesReportQuery.getTo());
			fileName += ".pdf";
			fileName = StringHelper.convertToFileSafeFormat(fileName);
			final String filePath = fileConstants.getSalesHome() + fileName;
			result = new ResultBean();
			result.setSuccess(
					SimplePdfWriter.write(
							new SalesReportTemplate(
									salesReportQuery,
									dailySalesReports).merge(velocityEngine),
							businessConstants.getBusinessShortName(),
							filePath,
							true)
					);
			if(result.getSuccess()) {
				final Map<String, Object> extras = new HashMap<String, Object>();
				extras.put("fileName", fileName);
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created sales report."));
				result.setExtras(extras);
				
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Please select the type of report."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean generateBackendReport(Date dateFrom, Date dateTo, String ip) {
		final ResultBean result = new ResultBean(Boolean.TRUE, "");
		
		final List<ZReading> zReadingList = zReadingService.findAllZReadingByDateRangeOrderByReadingDate(dateFrom, dateTo);
		final BIRBackendReport birBackendReport = new BIRBackendReport(zReadingList);
		
		final String fileName = "Backend Report " + DateFormatter.fileSafeFormat(dateFrom) + "_-_" + DateFormatter.fileSafeFormat(dateTo) + ".xlsx";
		final String filePath = fileConstants.getBackendReportHome() + fileName;
		birBackendReport.write(filePath);
		
		final Map<String, Object> extras = new HashMap<String, Object>();
		extras.put("fileName", fileName);
		result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created backend report."));
		result.setExtras(extras);
		activityLogHandler.myLog("generated backend report : " + dateFrom + " - " + dateTo, ip);
		
		return result;
	}
	
	private void ensureBIRSalesSummaryBeanNotNull(BIRSalesSummaryBean birSalesSummary) {
		if(birSalesSummary.getVatSales() == null) birSalesSummary.setVatSales(0.0f);
		if(birSalesSummary.getVatExSales() == null) birSalesSummary.setVatExSales(0.0f);
		if(birSalesSummary.getZeroRatedSales() == null) birSalesSummary.setZeroRatedSales(0.0f);
		if(birSalesSummary.getVatDiscount() == null) birSalesSummary.setVatDiscount(0.0f);
		if(birSalesSummary.getVatExDiscount() == null) birSalesSummary.setVatExDiscount(0.0f);
		if(birSalesSummary.getZeroRatedDiscount() == null) birSalesSummary.setZeroRatedDiscount(0.0f);
	}
}
