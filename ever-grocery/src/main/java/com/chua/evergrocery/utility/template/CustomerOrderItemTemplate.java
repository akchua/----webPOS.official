package com.chua.evergrocery.utility.template;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.enums.DocType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 11, 2018
 */
public class CustomerOrderItemTemplate extends AbstractTemplate {

	private CustomerOrderDetail customerOrderItem;
	
	private Integer content;
	
	private List<String> overflowList;
	
	private final Integer ITEM_NAME_MAX_LENGTH = 33;
	
	public CustomerOrderItemTemplate(CustomerOrderDetail customerOrderItem, Integer content) {
		this.customerOrderItem = customerOrderItem;
		this.content = content;
		this.overflowList = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/customerOrderItem.vm", "UTF-8", model);
	}
	
	public String getFormattedQuantity() {
		NumberFormat nf = new DecimalFormat("##.#");
		return String.format("%-4s", nf.format(customerOrderItem.getQuantity()));
	}
	
	public String getFormattedName() {
		String formattedName = customerOrderItem.getUnitType().getShorthand() + " ";
		
		NumberFormat nf = new DecimalFormat("##.##");
		
		formattedName += customerOrderItem.getProductDisplayName();
		if(customerOrderItem.getQuantity() > 1) {
			formattedName += " @" + nf.format(customerOrderItem.getUnitPrice());
		}
		if(!content.equals(0)) {
			formattedName += " |" + content;
		}
		
		int index = ITEM_NAME_MAX_LENGTH;
		while (index < formattedName.length()) {
			overflowList.add("    " + formattedName.substring(index, Math.min(index + ITEM_NAME_MAX_LENGTH, formattedName.length())));
		    index += ITEM_NAME_MAX_LENGTH;
		}
		formattedName = formattedName.substring(0, Math.min(ITEM_NAME_MAX_LENGTH, formattedName.length()));
		
		return String.format("%-" + ITEM_NAME_MAX_LENGTH + "s", formattedName);
	}
	
	public String getFormattedTotalPrice() {
		NumberFormat nf = new DecimalFormat("##,###.##");
		return String.format("%7s", nf.format(customerOrderItem.getTotalPrice()));
	}
	
	public Boolean isOverflow() {
		return !overflowList.isEmpty();
	}
	
	public List<String> getOverflowList() {
		return overflowList;
	}
}
