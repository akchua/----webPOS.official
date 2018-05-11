package com.chua.evergrocery.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.InventoryBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.NumberFormatter;

public class InventoryItemTemplate extends AbstractTemplate {

	private InventoryBean inventoryItem;
	
	public InventoryItemTemplate(InventoryBean inventoryItem) {
		this.inventoryItem = inventoryItem;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/inventoryItem.vm", "UTF-8", model);
	}

	public String getProductName() {
		return StringHelper.center(inventoryItem.getProductName(), 62);
	}
	
	public String getProductDisplayName() {
		return inventoryItem.getProductDisplayName();
	}
	
	public String getWholeInventory() {
		return " " + String.format("%-14s", inventoryItem.getWholeQuantity().toString() + " " + inventoryItem.getWholeUnit().getShorthand()) + " ";
	}
	
	public String getPieceInventory() {
		return " " + String.format("%-14s", NumberFormatter.decimalFormat(this.inventoryItem.getPieceQuantity(), 1) + " " + inventoryItem.getPieceUnit().getShorthand()) + " ";
	}
}
