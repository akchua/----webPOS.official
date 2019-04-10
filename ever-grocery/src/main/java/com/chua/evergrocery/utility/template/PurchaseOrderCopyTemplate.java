package com.chua.evergrocery.utility.template;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.enums.DocType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Apr 10, 2019
 */
public class PurchaseOrderCopyTemplate extends AbstractTemplate {

	private PurchaseOrder purchaseOrder;
	
	private List<PurchaseOrderDetail> purchaseOrderItems;
	
	private String formattedPurchaseOrderItems;
	
	public PurchaseOrderCopyTemplate(PurchaseOrder purchaseOrder, List<PurchaseOrderDetail> purchaseOrderItems) {
		this.purchaseOrder = purchaseOrder;
		this.purchaseOrderItems = purchaseOrderItems;
		
		this.formattedPurchaseOrderItems = "";
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		formattedPurchaseOrderItems = new PurchaseOrderItemListTemplate(purchaseOrderItems).merge(velocityEngine, docType);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/purchaseOrderCopy.vm", "UTF-8", model);
	}

	public String getId() {
		return purchaseOrder.getId() + "";
	}
	
	public String getFormattedTotalAmount() {
		return String.format("%12s", purchaseOrder.getFormattedTotalAmount());
	}
	
	public String getTotalItems() {
		NumberFormat nf = new DecimalFormat("###.#");
		return nf.format(purchaseOrder.getTotalItems());
	}
	
	public String getFormattedServer() {
		return purchaseOrder.getCreator().getFormattedName();
	}

	public String getFormattedPurchaseOrderItems() {
		return formattedPurchaseOrderItems;
	}
}
