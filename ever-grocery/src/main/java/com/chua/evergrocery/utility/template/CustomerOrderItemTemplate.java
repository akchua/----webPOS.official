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
	
	private Boolean showPrice;
	
	private List<String> overflowList;
	
	private final Integer ITEM_NAME_MAX_LENGTH = 30;
	
	private final Integer MAX_LINE_SPLIT_ADJUST = 7;
	
	public CustomerOrderItemTemplate(CustomerOrderDetail customerOrderItem, Boolean showPrice) {
		this.customerOrderItem = customerOrderItem;
		this.content = customerOrderItem.getProductDetail().getContent();
		this.showPrice = showPrice;
		
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
	
	public Boolean isShowPrice() {
		return showPrice;
	}
	
	public String getFormattedName() {
		String formattedName = customerOrderItem.getUnitType().getShorthand() + " ";
		
		NumberFormat nf = new DecimalFormat("##.00");
		NumberFormat nf2 = new DecimalFormat("#.#");
		
		if(customerOrderItem.getUpgradedQuantity() > 0.0f 
				&& customerOrderItem.getQuantity() >= 1.0f
				&& (customerOrderItem.getUnitType().equals(UnitType.CASE)
						|| customerOrderItem.getUnitType().equals(UnitType.BUNDLE)
						|| customerOrderItem.getUnitType().equals(UnitType.SACK)))
			formattedName += nf2.format(customerOrderItem.getUpgradedQuantity()) + "^";
		
		if(!customerOrderItem.getPromoId().equals(0l)) {
			formattedName += "$ ";
		}
		
		formattedName += customerOrderItem.getFormattedDisplayName();
		if((customerOrderItem.getQuantity() % 1.0f == 0.5f
				|| customerOrderItem.getUnitType().equals(UnitType.CASE)
				|| customerOrderItem.getUnitType().equals(UnitType.BUNDLE)
				|| customerOrderItem.getUnitType().equals(UnitType.SACK))
				&& content != null
				&& !content.equals(0)) {
			formattedName += "x" + content;
		}
		
		if(customerOrderItem.getQuantity() > 1) {
			formattedName += " @" + nf.format(customerOrderItem.getUnitPrice());
		}
		
		int index = ITEM_NAME_MAX_LENGTH;
		int splits = 1;
		int firstSplitIndex = ITEM_NAME_MAX_LENGTH;
		while (index < formattedName.length()) {
			int adjustedIndex = index;
			int i = 0;
			// try to find a white space up to 'MAX_LINE_SPLIT_ADJUST' characters back
			while(formattedName.charAt(adjustedIndex) != ' ' && i < MAX_LINE_SPLIT_ADJUST) {
				adjustedIndex--;
				i++;
			}
			// reset index if exceeded max line split and no white space found
			if(i == MAX_LINE_SPLIT_ADJUST && formattedName.charAt(adjustedIndex) != ' ') adjustedIndex = index;
			else adjustedIndex++;
			
			// record the first split
			if(splits == 1) firstSplitIndex = adjustedIndex;
			
			// +1 to not include the space on the next line
			overflowList.add("    " + formattedName.substring(adjustedIndex, Math.min(adjustedIndex + ITEM_NAME_MAX_LENGTH, formattedName.length())));
		    index = adjustedIndex + ITEM_NAME_MAX_LENGTH;
		    splits++;
		}
		formattedName = formattedName.substring(0, Math.min(firstSplitIndex, formattedName.length()));
		
		return String.format("%-" + ITEM_NAME_MAX_LENGTH + "s", formattedName.trim());
	}
	
	public String getFormattedTotalPrice() {
		return String.format("%10s", customerOrderItem.getFormattedBeforeTaxAdjustmentPrice());
	}
	
	public Boolean isOverflow() {
		return !overflowList.isEmpty();
	}
	
	public List<String> getOverflowList() {
		return overflowList;
	}
}
