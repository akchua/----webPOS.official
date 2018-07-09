package com.chua.evergrocery.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.DailySalesBreakdownBean;
import com.chua.evergrocery.enums.DocType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   8 Jul 2018
 */
public class DailySalesBreakdownTemplate extends AbstractTemplate {

	private DailySalesBreakdownBean dailySalesBreakdown;
	
	public DailySalesBreakdownTemplate(DailySalesBreakdownBean dailySalesBreakDown) {
		this.dailySalesBreakdown = dailySalesBreakDown;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/dailySalesBreakdown.vm", "UTF-8", model);
	}
	
	public String getFormattedCashierName() {
		return String.format("%36s", dailySalesBreakdown.getCashierName()) + " ";
	}
	
	public String getFormattedVatableSales() {
		return String.format("%14s", dailySalesBreakdown.getFormattedVatableSales()) + " ";
	}
	
	public String getFormattedVatExSales() {
		return String.format("%14s", dailySalesBreakdown.getFormattedVatExSales()) + " ";
	}
	
	public String getFormattedZeroRatedSales() {
		return String.format("%14s", dailySalesBreakdown.getFormattedZeroRatedSales()) + " ";
	}
	
	public String getFormattedVatAmount() {
		return String.format("%14s", dailySalesBreakdown.getFormattedVatAmount()) + " ";
	}
	
	public String getFormattedTotalSales() {
		return String.format("%14s", dailySalesBreakdown.getFormattedTotalSales()) + " ";
	}
}
