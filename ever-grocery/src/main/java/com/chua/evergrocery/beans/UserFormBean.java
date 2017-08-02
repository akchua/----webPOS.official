package com.chua.evergrocery.beans;

import com.chua.evergrocery.enums.UserType;

public class UserFormBean extends FormBean {

	private String firstName;
	
	private String lastName;
	
	private Integer itemsPerPage;
	
	private String username;
	
	private String password;
	
	private UserType userType;
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Integer getItemsPerPage() {
		return itemsPerPage;
	}
	
	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserType getUserType() {
		return userType;
	}
	
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
}
