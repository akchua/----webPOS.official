package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.enums.ReceiptType;

@Entity(name = "Company")
@Table(name = Company.TABLE_NAME)
public class Company extends BaseObject {
	private static final long serialVersionUID = 5717539669561642586L;
	
	public static final String TABLE_NAME = "company";
	
	private String name;
	private String address;
	private String agent;
	private String phoneNumber;
	
	private ReceiptType receiptType;
	
	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Basic
	@Column(name = "address")
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Basic
	@Column(name = "agent")
	public String getAgent() {
		return agent;
	}
	
	public void setAgent(String agent) {
		this.agent = agent;
	}
	
	@Basic
	@Column(name = "phone_number")
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "receipt_type", length = 50)
	public ReceiptType getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(ReceiptType receiptType) {
		this.receiptType = receiptType;
	}
}
