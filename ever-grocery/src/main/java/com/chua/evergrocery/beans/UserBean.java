package com.chua.evergrocery.beans;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.enums.UserType;

public class UserBean extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = -1653103404363381254L;
	
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
	
	public Long getUserId() {
		return user.getId();
	}
	
	public String getFirstName() {
		return user.getFirstName();
	}
	
	public String getLastName() {
		return user.getLastName();
	}
	
	public Integer getItemsPerPage() {
		return user.getItemsPerPage();
	}
	
	public UserType getUserType() {
		return user.getUserType();
	}
	
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}
}
