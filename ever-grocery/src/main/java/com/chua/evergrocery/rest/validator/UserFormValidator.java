package com.chua.evergrocery.rest.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.chua.evergrocery.beans.UserFormBean;
import com.chua.evergrocery.enums.UserType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   30 Nov 2017
 */
@Component
public class UserFormValidator extends AbstractFormValidator<UserFormBean> {

	@Override
	public Map<String, String> validate(UserFormBean userForm) {
		final Map<String, String> errors = new HashMap<String, String>();
		
		String temp = "";
		
		temp = validateFirstName(userForm.getFirstName());
		if(!temp.isEmpty()) errors.put("firstName", temp);
		temp = validateLastName(userForm.getLastName());
		if(!temp.isEmpty()) errors.put("lastName", temp);
		temp = validateEmailAddress(userForm.getEmailAddress());
		if(!temp.isEmpty()) errors.put("emailAddress", temp);
		temp = validateContactNumber(userForm.getContactNumber());
		if(!temp.isEmpty()) errors.put("contactNumber", temp);
		temp = validateUsername(userForm.getUsername());
		if(!temp.isEmpty()) errors.put("username", temp);
		temp = validateUserType(userForm.getUserType());
		if(!temp.isEmpty()) errors.put("userType", temp);
		
		return errors;
	}
	
	private String validateFirstName(String firstName) {
		return validateString(firstName, 2, 20);
	}
	
	private String validateLastName(String lastName) {
		return validateString(lastName, 2, 20);
	}
	
	private String validateEmailAddress(String emailAddress) {
		return validateEmail(emailAddress);
	}
	
	private String validateContactNumber(String contactNumber) {
		return validateString(contactNumber, 5, 20);
	}
	
	private String validateUsername(String username) {
		return notNull(username, (s) -> s.trim().matches("^[A-Za-z_]\\w{3,30}$") ? "" : "Username must be at least 3 to 30 characters and cannot contain white spaces and/or special characters.");
	}
	
	private String validateUserType(UserType userType) {
		return notNull(userType);
	}
}
