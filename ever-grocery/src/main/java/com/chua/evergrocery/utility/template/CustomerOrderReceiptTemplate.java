package com.chua.evergrocery.utility.template;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 11, 2018
 */
public class CustomerOrderReceiptTemplate extends AbstractTemplate {

	private CustomerOrder customerOrder;
	
	private List<CustomerOrderDetail> customerOrderItems;
	
	private String title;
	
	private String subtitle;
	
	private String footer;
	
	private String formattedHeader;
	
	private String formattedCustomerOrderItems;
	
	public CustomerOrderReceiptTemplate(CustomerOrder customerOrder, List<CustomerOrderDetail> customerOrderItems,
					String title, String subtitle, String footer) {
		this.customerOrder = customerOrder;
		this.customerOrderItems = customerOrderItems;
		this.title = title;
		this.subtitle = subtitle;
		this.footer = footer;
		
		this.formattedHeader = "";
		this.formattedCustomerOrderItems = "";
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		formattedHeader = new EverHeaderTemplate().merge(velocityEngine, docType);
		formattedCustomerOrderItems = new CustomerOrderItemListTemplate(customerOrderItems, Boolean.TRUE).merge(velocityEngine, docType);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/customerOrderReceipt.vm", "UTF-8", model);
	}
	
	public String getFormattedHeader() {
		return this.formattedHeader;
	}
	
	public String getFormattedPaidOn() {
		return StringHelper.center(DateFormatter.longFormat(customerOrder.getPaidOn()), 44);
	}
	
	public String getFormattedSIN() {
		final String formattedSIN;
		if(customerOrder.getSerialInvoiceNumber() > 0) {
			formattedSIN = StringHelper.center("SI No. : " + customerOrder.getFormattedSIN(), 44);
		} else if(customerOrder.getReferenceSerialInvoiceNumber() > 0) {
			formattedSIN = StringHelper.center("Ref SI No. : " + customerOrder.getFormattedRefSIN(), 44);
		} else {
			formattedSIN = "";
		}
		
		return formattedSIN;
	}
	
	public String getFormattedRefundNumber() {
		final String formattedRefundNumber;
		if(customerOrder.getRefundNumber() > 0) {
			formattedRefundNumber = StringHelper.center("Refund No. :" + customerOrder.getFormattedRefundNumber(), 44);
		} else {
			formattedRefundNumber = "";
		}
		return formattedRefundNumber;
	}
	
	public String getFormattedTitle() {
		return StringHelper.center(title, 44);
	}
	
	public String getFormattedSubtitle() {
		String temp = (subtitle == null || subtitle.isEmpty()) ? footer : subtitle;
		return StringHelper.center(temp, 44);
	}
	
	public String getOrderNumber() {
		return customerOrder.getOrderNumber();
	}
	
	public String getCustomerFormattedName() {
		return customerOrder.getCustomer() != null ? customerOrder.getCustomer().getCustomerCategory().getCode() + " " + customerOrder.getCustomer().getCode() + " - " + customerOrder.getCustomer().getFormattedName() : "";
	}
	
	public String getFormattedPackageCount() {
		String formattedPackageCount = "";
		
		NumberFormat nf = new DecimalFormat("##.0");
		
		Float caseCount = 0.0f;
		for(CustomerOrderDetail orderItem : customerOrderItems) {
			if((orderItem.getUnitType().equals(UnitType.CASE) || orderItem.getUnitType().equals(UnitType.BUNDLE) || orderItem.getUnitType().equals(UnitType.SACK))
					&& orderItem.getQuantity() >= 1.0f) caseCount += orderItem.getQuantity() - orderItem.getUpgradedQuantity();
		}
		
		if(caseCount > 0.0f) formattedPackageCount += "[ ]" + nf.format(caseCount) + " CS   ";
		if(customerOrder.getCartonCount() > 0) formattedPackageCount += "[ ]" + customerOrder.getCartonCount() + "C   ";
		if(customerOrder.getPlasticCount() > 0) formattedPackageCount += "[ ]" + customerOrder.getPlasticCount() + "P   ";
		if(customerOrder.getBagCount() > 0) formattedPackageCount += "[ ]" + customerOrder.getBagCount() + " SB";
		
		return formattedPackageCount;
	}
	
