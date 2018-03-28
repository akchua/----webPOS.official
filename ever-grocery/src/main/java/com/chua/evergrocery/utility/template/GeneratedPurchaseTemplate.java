package com.chua.evergrocery.utility.template;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.ProductStatisticsBean;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 22, 2018
 */
public class GeneratedPurchaseTemplate implements Template {

	private String companyName;
	
	private Date salePeriodStart;
	
	private Date salePeriodEnd;
	
	private Date expectedDeliveryDate;
	
	private Float daysToBook;
	
	private List<ProductStatisticsBean> productStats;
	
	private List<String> formattedPurchaseItems;
	
	public GeneratedPurchaseTemplate(String companyName, Date salePeriodStart, Date salePeriodEnd, 
			Date expectedDeliveryDate, Float daysToBook,
			List<ProductStatisticsBean> productStats) {
		this.companyName = companyName;
		this.salePeriodStart = salePeriodStart;
		this.salePeriodEnd = salePeriodEnd;
		this.expectedDeliveryDate = expectedDeliveryDate;
		this.daysToBook = daysToBook;
		this.productStats = productStats;
		this.formattedPurchaseItems = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		for(ProductStatisticsBean prodStat : productStats) {
			final GeneratedPurchaseItemTemplate genPurchaseItemTemplate = new GeneratedPurchaseItemTemplate(prodStat);
			formattedPurchaseItems.add(genPurchaseItemTemplate.merge(velocityEngine));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/generatedPurchase.vm", "UTF-8", model);
	}
	
	public String getCompanyName() {
		return this.companyName;
	}
	
	public String getSalesPeriod() {
		return DateFormatter.prettyFormat(salePeriodStart) + " - " + DateFormatter.prettyFormat(salePeriodEnd);
	}
	
	public String getPurchasePeriod() {
		Calendar purchasePeriodEnd = Calendar.getInstance();
		purchasePeriodEnd.add(Calendar.DAY_OF_MONTH, Math.round(daysToBook));
		return DateFormatter.prettyFormat(new Date()) + " - " + DateFormatter.prettyFormat(purchasePeriodEnd.getTime());
	}
	
	public String getExpectedDeliveryDate() {
		return DateFormatter.prettyFormat(expectedDeliveryDate);
	}
	
	public String getDate() {
		return DateFormatter.longFormat(new Date());
	}
	
	public List<String> getFormattedPurchaseItems() {
		return this.formattedPurchaseItems;
	}
}
