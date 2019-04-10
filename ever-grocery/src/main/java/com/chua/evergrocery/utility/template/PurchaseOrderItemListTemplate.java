package com.chua.evergrocery.utility.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.enums.DocType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Apr 10, 2019
 */
public class PurchaseOrderItemListTemplate extends AbstractTemplate {

	private List<PurchaseOrderDetail> purchaseOrderItems;
	
	private List<String> formattedPurchaseOrderItems;
	
	public PurchaseOrderItemListTemplate(List<PurchaseOrderDetail> purchaseOrderItems) {
		this.purchaseOrderItems = purchaseOrderItems;
		this.formattedPurchaseOrderItems = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		for(PurchaseOrderDetail purchaseOrderItem : purchaseOrderItems) {
			final PurchaseOrderItemTemplate purchaseOrderItemTemplate = new PurchaseOrderItemTemplate(purchaseOrderItem);
			formattedPurchaseOrderItems.add(purchaseOrderItemTemplate.merge(velocityEngine, docType));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/purchaseOrderItemList.vm", "UTF-8", model);
	}
	
	public List<String> getFormattedPurchaseOrderItems() {
		return formattedPurchaseOrderItems;
	}
}
