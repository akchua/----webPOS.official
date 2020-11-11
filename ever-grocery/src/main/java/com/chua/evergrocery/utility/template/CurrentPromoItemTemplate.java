package com.chua.evergrocery.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.PromoBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.format.NumberFormatter;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Nov 11, 2020
 */
public class CurrentPromoItemTemplate extends AbstractTemplate {

	private PromoBean promo;
	
	public CurrentPromoItemTemplate(PromoBean promo) {
		this.promo = promo;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/currentPromoItem.vm", "UTF-8", model);
	}

	public String getFormattedProductName() {
		return String.format("%-62s", promo.getProductName());
	}
	
	public String getFormattedPricePerCase() {
		return String.format("%13s", NumberFormatter.decimalFormat(promo.getCaseSellingPrice() * ((100 - promo.getDiscountPercent()) / 100.0f), 2));
	}
	
	public String getFormattedPricePerPiece() {
		return String.format("%13s", NumberFormatter.decimalFormat(promo.getPieceSellingPrice() * ((100 - promo.getDiscountPercent()) / 100.0f), 2));
	}
}
