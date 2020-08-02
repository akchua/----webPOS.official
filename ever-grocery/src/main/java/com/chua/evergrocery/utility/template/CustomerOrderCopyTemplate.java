package com.chua.evergrocery.utility.template;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Aug 30, 2018
 */
public class CustomerOrderCopyTemplate extends AbstractTemplate {
	
	private CustomerOrder customerOrder;
	
	private List<CustomerOrderDetail> customerOrderItems;
	
	private String formattedCustomerOrderItems;
	
	public CustomerOrderCopyTemplate(CustomerOrder customerOrder, List<CustomerOrderDetail> customerOrderItems) {
		this.customerOrder = customerOrder;
		this.customerOrderItems = customerOrderItems;
		
		this.formattedCustomerOrderItems = "";
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		formattedCustomerOrderItems = new CustomerOrderItemListTemplate(customerOrderItems, Boolean.TRUE).merge(velocityEngine, docType);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/customerOrderCopy.vm", "UTF-8", model);
	}

	public String getOrderNumber() {
		return customerOrder.getOrderNumber();
	}
	
	public String getFormattedGrossAmount() {
		return String.format("%12s", customerOrder.getFormattedGrossAmount());
	}
	
	public Boolean isDiscounted() {
		return customerOrder.getOutrightPromoDiscount() > 0;
	}
	
	public String getFormattedOutrightPromoDiscount() {
		return String.format("%12s", customerOrder.getFormattedOutrightPromoDiscount());
	}
	
	public String getFormattedTotalAmount() {
		return String.format("%12s", customerOrder.getFormattedTotalAmount());
	}
	
	public String getTotalItems() {
		NumberFormat nf = new DecimalFormat("###.#");
		return nf.format(customerOrder.getTotalItems());
	}
	
	public String getFormattedServer() {
		return customerOrder.getCreator().getShortName();
	}

	public String getFormattedCustomerOrderItems() {
		return formattedCustomerOrderItems;
	}
	
	public String getFormattedDate() {
		return DateFormatter.encryptedDayFormat(new Date());
	}
}
