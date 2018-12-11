package com.chua.evergrocery.utility.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.DailySalesReportBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   8 Jul 2018
 */
public class SalesReportTemplate extends AbstractTemplate {

	private SalesReportQueryBean salesReportQuery;
	
	private List<DailySalesReportBean> dailySalesReports;
	
	private Float totalVatSales;
	
	private Float totalVatExSales;
	
	private Float totalZeroRatedSales;
	
	private Float totalDiscountAmount;
	
	private List<String> formattedDailySalesReportList;
	
	public SalesReportTemplate(SalesReportQueryBean salesReportQuery, List<DailySalesReportBean> dailySalesReports) {
		this.salesReportQuery = salesReportQuery;
		this.dailySalesReports = dailySalesReports;
		
		this.totalVatSales = 0.0f;
		this.totalVatExSales = 0.0f;
		this.totalZeroRatedSales = 0.0f;
		this.totalDiscountAmount = 0.0f;
		
		for(DailySalesReportBean dailySalesReport : dailySalesReports) {
			this.totalVatSales += dailySalesReport.getTotalVatSales();
			this.totalVatExSales += dailySalesReport.getTotalVatExSales();
			this.totalZeroRatedSales += dailySalesReport.getTotalZeroRatedSales();
			this.totalDiscountAmount += dailySalesReport.getTotalDiscountAmount();
		}
		
		this.formattedDailySalesReportList = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		for(DailySalesReportBean dailySalesReport : dailySalesReports) {
			final DailySalesReportTemplate dailySalesTemplate = new DailySalesReportTemplate(dailySalesReport);
			formattedDailySalesReportList.add(dailySalesTemplate.merge(velocityEngine, docType));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/salesReport.vm", "UTF-8", model);
	}
	
	public String getFormattedDateFrom() {
		return DateFormatter.prettyFormat(salesReportQuery.getFrom());
	}
	
	public String getFormattedDateTo() {
		return DateFormatter.prettyFormat(salesReportQuery.getTo());
	}
	
	public String getFormattedDate() {
		return DateFormatter.longFormat(new Date());
	}
	
	public String getFormattedTransactionsIncluded() {
		String temp = "";
		if(salesReportQuery.getDiscountType() == null) {
			temp = "ALL TRANSACTIONS";
		} else {
			temp = salesReportQuery.getDiscountType().getDisplayName() + " TRANSACTIONS";
		}
		
		return temp;
	}
	public String getFormattedTotalVatableSales() {
		return String.format("%17s", CurrencyFormatter.pesoFormat(this.totalVatSales / 1.12));
	}
	
	public String getFormattedTotalVatExSales() {
		return String.format("%17s", CurrencyFormatter.pesoFormat(this.totalVatExSales));
	}
	
	public String getFormattedTotalZeroRatedSales() {
		return String.format("%17s", CurrencyFormatter.pesoFormat(this.totalZeroRatedSales));
	}
	
	public String getFormattedTotalVatAmount() {
		return String.format("%17s", CurrencyFormatter.pesoFormat(this.totalVatSales - (this.totalVatSales / 1.12)));
	}
	
	public String getFormattedTotalDiscountAmount() {
		return String.format("%17s", CurrencyFormatter.pesoFormat(this.totalDiscountAmount));
	}
	
	public String getFormattedGrandTotal() {
		return String.format("%17s", CurrencyFormatter.pesoFormat(this.totalVatSales + this.totalVatExSales + this.totalZeroRatedSales - this.totalDiscountAmount));
	}

	public List<String> getFormattedDailySalesReportList() {
		return formattedDailySalesReportList;
	}
}
