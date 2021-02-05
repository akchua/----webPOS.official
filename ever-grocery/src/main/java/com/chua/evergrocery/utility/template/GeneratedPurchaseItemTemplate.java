package com.chua.evergrocery.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.GeneratedProductPOBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.StringHelper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 22, 2018
 */
public class GeneratedPurchaseItemTemplate extends AbstractTemplate {

	private GeneratedProductPOBean generatedProductPO;

	public GeneratedPurchaseItemTemplate(GeneratedProductPOBean generatedProductPO) {
		this.generatedProductPO = generatedProductPO;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/generatedPurchaseItem.vm", "UTF-8", model);
	}
	
	//62
	public String getFormattedProductName() {
		return StringHelper.center(generatedProductPO.getProductName() + (generatedProductPO.getInventory().getWholeContent() != 0 ? " (x" + generatedProductPO.getInventory().getWholeContent() + ")" : ""), 62);
	}
	
	//19 right pad
	public String getFormattedOrder() {
		return String.format("%-19s", generatedProductPO.getToPurchase().getFormattedInventory());
	}
	
	public String getFormattedInventory() {
		return String.format("%-19s", generatedProductPO.getInventory().getFormattedInventory());
	}
	
	public String getFormattedSold() {
		return String.format("%-19s", generatedProductPO.getInventory().getFormattedSoldCount());
	}
}
