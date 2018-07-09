package com.chua.evergrocery.utility.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.DailySalesBean;
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
	
	private List<DailySalesBean> dailySalesList;
	
	private Float totalVatSales;
	
	private Float totalVatExSales;
	
	private Float totalZeroRatedSales;
	
	private List<String> formattedDailySalesList;
	
	public SalesReportTemplate(SalesReportQueryBean salesReportQuery, List<DailySalesBean> dailySalesList) {
		this.salesReportQuery = salesReportQuery;
		this.dailySalesList = dailySalesList;
		
		this.totalVatSales = 0.0f;
		this.totalVatExSales = 0.0f;
		this.totalZeroRatedSales = 0.0f;
		
		for(DailySalesBean dailySales : dailySalesList) {
			this.totalVatSales += dailySales.getTotalVatSales();
			this.totalVatExSales += dailySales.getTotalVatExSales();
			this.totalZeroRatedSales += dailySales.getTotalZeroRatedSales();
		}
		
		this.formattedDailySalesList = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		for(DailySalesBean dailySales : dailySalesList) {
			final DailySalesTemplate dailySalesTemplate = new DailySalesTemplate(dailySales);
			formattedDailySalesList.add(dailySalesTemplate.merge(velocityEngine, docType));
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
	
	public String getFormattedGrandTotal() {
		return String.format("%17s", CurrencyFormatter.pesoFormat(this.totalVatSales + this.totalVatExSales + this.totalZeroRatedSales));
	}
	
	public List<String> getFormattedDailySalesList() {
		return formattedDailySalesList;
	}
}
