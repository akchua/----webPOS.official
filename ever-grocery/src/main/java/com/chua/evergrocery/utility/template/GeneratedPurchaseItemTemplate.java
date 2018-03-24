package com.chua.evergrocery.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.ProductStatisticsBean;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.NumberFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 22, 2018
 */
public class GeneratedPurchaseItemTemplate implements Template {

	private ProductStatisticsBean productStatisticsBean;

	public GeneratedPurchaseItemTemplate(ProductStatisticsBean productStatisticsBean) {
		this.productStatisticsBean = productStatisticsBean;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/generatedPurchaseItem.vm", "UTF-8", model);
	}
	
	public String getProductName() {
		return StringHelper.center(productStatisticsBean.getProductName(), 62);
	}
	
	public String getQuantity() {
		return StringHelper.center(productStatisticsBean.getQuantity() + "", 7);
	}
	
	public String getUnit() {
		return StringHelper.center(productStatisticsBean.getUnit().toString(), 8);
	}
	
	public String getSales() {
		return " " + String.format("%10s", productStatisticsBean.getFormattedSales()) + " ";
	}
	
	public String getProfit() {
		return " " + String.format("%10s", productStatisticsBean.getFormattedProfit()) + " ";
	}
	
	public String getPreviousSaleRate() {
		return String.format("%8s", productStatisticsBean.getFormattedPreviousSaleRate());
	}
	
	public String getCurrentSaleRate() {
		return String.format("%8s", productStatisticsBean.getFormattedCurrentSaleRate());
	}
	
	public String getPreviousTotalBudget() {
		return " " + String.format("%10s", productStatisticsBean.getFormattedPreviousTotalBudget()) + " ";
	}
	
	public String getCurrentTotalBudget() {
		return " " + String.format("%10s", productStatisticsBean.getFormattedCurrentTotalBudget()) + " ";
	}
	
	public String getNetBudgetChange() {
		final String netBudgetChange;
		
		if(productStatisticsBean.getPreviousTotalBudget() != 0.0f) {
			netBudgetChange = NumberFormatter.toPercent((productStatisticsBean.getCurrentTotalBudget() - productStatisticsBean.getPreviousTotalBudget()) / productStatisticsBean.getPreviousTotalBudget() * 100);
		} else {
			netBudgetChange = "0.0%";
		}
		
		return String.format("%8s", netBudgetChange);
	}
}
