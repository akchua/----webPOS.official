package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chua.evergrocery.database.entity.base.BaseObject;

@Entity(name = "Distributor")
@Table(name = Distributor.TABLE_NAME)
public class Distributor extends BaseObject {
	private static final long serialVersionUID = 3988631027247989063L;
	
	public static final String TABLE_NAME = "distributor";
	
	private String name;
	private String address;
	private String agent;
	private String phoneNumber;
	
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
}
