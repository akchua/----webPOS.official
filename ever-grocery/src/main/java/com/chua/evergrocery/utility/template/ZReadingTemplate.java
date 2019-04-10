package com.chua.evergrocery.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.database.entity.ZReading;
import com.chua.evergrocery.enums.DocType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   22 Mar 2019
 */
public class ZReadingTemplate extends AbstractTemplate {

	private ZReading zReading;
	
	private String formattedHeader;
	
	public ZReadingTemplate(ZReading zReading) {
		this.zReading = zReading;
		
		this.formattedHeader = "";
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		formattedHeader = new EverHeaderTemplate().merge(velocityEngine, docType);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/zreading.vm", "UTF-8", model);
	}
	
	public String getFormattedHeader() {
		return formattedHeader;
	}
	
	public String getFormattedZReadingCounter() {
		return zReading.getCounter() + "";
	}
	
	public String getFormattedReadingDate() {
		return zReading.getFormattedReadingDate();
	}
	
	public String getFormattedReceiptCount() {
		return zReading.getNumberOfReceipts() + "";
	}
	
	public String getFormattedBeginningSIN() {
		return zReading.getFormattedBeginningSIN();
	}
	
	public String getFormattedEndingSIN() {
		return zReading.getFormattedEndingSIN();
	}
	
	public String getFormattedBeginningBalance() {
		return String.format("%21s", "Php " + zReading.getFormattedBeginningBalance());
	}
	
	public String getFormattedEndingBalance() {
		return String.format("%21s", "Php " + zReading.getFormattedEndingBalance());
	}
	
	public String getFormattedNetSales() {
		return String.format("%21s", "Php " + zReading.getFormattedNetSales());
	}
	
	public String getFormattedGrossSales() {
		return String.format("%21s", "Php " + zReading.getFormattedGrossSales());
	}
	
	public String getFormattedRegularDiscountAmount() {
		return String.format("%21s", "Php " + zReading.getFormattedRegularDiscountAmount());
	}
	
	public String getFormattedSeniorDiscountAmount() {
		return String.format("%21s", "Php " + zReading.getFormattedSeniorDiscountAmount());
	}
	
	public String getFormattedPwdDiscountAmount() {
		return String.format("%21s", "Php " + zReading.getFormattedPwdDiscountAmount());
	}
	
	public String getFormattedVatableSales() {
		return String.format("%21s", "Php " + zReading.getFormattedNetVatableSales());
	}
	
	public String getFormattedVatAmount() {
		return String.format("%21s", "Php " + zReading.getFormattedNetVatAmount());
	}
	
	public String getFormattedVatExSales() {
		return String.format("%21s", "Php " + zReading.getFormattedNetVatExSales());
	}
	
	public String getFormattedZeroRatedSales() {
		return String.format("%21s", "Php " + zReading.getFormattedNetZeroRatedSales());
	}
	
	public String getFormattedTotalRefund() {
		return String.format("%21s", "Php " + zReading.getFormattedRefundAmount());
	}
	
	public String getFormattedBeginningRefundNumber() {
		return String.format("%21s", zReading.getFormattedBeginningRefundNumber());
	}
	
	public String getFormattedEndingRefundNumber() {
		return String.format("%21s", zReading.getFormattedEndingRefundNumber());
	}
	
	public String getFormattedBeginningRefundAmount() {
		return String.format("%21s", "Php " + zReading.getFormattedBeginningRefundAmount());
	}
	
	public String getFormattedEndingRefundAmount() {
		return String.format("%21s", "Php " + zReading.getFormattedEndingRefundAmount());
	}
	
	public String getFormattedTotalCashPayment() {
		return String.format("%21s", "Php " + zReading.getFormattedTotalCashPayment());
	}
	
	public String getFormattedTotalCheckPayment() {
		return String.format("%21s", "Php " + zReading.getFormattedTotalCheckPayment());
	}
	
	public String getFormattedTotalCardPayment() {
		return String.format("%21s", "Php " + zReading.getFormattedTotalCardPayment());
	}
	
	public String getFormattedGrandTotal() {
		return String.format("%18s", "Php " + zReading.getFormattedEndingBalance());
	}
}
