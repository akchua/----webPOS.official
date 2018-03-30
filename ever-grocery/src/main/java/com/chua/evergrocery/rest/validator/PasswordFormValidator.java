package com.chua.evergrocery.rest.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.chua.evergrocery.beans.PasswordFormBean;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   30 Nov 2017
 */
@Component
public class PasswordFormValidator extends AbstractFormValidator<PasswordFormBean> {

	@Override
	public Map<String, String> validate(PasswordFormBean passwordForm) {
		final Map<String, String> errors = new HashMap<String, String>();
		
		String temp = "";
		
		temp = notNull(passwordForm.getOldPassword());
		if(!temp.isEmpty()) errors.put("oldPassword", temp);
		temp = validatePassword(passwordForm.getPassword());
		if(!temp.isEmpty()) errors.put("password", temp);
		temp = validatePassword(passwordForm.getConfirmPassword());
		if(!temp.isEmpty()) errors.put("confirmPassword", temp);
		
		if(errors.isEmpty()) {
			if(!passwordForm.getPassword().equals(passwordForm.getConfirmPassword())) errors.put("confirmPassword", "Confirm password does not match.");
		}
		
		return errors;
	}
	
	private String validatePassword(String password) {
		return notNull(password, (s) -> s.matches("((?=.*\\d)(?=.*[a-zA-Z])\\S{5,21})") ? "" : "Password must be at least 6 to 20 characters and cannot contain white spaces and must be a combination of letters and digits.");
	}
}
