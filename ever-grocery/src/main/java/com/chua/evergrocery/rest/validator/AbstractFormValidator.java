package com.chua.evergrocery.rest.validator;

import java.util.function.Function;

import com.chua.evergrocery.beans.FormBean;
import com.chua.evergrocery.utility.EmailUtil;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   30 Nov 2017
 */
public abstract class AbstractFormValidator<T extends FormBean> 
		implements FormValidator<T> {
	
	protected String notNull(Object o) {
		return o != null ? "" : "This field cannot be empty.";
	}
	
	protected String notNull(String s, Function<String, String> f) {
		String ret = notNull(s);
		if(!ret.isEmpty()) return ret;
		
		ret = f.apply(s);
		
		return ret;
	}
	
	protected String validateString(String s, int min, int max) {
		String ret = notNull(s);
		if(!ret.isEmpty()) return ret;
		
		return validateStringNull(s, min, max);
	}
	
	protected String validateStringNull(String s, int min, int max) {
		if(s == null) return "";
		String ret = "";
		if(min <= -1 && max <= -1) return s;
		
		if(min <= -1) {
			ret = s.trim().length() <= max ? "" : "This field cannot have more than " + max + " characters.";
		} else if(max <= -1) {
			ret = s.trim().length() >= min ? "" : "This field must be at least " + min + " characters long.";
		} else {
			ret = s.trim().length() >= min && s.trim().length() <= max 
					? "" : "This field requires " + min + " to " + max + " characters.";
		}
		
		return ret;
	}
	
	protected String validateInteger(Integer i, int min, int max) {
		String ret = notNull(i);
		if(!ret.isEmpty()) return ret;
		
		ret = i >= min && i <= max ? "" : "Value must be between " + min + " to " + max + ".";
		
		return ret;
	}
	
	protected String validateFloat(Float f, float min, float max) {
		String ret = notNull(f);
		if(!ret.isEmpty()) return ret;
		
		ret = f >= min && f <= max ? "" : "Value must be between " + min + " to " + max + ".";
		
		return ret;
	}
	
	protected String validateEmail(String email) {
		String ret = notNull(email);
		if(!ret.isEmpty()) return ret;
		
		ret = EmailUtil.validateEmail(email) ? "" : "Invalid email.";
		
		return ret;
	}
}
