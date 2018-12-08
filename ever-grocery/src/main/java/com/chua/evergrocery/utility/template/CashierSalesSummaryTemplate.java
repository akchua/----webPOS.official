package com.chua.evergrocery.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.CashierSalesSummaryBean;
import com.chua.evergrocery.enums.DocType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   8 Jul 2018
 */
public class CashierSalesSummaryTemplate extends AbstractTemplate {

	private CashierSalesSummaryBean cashierSalesSummary;
	
	public CashierSalesSummaryTemplate(CashierSalesSummaryBean cashierSalesSummary) {
		this.cashierSalesSummary = cashierSalesSummary;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/cashierSalesSummary.vm", "UTF-8", model);
	}
	
	public String getFormattedCashierName() {
		return String.format("%36s", cashierSalesSummary.getCashierFormattedName()) + " ";
	}
	
	public String getFormattedVatableSales() {
		return String.format("%14s", cashierSalesSummary.getFormattedVatableSales()) + " ";
	}
	
	public String getFormattedVatExSales() {
		return String.format("%14s", cashierSalesSummary.getFormattedVatExSales()) + " ";
	}
	
	public String getFormattedZeroRatedSales() {
		return String.format("%14s", cashierSalesSummary.getFormattedZeroRatedSales()) + " ";
	}
	
	public String getFormattedVatAmount() {
		return String.format("%14s", cashierSalesSummary.getFormattedVatAmount()) + " ";
	}
	
	public String getFormattedDiscountAmount() {
		return String.format("%12s", cashierSalesSummary.getFormattedDiscountAmount()) + " ";
	}
	
	public String getFormattedTotalSales() {
		return String.format("%14s", cashierSalesSummary.getFormattedTotalSales()) + " ";
	}
}
