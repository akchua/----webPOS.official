package com.chua.evergrocery.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.database.entity.XReading;
import com.chua.evergrocery.enums.DocType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   24 Mar 2019
 */
public class XReadingTemplate extends AbstractTemplate {

	private XReading xReading;
	
	private String formattedHeader;
	
	public XReadingTemplate(XReading xReading) {
		this.xReading = xReading;
		
		this.formattedHeader = "";
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		formattedHeader = new EverHeaderTemplate().merge(velocityEngine, docType);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/xreading.vm", "UTF-8", model);
	}
	
	public String getFormattedHeader() {
		return formattedHeader;
	}
	
	public String getFormattedCashier() {
		return xReading.getCashier().getFormattedName() + "";
	}
	
	public String getFormattedReadingDate() {
		return xReading.getFormattedReadingDate();
	}
	
	
	public String getFormattedBeginningBalance() {
		return String.format("%21s", "Php " + xReading.getFormattedBeginningBalance());
	}
	
	public String getFormattedEndingBalance() {
		return String.format("%21s", "Php " + xReading.getFormattedEndingBalance());
	}
	
	public String getFormattedNetSales() {
		return String.format("%21s", "Php " + xReading.getFormattedNetSales());
	}
	
	public String getFormattedGrossSales() {
		return String.format("%21s", "Php " + xReading.getFormattedGrossSales());
	}
	
	public String getFormattedRegularDiscountAmount() {
		return String.format("%21s", "Php " + xReading.getFormattedRegularDiscountAmount());
	}
	
	public String getFormattedSeniorDiscountAmount() {
		return String.format("%21s", "Php " + xReading.getFormattedSeniorDiscountAmount());
	}
	
	public String getFormattedPwdDiscountAmount() {
		return String.format("%21s", "Php " + xReading.getFormattedPwdDiscountAmount());
	}
	
	public String getFormattedTotalRefund() {
		return String.format("%21s", "Php " + xReading.getFormattedRefundAmount());
	}
	
	public String getFormattedTotalCashPayment() {
		return String.format("%21s", "Php " + xReading.getFormattedTotalCashPayment());
	}
	
	public String getFormattedTotalCheckPayment() {
		return String.format("%21s", "Php " + xReading.getFormattedTotalCheckPayment());
	}
	
	public String getFormattedTotalCardPayment() {
		return String.format("%21s", "Php " + xReading.getFormattedTotalCardPayment());
	}
	
	public String getFormattedTotalPointsPayment() {
		return String.format("%21s", "Php " + xReading.getFormattedTotalPointsPayment());
	}
}