	public String getFormattedVatable() {
		return String.format("%16s", "Php " + CurrencyFormatter.pesoFormat(customerOrder.getNetVatSales() / 1.12f));
	}
	
	public String getFormattedVatExSales() {
		return String.format("%16s", "Php " + customerOrder.getFormattedVatExSales());
	}
	
	public String getFormattedZeroRatedSales() {
		return String.format("%16s", "Php " + customerOrder.getFormattedZeroRatedSales());
	}
	
	public String getFormattedVat() {
		Float vat = customerOrder.getNetVatSales() - (customerOrder.getNetVatSales() / 1.12f);
		return String.format("%16s", "Php " + CurrencyFormatter.pesoFormat(vat));
	}
	
	public Boolean isDiscounted() {
		return !customerOrder.getDiscountType().equals(DiscountType.NO_DISCOUNT);
	}
	
	public Boolean isTaxAdjusted() {
		return !customerOrder.getTaxAdjustment().equals(0.0f);
	}
	
	public String getFormattedTaxAdjustableAmount() {
		return String.format("%16s", "Php " + CurrencyFormatter.pesoFormat(customerOrder.getGrossAmount() - customerOrder.getTaxAdjustment()));
	}
	
	public String getFormattedTaxAdjustment() {
		return String.format("%16s", "Php " + CurrencyFormatter.pesoFormat(customerOrder.getTaxAdjustment()));
	}
	
	public Boolean isNonZeroDiscount() {
		return !customerOrder.getDiscountType().getPercentDiscount().equals(0.0f);
	}
	
	public String getFormattedDiscountableAmount() {
		return String.format("%16s", "Php " + customerOrder.getFormattedTotalDiscountableAmount());
	}
	
	public String getFormattedDiscountLabel() {
		return String.format("%-3s", customerOrder.getDiscountType().getShortHand()) + String.format("%2s", customerOrder.getDiscountType().getPercentDiscount() + "%");
	}
	
	public String getFormattedTotalDiscountAmount() {
		return String.format("%16s", "Php " + customerOrder.getFormattedTotalDiscountAmount());
	}
	
	public String getFormattedNetOfDiscount() {
		return String.format("%16s", "Php " + CurrencyFormatter.pesoFormat(customerOrder.getTotalDiscountableAmount() - customerOrder.getTotalDiscountAmount()));
	}
	
	public String getFormattedGrossAmount() {
		return String.format("%12s", customerOrder.getFormattedGrossAmount());
	}
	
	public Boolean isDiscounted2() {
		return customerOrder.getOutrightPromoDiscount() > 0;
	}
	
	public String getFormattedOutrightPromoDiscount() {
		return String.format("%12s", customerOrder.getFormattedOutrightPromoDiscount());
	}
	
	public String getFormattedTotalAmount() {
		return String.format("%14s", "Php " + customerOrder.getFormattedTotalAmount());
	}
	
	public String getFormattedCash() {
		return String.format("%16s", "Php " + customerOrder.getFormattedCash());
	}
	
	public Boolean isPaidWithCheck() {
		return customerOrder.getCheckAmount() > 0;
	}
	
	public String getFormattedCheckAccountNumber() {
		return StringHelper.center(customerOrder.getCheckAccountNumber(), 44);
	}
	
	public String getFormattedCheckNumber() {
		return String.format("%24s", "Check #" + customerOrder.getCheckNumber());
	}
	
	public String getFormattedCheckAmount() {
		return String.format("%16s", "Php " + customerOrder.getFormattedCheckAmount());
	}
	
	public Boolean isPaidWithCard() {
		return customerOrder.getCardAmount() > 0;
	}
	
