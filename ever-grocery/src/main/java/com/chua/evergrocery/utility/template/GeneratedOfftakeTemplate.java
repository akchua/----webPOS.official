package com.chua.evergrocery.utility.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.GeneratedOfftakeBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 25, 2019
 */
public class GeneratedOfftakeTemplate extends AbstractTemplate {
	
	private String companyName;
	
	private Float offtakeDays;
	
	private List<GeneratedOfftakeBean> generateOfftakes;
	
	private List<String> formattedGeneratedOfftakes;

	public GeneratedOfftakeTemplate(String companyName, Float offtakeDays,
			List<GeneratedOfftakeBean> generateOfftakes) {
		this.companyName = companyName;
		this.offtakeDays = offtakeDays;
		this.generateOfftakes = generateOfftakes;
		this.formattedGeneratedOfftakes = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		for(GeneratedOfftakeBean generateOfftake : generateOfftakes) {
			final GeneratedOfftakeItemTemplate genOfftakeItemTemplate = new GeneratedOfftakeItemTemplate(generateOfftake);
			formattedGeneratedOfftakes.add(genOfftakeItemTemplate.merge(velocityEngine, docType));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/generatedOfftake.vm", "UTF-8", model);
	}
	
	public String getCompanyName() {
		return this.companyName;
	}
	
	public String getDate() {
		return DateFormatter.longFormat(new Date());
	}
	
	public String getOfftakeDays() {
		return offtakeDays + "";
	}
	
	public List<String> getFormattedGeneratedOfftakes() {
		return formattedGeneratedOfftakes;
	}
}
