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
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.rest.handler.ProductHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 11, 2018
 */
public class CustomerOrderListTemplate extends AbstractTemplate {

	private CustomerOrder customerOrder;
	
	private List<CustomerOrderDetail> wholeItems;
	
	private List<CustomerOrderDetail> counterItems;
	
	private List<CustomerOrderDetail> otherItems;
	
	private ProductHandler productHandler;
	
	private List<String> formattedWholeItems;
	
	private List<String> formattedCounterItems;
	
	private List<String> formattedOtherItems;
	
	public CustomerOrderListTemplate(CustomerOrder customerOrder, List<CustomerOrderDetail> customerOrderItems, ProductHandler productHandler) {
		this.customerOrder = customerOrder;
		this.productHandler = productHandler;
		
		this.wholeItems = new ArrayList<CustomerOrderDetail>();
		this.counterItems = new ArrayList<CustomerOrderDetail>();
		this.otherItems = new ArrayList<CustomerOrderDetail>();
		
		for(CustomerOrderDetail orderItem : customerOrderItems) {
			final String categoryName = orderItem.getProductDetail().getProduct().getCategory().getName();
			if((orderItem.getUnitType().equals(UnitType.CASE) || orderItem.getUnitType().equals(UnitType.BUNDLE) || orderItem.getUnitType().equals(UnitType.SACK))
					&& orderItem.getQuantity() >= 1.0f) {
				wholeItems.add(orderItem);
			} else if(categoryName.equals("Cigarette") || categoryName.equals("Counter Item")) {
				counterItems.add(orderItem);
			} else {
				otherItems.add(orderItem);
			}
		}
		
		this.formattedWholeItems = new ArrayList<String>();
		this.formattedCounterItems = new ArrayList<String>();
		this.formattedOtherItems = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		for(CustomerOrderDetail wholeItem : wholeItems) {
			final CustomerOrderItemTemplate customerOrderItemTemplate = new CustomerOrderItemTemplate(wholeItem);
			formattedWholeItems.add(customerOrderItemTemplate.merge(velocityEngine, docType));
		}
		
		for(CustomerOrderDetail counterItem : counterItems) {
			final CustomerOrderItemTemplate customerOrderItemTemplate = new CustomerOrderItemTemplate(counterItem);
			formattedCounterItems.add(customerOrderItemTemplate.merge(velocityEngine, docType));
		}
		
		for(CustomerOrderDetail otherItem : otherItems) {
			final CustomerOrderItemTemplate customerOrderItemTemplate = new CustomerOrderItemTemplate(otherItem);
			formattedOtherItems.add(customerOrderItemTemplate.merge(velocityEngine, docType));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/customerOrderList.vm", "UTF-8", model);
	}
	
	public String getOrderNumber() {
		return customerOrder.getOrderNumber();
	}
	
	public String getFormattedCustomer() {
		return customerOrder.getCustomer() != null ? customerOrder.getCustomer().getFormattedName() : customerOrder.getName();
	}
	
	public String getTotalItems() {
		NumberFormat nf = new DecimalFormat("###.#");
		return nf.format(customerOrder.getTotalItems());
	}
	
	public String getFormattedServer() {
		return customerOrder.getCreator().getFormattedName();
	}
	
	public Boolean isWholeItemExists() {
		return !wholeItems.isEmpty();
	}
	
	public Boolean isCounterItemExists() {
		return !counterItems.isEmpty();
	}
	
	public List<String> getFormattedWholeItems() {
		return formattedWholeItems;
	}
	
	public List<String> getFormattedCounterItems() {
		return formattedCounterItems;
	}
	
	public List<String> getFormattedOtherItems() {
		return formattedOtherItems;
	}
}
