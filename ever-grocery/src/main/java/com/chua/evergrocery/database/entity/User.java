package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.enums.UserType;

@Entity(name = "User")
@Table(name = User.TABLE_NAME)
public class User extends BaseObject {

	private static final long serialVersionUID = 8823246613871411281L;

	public static final String TABLE_NAME = "user";
	
	private String firstName;
	
	private String lastName;
	
	private Integer itemsPerPage = Integer.valueOf(10);
	
	private String username;
	
	private String password;
	
	private UserType userType;
	
	@Basic
	@Column(name = "first_name")
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Basic
	@Column(name = "last_name")
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Basic
	@Column(name = "items_per_page")
	public Integer getItemsPerPage() {
		return itemsPerPage;
	}
	
	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
	
	@Basic
	@Column(name = "username")
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Basic
	@Column(name = "password")
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "user_type", length = 50)
	public UserType getUserType() {
		return userType;
	}
	
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
}
