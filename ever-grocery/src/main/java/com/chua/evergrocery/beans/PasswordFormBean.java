package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 30, 2018
 */
public class PasswordFormBean extends FormBean {

	private String oldPassword;
	
	private String password;
	
	private String confirmPassword;
	
	public PasswordFormBean() {
		
	}
	
	public PasswordFormBean(String oldPassword, String password, String confirmPassword) {
		setOldPassword(oldPassword);
		setPassword(password);
		setConfirmPassword(confirmPassword);
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
