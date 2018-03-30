package com.chua.evergrocery.beans;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.enums.UserType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 30, 2017
 */
public class UserBean extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = -6785402734231625632L;
	
	private User user;

	public UserBean(String username, String password, Collection<? extends GrantedAuthority> authorities, User user) {
		super(username, password, authorities);
		setUser(user);
	}

	public User getUserEntity() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Long getId() {
		return user.getId();
	}
	
	public String getImage() {
		return user.getImage();
	}
	
	public String getFirstName() {
		return user.getFirstName();
	}
	
	public String getLastName() {
		return user.getLastName();
	}
	
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}
	
	public String getEmailAddress() {
		return user.getEmailAddress();
	}
	
	public String getContactNumber() {
		return user.getContactNumber();
	}
	
	public Integer getItemsPerPage() {
		return user.getItemsPerPage();
	}
	
	public UserType getUserType() {
		return user.getUserType();
	}
}
