package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.serializer.json.CustomerSerializer;
import com.chua.evergrocery.serializer.json.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name = "CustomerOrder")
@Table(name = CustomerOrder.TABLE_NAME)
public class CustomerOrder extends BaseObject {

	private static final long serialVersionUID = -2196136906086142891L;

	public static final String TABLE_NAME = "customer_order";
	
	private String name;
	
	@JsonSerialize(using = CustomerSerializer.class)
	private Customer customer;
	
	@JsonSerialize(using = UserSerializer.class)
	private User creator;
	
	@JsonSerialize(using = UserSerializer.class)
	private User cashier;
	
	private Float totalAmount;
	
	private Float totalItems;
	
	private Status status;

	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(targetEntity = Customer.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "cashier_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCashier() {
		return cashier;
	}

	public void setCashier(User cashier) {
		this.cashier = cashier;
	}

	@Basic
	@Column(name = "total_amount")
	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Basic
	@Column(name = "total_items")
	public Float getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Float totalItems) {
		this.totalItems = totalItems;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 50)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
