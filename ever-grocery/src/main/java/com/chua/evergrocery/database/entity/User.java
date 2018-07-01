package com.chua.evergrocery.database.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.enums.UserType;

@Entity(name = "User")
@Table(name = User.TABLE_NAME)
public class User extends BaseObject {

	private static final long serialVersionUID = 8823246613871411281L;

	public static final String TABLE_NAME = "user";
	
	private String image;
	
	private String firstName;
	
	private String lastName;
	
	private String emailAddress;
	
	private String contactNumber;
	
	private String username;
	
	private String password;
	
	private Integer itemsPerPage;
	
	private UserType userType;
	
	private Date lastSuccessfulLogin;

	private Date lastAudit;
	
	@Basic
	@Column(name = "image")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

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
	
	@Transient
	public String getFormattedName() {
		return lastName + ", " + firstName;
	}
	
	@Transient
	public String getShortName() {
		return Character.toUpperCase(firstName.charAt(0)) + "." + lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Basic
	@Column(name = "email_address")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Basic
	@Column(name = "contact_number")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Basic
	@NotNull
	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Basic
	@NotNull
	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Basic
	@NotNull
	@Column(name = "items_per_page")
	public Integer getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "user_type", length = 50)
	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_successful_login")
	public Date getLastSuccessfulLogin() {
		return lastSuccessfulLogin;
	}

	public void setLastSuccessfulLogin(Date lastSuccessfulLogin) {
		this.lastSuccessfulLogin = lastSuccessfulLogin;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_audit")
	public Date getLastAudit() {
		return lastAudit;
	}

	public void setLastAudit(Date lastAudit) {
		this.lastAudit = lastAudit;
	}
}
