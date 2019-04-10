package com.chua.evergrocery.utility.template;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   21 Mar 2019
 */
public class EverHeaderTemplate extends AbstractTemplate {

	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/everHeader.vm", "UTF-8", model);
	}
	
	public String getDate() {
		return StringHelper.center(DateFormatter.longFormat(new Date()), 44);
	}
}
