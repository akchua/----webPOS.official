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
import com.chua.evergrocery.serializer.json.CompanySerializer;
import com.chua.evergrocery.serializer.json.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name = "PurchaseOrder")
@Table(name = PurchaseOrder.TABLE_NAME)
public class PurchaseOrder extends BaseObject {

	private static final long serialVersionUID = 9196459786308898659L;

	public static final String TABLE_NAME = "purhcase_order";
	
	@JsonSerialize(using = CompanySerializer.class)
	private Company company;
	
	@JsonSerialize(using = UserSerializer.class)
	private User creator;
	
	@JsonSerialize(using = UserSerializer.class)
	private User managerInCharge;
	
	private Float totalAmount;
	
	private Integer totalItems;
	
	private Status status;

	@ManyToOne(targetEntity = Company.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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
	@JoinColumn(name = "MIC_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getManagerInCharge() {
		return managerInCharge;
	}

	public void setManagerInCharge(User managerInCharge) {
		this.managerInCharge = managerInCharge;
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
	public Integer getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Integer totalItems) {
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
