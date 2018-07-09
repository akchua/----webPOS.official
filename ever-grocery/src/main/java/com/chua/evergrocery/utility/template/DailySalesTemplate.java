package com.chua.evergrocery.utility.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.DailySalesBean;
import com.chua.evergrocery.beans.DailySalesBreakdownBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   8 Jul 2018
 */
public class DailySalesTemplate extends AbstractTemplate {

	private DailySalesBean dailySales;
	
	private List<String> formattedDailySalesBreakdowns;
	
	public DailySalesTemplate(DailySalesBean dailySales) {
		this.dailySales = dailySales;
		this.formattedDailySalesBreakdowns = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		for(DailySalesBreakdownBean dailySalesBreakdown : dailySales.getDailySalesBreakdowns()) {
			final DailySalesBreakdownTemplate dailySalesBreakdownTemplate = new DailySalesBreakdownTemplate(dailySalesBreakdown);
			formattedDailySalesBreakdowns.add(dailySalesBreakdownTemplate.merge(velocityEngine, docType));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/dailySales.vm", "UTF-8", model);
	}
	
	public String getFormattedSaleDate() {
		return StringHelper.center(DateFormatter.prettyFormat(dailySales.getSaleDate()), 15);
	}
	
	public String getFormattedSinStart() {
		return StringHelper.center(dailySales.getFormattedSinStart(), 10);
	}
	
	public String getFormattedSinEnd() {
		return StringHelper.center(dailySales.getFormattedSinEnd(), 10);
	}
	
	public String getFormattedTotalVatableSales() {
		return String.format("%14s", dailySales.getFormattedTotalVatableSales()) + " ";
	}
	
	public String getFormattedTotalVatExSales() {
		return String.format("%14s", dailySales.getFormattedTotalVatExSales()) + " ";
	}
	
	public String getFormattedTotalZeroRatedSales() {
		return String.format("%14s", dailySales.getFormattedTotalZeroRatedSales()) + " ";
	}
	
	public String getFormattedTotalVatAmount() {
		return String.format("%14s", dailySales.getFormattedTotalVatAmount()) + " ";
	}
	
	public String getFormattedTotalSales() {
		return String.format("%14s", dailySales.getFormattedTotalSales()) + " ";
	}
	
	public List<String> getFormattedDailySalesBreakdowns() {
		return formattedDailySalesBreakdowns;
	}
}
