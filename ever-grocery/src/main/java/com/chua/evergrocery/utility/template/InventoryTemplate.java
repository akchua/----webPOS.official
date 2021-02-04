package com.chua.evergrocery.utility.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.InventoryBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.format.DateFormatter;

public class InventoryTemplate extends AbstractTemplate {
	
	private String subjectName;
	
	private List<InventoryBean> inventories;
	
	private List<String> formattedInventoryItems;

	public InventoryTemplate(String subjectName, List<InventoryBean> inventories) {
		this.subjectName = subjectName;
		this.inventories = inventories;
		this.formattedInventoryItems = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		for(InventoryBean inventory : inventories) {
			final InventoryItemTemplate inventoryItemTemplate = new InventoryItemTemplate(inventory);
			formattedInventoryItems.add(inventoryItemTemplate.merge(velocityEngine));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/inventory.vm", "UTF-8", model);
	}
	
	public String getSubjectName() {
		return this.subjectName;
	}
	
	public String getDate() {
		return DateFormatter.longFormat(new Date());
	}
	
	public List<String> getFormattedInventoryItems() {
		return this.formattedInventoryItems;
	}
}
