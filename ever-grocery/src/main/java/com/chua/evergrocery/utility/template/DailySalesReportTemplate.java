package com.chua.evergrocery.utility.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.CashierSalesSummaryBean;
import com.chua.evergrocery.beans.DailySalesReportBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   8 Jul 2018
 */
public class DailySalesReportTemplate extends AbstractTemplate {

	private DailySalesReportBean dailySalesReport;
	
	private List<String> formattedCashierSalesSummaryList;
	
	public DailySalesReportTemplate(DailySalesReportBean dailySalesReport) {
		this.dailySalesReport = dailySalesReport;
		this.formattedCashierSalesSummaryList = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		for(CashierSalesSummaryBean cashierSalesSummary : dailySalesReport.getCashierSalesSummaries()) {
			final CashierSalesSummaryTemplate cashierSalesSummaryTemplate = new CashierSalesSummaryTemplate(cashierSalesSummary);
			formattedCashierSalesSummaryList.add(cashierSalesSummaryTemplate.merge(velocityEngine, docType));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/dailySalesReport.vm", "UTF-8", model);
	}
	
	public String getFormattedSaleDate() {
		return StringHelper.center(DateFormatter.prettyFormat(dailySalesReport.getSaleDate()), 15);
	}
	
	public String getFormattedSinStart() {
		return StringHelper.center(dailySalesReport.getSinRange().getFormattedStartSIN(), 10);
	}
	
	public String getFormattedSinEnd() {
		return StringHelper.center(dailySalesReport.getSinRange().getFormattedEndSIN(), 10);
	}
	
	public String getFormattedTotalVatableSales() {
		return String.format("%14s", dailySalesReport.getFormattedTotalVatableSales()) + " ";
	}
	
	public String getFormattedTotalVatExSales() {
		return String.format("%14s", dailySalesReport.getFormattedTotalVatExSales()) + " ";
	}
	
	public String getFormattedTotalZeroRatedSales() {
		return String.format("%14s", dailySalesReport.getFormattedTotalZeroRatedSales()) + " ";
	}
	
	public String getFormattedTotalVatAmount() {
		return String.format("%14s", dailySalesReport.getFormattedTotalVatAmount()) + " ";
	}
	
	public String getFormattedTotalDiscountAmount() {
		return String.format("%12s", dailySalesReport.getFormattedTotalDiscountAmount()) + " ";
	}
	
	public String getFormattedTotalSales() {
		return String.format("%14s", dailySalesReport.getFormattedTotalSales()) + " ";
	}

	public List<String> getFormattedCashierSalesSummaryList() {
		return formattedCashierSalesSummaryList;
	}
}
