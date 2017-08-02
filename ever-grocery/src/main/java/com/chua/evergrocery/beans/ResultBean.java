package com.chua.evergrocery.beans;

import java.util.Map;

public class ResultBean {

	private Boolean success;
	
	private String message;
	
	private Map<String, Object> extras;
	
	public ResultBean() {
		
	}
	
	public ResultBean(Boolean success, String message) {
		setSuccess(success);
		setMessage(message);
	}
	
	public Boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getExtras() {
		return extras;
	}

	public void setExtras(Map<String, Object> extras) {
		this.extras = extras;
	}
}
