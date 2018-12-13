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
import com.chua.evergrocery.enums.UnitType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 11, 2018
 */
public class CustomerOrderItemTemplate extends AbstractTemplate {

	private CustomerOrderDetail customerOrderItem;
	
	private Integer content;
	
	private List<String> overflowList;
	
	private final Integer ITEM_NAME_MAX_LENGTH = 30;
	
	public CustomerOrderItemTemplate(CustomerOrderDetail customerOrderItem) {
		this.customerOrderItem = customerOrderItem;
		this.content = customerOrderItem.getProductDetail().getContent();
		
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
		
		formattedName += customerOrderItem.getFormattedDisplayName();
		if((customerOrderItem.getQuantity() % 1.0f == 0.5f
				|| customerOrderItem.getUnitType().equals(UnitType.CASE)
				|| customerOrderItem.getUnitType().equals(UnitType.BUNDLE)
				|| customerOrderItem.getUnitType().equals(UnitType.SACK))
				&& !content.equals(0)) {
			formattedName += "x" + content;
		}
		
		if(customerOrderItem.getQuantity() > 1) {
			formattedName += " @" + nf.format(customerOrderItem.getUnitPrice());
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
		return String.format("%10s", customerOrderItem.getFormattedTotalPrice());
	}
	
	public Boolean isOverflow() {
		return !overflowList.isEmpty();
	}
	
	public List<String> getOverflowList() {
		return overflowList;
	}
}
