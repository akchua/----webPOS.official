package com.chua.evergrocery.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.GeneratedProductPOBean;
import com.chua.evergrocery.beans.InventoryBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.NumberFormatter;

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
		return StringHelper.center(generatedProductPO.getProductName(), 62);
	}
	
	//19 right pad
	public String getFormattedOrder() {
		String formattedOrder = "";
		final InventoryBean toPurchase = generatedProductPO.getToPurchase();
		
		if(toPurchase.getWholeQuantity() != 0) {
			formattedOrder += toPurchase.getWholeQuantity() + toPurchase.getWholeUnit().getShorthand();
			if(toPurchase.getPieceQuantity() != 0.0f) {
				formattedOrder += " & " + NumberFormatter.decimalFormat(toPurchase.getPieceQuantity(), 2) + toPurchase.getPieceUnit().getShorthand();
			}
		} else if(toPurchase.getPieceQuantity() != 0.0f) {
			formattedOrder += NumberFormatter.decimalFormat(toPurchase.getPieceQuantity(), 2) + toPurchase.getPieceUnit().getShorthand();
		} else {
			formattedOrder += "=";
		}
		
		return String.format("%-19s", formattedOrder);
	}
	
	public String getFormattedInventory() {
		String formattedInventory = "";
		final InventoryBean inventory = generatedProductPO.getInventory();
		
		if(inventory.getWholeQuantity() != 0) {
			formattedInventory += inventory.getWholeQuantity() + inventory.getWholeUnit().getShorthand();
			if(inventory.getPieceQuantity() != 0.0f) {
				formattedInventory += " & " + NumberFormatter.decimalFormat(inventory.getPieceQuantity(), 2) + inventory.getPieceUnit().getShorthand();
			}
		} else if(inventory.getPieceQuantity() != 0.0f) {
			formattedInventory += NumberFormatter.decimalFormat(inventory.getPieceQuantity(), 2) + inventory.getPieceUnit().getShorthand();
		} else {
			formattedInventory += "=";
		}
		
		return String.format("%-19s", formattedInventory);
	}
	
	public String getFormattedSold() {
		String formattedSold = "";
		final InventoryBean inventory = generatedProductPO.getInventory();
		
		if(inventory.getSoldWholeQuantity() != 0) {
			formattedSold += inventory.getSoldWholeQuantity() + inventory.getWholeUnit().getShorthand();
			if(inventory.getSoldPieceQuantity() != 0.0f) {
				formattedSold += " & " + NumberFormatter.decimalFormat(inventory.getSoldPieceQuantity(), 2) + inventory.getPieceUnit().getShorthand();
			}
		} else if(inventory.getSoldPieceQuantity() != 0.0f) {
			formattedSold += NumberFormatter.decimalFormat(inventory.getSoldPieceQuantity(), 2) + inventory.getPieceUnit().getShorthand();
		} else {
			formattedSold += "=";
		}
		
		return String.format("%-19s", formattedSold);
	}
}
