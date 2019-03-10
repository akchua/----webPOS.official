package com.chua.evergrocery.utility.template;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.DocType;
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
	
	private String formattedCustomerOrderItems;
	
	public CustomerOrderReceiptTemplate(CustomerOrder customerOrder, List<CustomerOrderDetail> customerOrderItems) {
		this.customerOrder = customerOrder;
		this.customerOrderItems = customerOrderItems;
		
		this.formattedCustomerOrderItems = "";
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		formattedCustomerOrderItems = new CustomerOrderItemListTemplate(customerOrderItems).merge(velocityEngine, docType);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/customerOrderReceipt.vm", "UTF-8", model);
	}
	
	public String getDate() {
		return StringHelper.center(DateFormatter.longFormat(new Date()), 44);
	}
	
	public String getFormattedSIN() {
		return customerOrder.getFormattedSIN();
	}
	
	public String getOrderNumber() {
		return customerOrder.getOrderNumber();
	}
	
	public String getFormattedVatable() {
		return String.format("%12s", CurrencyFormatter.pesoFormat(customerOrder.getVatSales() / 1.12f));
	}
	
	public String getFormattedVatExSales() {
		return String.format("%12s", customerOrder.getFormattedVatExSales());
	}
	
	public String getFormattedZeroRatedSales() {
		return String.format("%12s", customerOrder.getFormattedZeroRatedSales());
	}
	
	public String getFormattedVat() {
		Float vat = customerOrder.getVatSales() - (customerOrder.getVatSales() / 1.12f);
		return String.format("%12s", CurrencyFormatter.pesoFormat(vat));
	}
	
	public Boolean isDiscounted() {
		return !customerOrder.getDiscountType().equals(DiscountType.NO_DISCOUNT);
	}
	
	public String getFormattedGrossAmount() {
		return String.format("%12s", customerOrder.getFormattedGrossAmount());
	}
	
	public String getFormattedDiscountLabel() {
		return String.format("%-3s", customerOrder.getDiscountType().getShortHand()) + String.format("%2s", customerOrder.getDiscountType().getPercentDiscount() + "%");
	}
	
	public String getFormattedTotalDiscountAmount() {
		return String.format("%12s", customerOrder.getFormattedTotalDiscountAmount());
	}
	
	public String getFormattedTotalAmount() {
		return String.format("%10s", customerOrder.getFormattedTotalAmount());
	}
	
	public String getFormattedCash() {
		return String.format("%12s", CurrencyFormatter.pesoFormat(customerOrder.getCash()));
	}
	
	public String getFormattedChange() {
		return String.format("%10s", CurrencyFormatter.pesoFormat(customerOrder.getCash() - customerOrder.getTotalAmount()));
	}
	
	public String getFormattedCashier() {
		return customerOrder.getCashier().getFormattedName();
	}

	public String getFormattedCustomerOrderItems() {
		return formattedCustomerOrderItems;
	}
}
