package com.chua.evergrocery.rest.handler.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.UserFormBean;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.enums.UserType;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.UserHandler;
import com.chua.evergrocery.utility.EncryptionUtil;

@Transactional
@Component
public class UserHandlerImpl implements UserHandler {

	@Autowired
	private UserService userService;

	@Override
	public ObjectList<User> getUserObjectList(Integer pageNumber, String searchKey) {
		return userService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public User getUser(Long userId) {
		return userService.find(userId);
	}
	
	@Override
	public ResultBean createUser(UserFormBean userForm) {
		final ResultBean result;
		
		if(!userService.isExistsByUsername(userForm.getUsername())) {
			final User user = new User();
			user.setPassword(EncryptionUtil.getMd5(userForm.getPassword()));
			setUser(user, userForm);
			
			result = new ResultBean();
			result.setSuccess(userService.insert(user) != null);
			if(result.getSuccess()) {
				result.setMessage("User successfully created.");
			} else {
				result.setMessage("Failed to create user.");
			}
		} else {
			result = new ResultBean(false, "Username \"" + userForm.getUsername() + "\" already exists!");
		}
		
		return result;
	}
	
	@Override
	public ResultBean updateUser(UserFormBean userForm) {
		final ResultBean result;
		
		if(userForm.getId() != 1l || userForm.getId() == UserContextHolder.getUser().getUserId()) {
			final User user = userService.find(userForm.getId());
			if(user != null) {
				if(!(StringUtils.trimToEmpty(user.getUsername()).equalsIgnoreCase(userForm.getUsername())) &&
						userService.isExistsByUsername(userForm.getUsername())) {
					result = new ResultBean(false, "Username \"" + userForm.getUsername() + "\" already exists!");
				} else {
					setUser(user, userForm);
					
					result = new ResultBean();
					result.setSuccess(userService.update(user));
					if(result.getSuccess()) {
						if(user.getId().equals(UserContextHolder.getUser().getUserId())) {
							UserContextHolder.refreshUser(user);
						}
						result.setMessage("User successfully updated.");
					} else {
						result.setMessage("Failed to update user.");
					}
				}
			} else {
				result = new ResultBean(false, "User not found.");
			}
		} else {
			result = new ResultBean(false, "Cannot edit this account.");
		}
		
		return result;
	}

	@Override
	public ResultBean removeUser(Long userId) {
		final ResultBean result;
		
		if(!(userId == UserContextHolder.getUser().getUserId() || userId == 1l)) {
			final User user = userService.find(userId);
			if(user != null) {
				result = new ResultBean();
				
				result.setSuccess(userService.delete(user));
				if(result.getSuccess()) {
					result.setMessage("Successfully removed User \"" + user.getUsername() + "\".");
				} else {
					result.setMessage("Failed to remove User \"" + user.getUsername() + "\".");
				}
			} else {
				result = new ResultBean(false, "User not found.");
			}
		} else {
			result = new ResultBean(false, "Cannot delete this account");
		}
		
		return result;
	}
	
	@Override
	public List<User> getUserList() {
		return userService.findAllOrderByLastName();
	}
	
	private void setUser(User user, UserFormBean userForm) {
		user.setFirstName(userForm.getFirstName());
		user.setLastName(userForm.getLastName());
		user.setItemsPerPage(userForm.getItemsPerPage());
		user.setUserType(userForm.getUserType());
		user.setUsername(userForm.getUsername());
	}

	@Override
	public List<String> getUserTypeList() {
		return Arrays.asList(UserType.values()).stream().map(UserType::name).collect(Collectors.toList());
	}
}
