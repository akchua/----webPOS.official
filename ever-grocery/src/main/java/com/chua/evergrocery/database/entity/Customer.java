package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chua.evergrocery.database.entity.base.BaseObject;

@Entity(name = "Customer")
@Table(name = Customer.TABLE_NAME)
public class Customer extends BaseObject {

	private static final long serialVersionUID = -1975395949715167723L;
	
	public static final String TABLE_NAME = "customer";
	
	private String firstName;
	
	private String lastName;
	
	private String address;

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
	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
