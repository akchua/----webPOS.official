package com.chua.evergrocery.rest.handler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.beans.DailySalesBean;
import com.chua.evergrocery.beans.DailySalesBreakdownBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;
import com.chua.evergrocery.constants.BusinessConstants;
import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.service.CustomerOrderService;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.rest.handler.SalesReportHandler;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.Html;
import com.chua.evergrocery.utility.SimplePdfWriter;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.chua.evergrocery.utility.template.SalesReportTemplate;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   9 Jul 2018
 */
@Component
@Transactional
public class SalesReportHandlerImpl implements SalesReportHandler {

	@Autowired
	private CustomerOrderService customerOrderService;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Autowired
	private BusinessConstants businessConstants;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Override
	public ResultBean generateReport(SalesReportQueryBean salesReportQuery) {
		final ResultBean result;
		final List<CustomerOrder> customerOrders = customerOrderService.findAllBySalesReportQueryBean(salesReportQuery);
		
		if(customerOrders != null) {
			if(!customerOrders.isEmpty()) {
				final List<DailySalesBean> dailySalesList = new ArrayList<DailySalesBean>();
				
				Date refDate = DateUtil.getDefaultDate();
				DailySalesBean dailySales = null;
				
				Map<String, Float> dailyVatSalesBreakdown = new HashMap<String, Float>();
				Map<String, Float> dailyVatExSalesBreakdown = new HashMap<String, Float>();
				Map<String, Float> dailyZeroRatedSalesBreakdown = new HashMap<String, Float>();
				
				for(int i = 0; i < customerOrders.size(); i++) {
					if(!DateUtil.isSameDay(refDate, customerOrders.get(i).getPaidOn())) {
						if(dailySales != null) {
							dailySales.setSinEnd(customerOrders.get(i - 1).getSerialInvoiceNumber());
							List<DailySalesBreakdownBean> dailySalesBreakdowns = new ArrayList<DailySalesBreakdownBean>();
							
							Float totalVatSales = 0.0f;
							Float totalVatExSales = 0.0f;
							Float totalZeroRatedSales = 0.0f;
							Set<String> cashierNames = dailyVatSalesBreakdown.keySet();
							
							for(String cashierName : cashierNames) {
								// Recording sales breakdown per cashier
								DailySalesBreakdownBean dailySalesBreakdown = new DailySalesBreakdownBean();
								dailySalesBreakdown.setCashierName(cashierName);
								dailySalesBreakdown.setVatSales(dailyVatSalesBreakdown.get(cashierName));
								dailySalesBreakdown.setVatExSales(dailyVatExSalesBreakdown.get(cashierName));
								dailySalesBreakdown.setZeroRatedSales(dailyZeroRatedSalesBreakdown.get(cashierName));
								dailySalesBreakdowns.add(dailySalesBreakdown);
								
								// Getting total sales
								totalVatExSales += dailyVatExSalesBreakdown.get(cashierName);
								totalVatSales += dailyVatSalesBreakdown.get(cashierName);
								totalZeroRatedSales += dailyZeroRatedSalesBreakdown.get(cashierName);
							}
							
							dailySales.setTotalVatSales(totalVatSales);
							dailySales.setTotalVatExSales(totalVatExSales);
							dailySales.setTotalZeroRatedSales(totalZeroRatedSales);
							dailySales.setDailySalesBreakdowns(dailySalesBreakdowns);
							
							dailySalesList.add(dailySales);
							
							// Reset sales breakdown maps
							dailyVatSalesBreakdown = new HashMap<String, Float>();
							dailyVatExSalesBreakdown = new HashMap<String, Float>();
							dailyZeroRatedSalesBreakdown = new HashMap<String, Float>();
						}
						dailySales = new DailySalesBean();
						dailySales.setSaleDate(customerOrders.get(i).getPaidOn());
						dailySales.setSinStart(customerOrders.get(i).getSerialInvoiceNumber());
					}
					addOrderToBreakdown(dailyVatSalesBreakdown, dailyVatExSalesBreakdown, dailyZeroRatedSalesBreakdown, customerOrders.get(i));
				}
				
				String fileName = "";
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
										dailySalesList).merge(velocityEngine),
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
				result = new ResultBean(Boolean.FALSE, Html.line("No sales available for the given query."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load customer orders. Please refresh the page."));
		}
		
		return result;
	}
	
	private void addOrderToBreakdown(Map<String, Float> vatSalesBreakdown, Map<String, Float> vatExSalesBreakdown, 
			Map<String, Float> zeroRatedSalesBreakdown, CustomerOrder customerOrder) {
		final String cashierName = customerOrder.getCashier().getFormattedName();
		addAmountToMap(vatSalesBreakdown, cashierName, customerOrder.getVatSales());
		addAmountToMap(vatExSalesBreakdown, cashierName, customerOrder.getVatExSales());
		addAmountToMap(zeroRatedSalesBreakdown, cashierName, customerOrder.getZeroRatedSales());
	}
	
	private void addAmountToMap(Map<String, Float> map, String cashierName, Float amount) {
		Float totalAmount = map.get(cashierName);
		if(totalAmount == null) totalAmount = 0.0f;
		totalAmount += amount;
		map.put(cashierName, totalAmount);
	}
}
