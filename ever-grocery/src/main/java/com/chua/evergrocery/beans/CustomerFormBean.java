package com.chua.evergrocery.beans;

public class CustomerFormBean extends FormBean {

	private Long customerCategoryId;
	
	private String name;
	
	private String storeName;
	
	private String code;
	
	private String contactNumber;
	
	private String address;
	
	private String cardId;

	public Long getCustomerCategoryId() {
		return customerCategoryId;
	}

	public void setCustomerCategoryId(Long customerCategoryId) {
		this.customerCategoryId = customerCategoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
}
