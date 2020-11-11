package com.chua.evergrocery.utility.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.PromoBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Nov 11, 2020
 */
public class CurrentPromoTemplate extends AbstractTemplate {

	private List<PromoBean> promos;
	
	private List<String> formattedPromos;
	
	public CurrentPromoTemplate(List<PromoBean> promos) {
		this.promos = promos;
		this.formattedPromos = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		for(PromoBean promo : promos) {
			final CurrentPromoItemTemplate currentPromoItemTemplate = new CurrentPromoItemTemplate(promo);
			formattedPromos.add(currentPromoItemTemplate.merge(velocityEngine, docType));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/currentPromo.vm", "UTF-8", model);
	}
	
	public String getFormattedDate() {
		return DateFormatter.prettyFormat(new Date());
	}
	
	public List<String> getFormattedPromos() {
		return formattedPromos;
	}
}
