package com.chua.evergrocery.utility.template;

import org.apache.velocity.app.VelocityEngine;

import com.chua.evergrocery.enums.DocType;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public interface Template {

	String merge(VelocityEngine velocityEngine);
	
	String merge(VelocityEngine velocityEngine, DocType docType);
}
