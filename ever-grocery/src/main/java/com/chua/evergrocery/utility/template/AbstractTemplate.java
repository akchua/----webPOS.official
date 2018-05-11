package com.chua.evergrocery.utility.template;

import org.apache.velocity.app.VelocityEngine;

import com.chua.evergrocery.enums.DocType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 11, 2018
 */
public abstract class AbstractTemplate implements Template {

	public String merge(VelocityEngine velocityEngine) {
		return merge(velocityEngine, DocType.DEFAULT);
	}
}
