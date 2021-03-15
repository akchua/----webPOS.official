package com.chua.evergrocery.utility.template;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.enums.UnitType;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Mar 15, 2021
 */
public class PendingItemTemplate extends AbstractTemplate {

	private CustomerOrderDetail pendingItem;
	
	public PendingItemTemplate(CustomerOrderDetail customerOrderItem) {
		this.pendingItem = customerOrderItem;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/pendingItem.vm", "UTF-8", model);
	}
	
	public String getFormattedQuantity() {
		NumberFormat nf = new DecimalFormat("##.#");
		return String.format("%-6s", nf.format(pendingItem.getQuantity()));
	}
	
	public String getFormattedName() {
		String formattedName = pendingItem.getUnitType().getShorthand() + " ";
		
		NumberFormat nf2 = new DecimalFormat("#.#");
		
		if(pendingItem.getUpgradedQuantity() > 0.0f 
				&& pendingItem.getQuantity() >= 1.0f
				&& (pendingItem.getUnitType().equals(UnitType.CASE)
						|| pendingItem.getUnitType().equals(UnitType.BUNDLE)
						|| pendingItem.getUnitType().equals(UnitType.SACK)))
			formattedName += nf2.format(pendingItem.getUpgradedQuantity()) + "^";
		
		if(!pendingItem.getPromoId().equals(0l)) {
			formattedName += "$ ";
		}
		
		formattedName += pendingItem.getFormattedItemName();
		
		return String.format("%-86s", formattedName.trim());
	}
	
	public String getFormattedOrderId() {
		return "#" + pendingItem.getCustomerOrder().getOrderNumber();
	}
}
