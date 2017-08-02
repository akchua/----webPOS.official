package com.chua.evergrocery.rest.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.PasswordBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.UserFormBean;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.rest.handler.SettingsHandler;
import com.chua.evergrocery.utility.EncryptionUtil;

@Transactional
@Component
public class SettingsHandlerImpl implements SettingsHandler {

	@Autowired
	private UserService userService;
	
	@Override
	public ResultBean updateSettings(UserFormBean userForm) {
		final ResultBean result;
		
		final User user = userService.find(UserContextHolder.getUser().getUserId());
		
		if(user != null) {
			user.setFirstName(userForm.getFirstName());
			user.setLastName(userForm.getLastName());
			user.setItemsPerPage(userForm.getItemsPerPage());
				
			result = new ResultBean();
			result.setSuccess(userService.update(user));
			if(result.getSuccess()) {
				UserContextHolder.refreshUser(user);
				result.setMessage("Your profile has been updated.");
			} else {
				result.setMessage("Failed to update profile.");
			}
		} else {
			result = new ResultBean(false, "Internal Error: User not found!");
		}
		
		return result;
	}

	@Override
	public ResultBean changePassword(PasswordBean passwordBean) {
		final ResultBean result;
		
		final User user = userService.find(UserContextHolder.getUser().getUserId());
		
		if(user != null) {
			if(EncryptionUtil.getMd5(passwordBean.getCurrentPassword()).equals(user.getPassword())) {
				if(passwordBean.getNewPassword().equals(passwordBean.getRePassword())) {
					user.setPassword(EncryptionUtil.getMd5(passwordBean.getNewPassword()));
					
					result = new ResultBean();
					result.setSuccess(userService.insert(user) != null);
					if(result.getSuccess()) {
						UserContextHolder.refreshUser(user);
						result.setMessage("Your password has been changed.");
					} else {
						result.setMessage("Failed to change password.");
					}
				} else {
					result = new ResultBean(false, "Password mismatch.");
				}
			} else {
				result = new ResultBean(false, "Wrong password.");
			}
		} else {
			result = new ResultBean(false, "Internal Error: User not found!");
		}
		
		return result;
	}
}
