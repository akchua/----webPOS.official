package com.chua.evergrocery.utility.template;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.GeneratedOfftakeBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.StringHelper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 25, 2019
 */
public class GeneratedOfftakeItemTemplate extends AbstractTemplate {

	private GeneratedOfftakeBean generatedOfftake;
	
	public GeneratedOfftakeItemTemplate(GeneratedOfftakeBean generatedOfftake) {
		this.generatedOfftake = generatedOfftake;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/generatedOfftakeItem.vm", "UTF-8", model);
	}
	
	//62
	public String getFormattedProductName() {
		return StringHelper.center(generatedOfftake.getProductName(), 62);
	}
	
	public String getFormattedOrder() {
		final String formattedOrder = generatedOfftake.getSuggestedOrder() + " " + generatedOfftake.getProductWholeUnit().getDisplayName();
		
		return String.format("%-13s", formattedOrder);
	}
	
	public String getFormattedOfftake() {
		DecimalFormat df = new DecimalFormat("0.0000");
		return String.format("%-13s", df.format(generatedOfftake.getOfftake()));
	}
}
