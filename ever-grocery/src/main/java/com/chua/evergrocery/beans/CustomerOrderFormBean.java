package com.chua.evergrocery.beans;

public class CustomerOrderFormBean extends FormBean {

	private String name;
	
	private Long customerId;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
}