	public String getFormattedTransactionNumber() {
		return String.format("%24s", "TXN #" + customerOrder.getCardTransactionNumber());
	}
	
	public String getFormattedCardAmount() {
		return String.format("%16s", "Php " + customerOrder.getFormattedCardAmount());
	}
	
	public Boolean isPaidWithPoints() {
		return customerOrder.getPointsAmount() > 0;
	}
	
	public String getFormattedCustomerName() {
		return StringHelper.center(customerOrder.getCustomer() != null ? customerOrder.getCustomer().getName() : "", 44);
	}
	
	public String getFormattedCustomerCardId() {
		return String.format("%24s", customerOrder.getCustomer() != null ? customerOrder.getCustomer().getFormattedCardId() : "");
	}
	
	public String getFormattedPointsAmount() {
		return String.format("%16s", customerOrder.getFormattedPointsAmount());
	}
	
	public Boolean isPaidWithNonCash() {
		return customerOrder.getCheckAmount() + customerOrder.getCardAmount() + customerOrder.getPointsAmount() > 0;
	}
	
	public String getFormattedTotalPayment() {
		return String.format("%16s", "Php " + customerOrder.getFormattedTotalPayment());
	}
	
	public String getFormattedChange() {
		return String.format("%14s", "Php " + CurrencyFormatter.pesoFormat(customerOrder.getTotalPayment() - customerOrder.getTotalAmount()));
	}
	
	public Boolean isPointsActivated() {
		return customerOrder.getCustomer() != null && customerOrder.getCustomer().getCardId() != null && !customerOrder.getCustomer().getCardId().isEmpty();
	}
	
	public String getFormattedTotalPoints() {
		return customerOrder.getCustomer() != null ? customerOrder.getCustomer().getFormattedAvailablePoints() : "0.00";
	}
	
	public String getFormattedPointsUsed() {
		return customerOrder.getFormattedPointsAmount();
	}
	
	public String getFormattedPointsEarned() {
		return customerOrder.getFormattedPointsEarned();
	}
	
	public String getFormattedPreviousPoints() {
		final Float previousPoints = (customerOrder.getCustomer() != null) ? customerOrder.getCustomer().getAvailablePoints() + customerOrder.getPointsAmount() - customerOrder.getPointsEarned() : 0.0f;
		return CurrencyFormatter.pesoFormat(previousPoints);
	}
	
	public String getFormattedDiscountName() {
		return String.format("%-14s", "Customer Name") + ": " + customerOrder.getDiscountName();
	}
	
	public String getFormattedDiscountAddress() {
		return String.format("%-14s", "Address") + ": " + customerOrder.getDiscountAddress();
	}
	
	public String getFormattedDiscountTin() {
		return String.format("%-14s", "TIN") + ": " + customerOrder.getDiscountTin();
	}
	
	public String getFormattedIdNumber() {
		return String.format("%-14s", "SC/PWD ID No.") + ": " + customerOrder.getDiscountIdNumber();
	}
	
	public String getFormattedServer() {
		return customerOrder.getCreator().getShortName();
	}
	
	public String getFormattedCashier() {
		return customerOrder.getCashier().getShortName();
	}

	public String getFormattedCustomerOrderItems() {
		return formattedCustomerOrderItems;
	}
	
	public List<String> getDisclaimerList() {
		final List<String> disclaimerList = new ArrayList<String>();
		if(customerOrder.getTotalAmount() < 0) {
			disclaimerList.add("This document shall be valid for FIVE(5)");
		} else {
			disclaimerList.add("This invoice shall be valid for FIVE(5)");
		}
		disclaimerList.add("years from the date of the permit to use");
		if(customerOrder.getTotalAmount() < 0) {
			disclaimerList.add("THIS DOCUMENT IS NOT VALID FOR CLAIM");
			disclaimerList.add("OF INPUT TAX");
		}
		return disclaimerList;
	}
	
	public String getFormattedFooter() {
		return StringHelper.center(footer, 44);
	}
}
