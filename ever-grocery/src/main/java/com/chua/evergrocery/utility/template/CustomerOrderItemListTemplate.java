package com.chua.evergrocery.utility.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.enums.UnitType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 11, 2018
 */
public class CustomerOrderItemListTemplate extends AbstractTemplate {

	private List<CustomerOrderDetail> wholeItems;
	
	private List<CustomerOrderDetail> counterItems;
	
	private List<CustomerOrderDetail> otherItems;
	
	private List<String> formattedWholeItems;
	
	private List<String> formattedCounterItems;
	
	private List<String> formattedOtherItems;
	
	private Boolean showPrice;
	
	public CustomerOrderItemListTemplate(List<CustomerOrderDetail> customerOrderItems, Boolean showPrice) {
		this.showPrice = showPrice;
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
			final CustomerOrderItemTemplate customerOrderItemTemplate = new CustomerOrderItemTemplate(wholeItem, showPrice);
			formattedWholeItems.add(customerOrderItemTemplate.merge(velocityEngine, docType));
		}
		
		for(CustomerOrderDetail counterItem : counterItems) {
			final CustomerOrderItemTemplate customerOrderItemTemplate = new CustomerOrderItemTemplate(counterItem, showPrice);
			formattedCounterItems.add(customerOrderItemTemplate.merge(velocityEngine, docType));
		}
		
		for(CustomerOrderDetail otherItem : otherItems) {
			final CustomerOrderItemTemplate customerOrderItemTemplate = new CustomerOrderItemTemplate(otherItem, showPrice);
			formattedOtherItems.add(customerOrderItemTemplate.merge(velocityEngine, docType));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/customerOrderItemList.vm", "UTF-8", model);
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
