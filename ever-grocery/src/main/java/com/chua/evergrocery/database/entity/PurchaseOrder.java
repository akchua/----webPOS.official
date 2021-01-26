package com.chua.evergrocery.database.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.serializer.json.CompanySerializer;
import com.chua.evergrocery.serializer.json.UserSerializer;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name = "PurchaseOrder")
@Table(name = PurchaseOrder.TABLE_NAME)
public class PurchaseOrder extends BaseObject {

	private static final long serialVersionUID = 9196459786308898659L;

	public static final String TABLE_NAME = "purchase_order";
	
	@JsonSerialize(using = CompanySerializer.class)
	private Company company;
	
	@JsonSerialize(using = UserSerializer.class)
	private User creator;
	
	@JsonSerialize(using = UserSerializer.class)
	private User managerInCharge;
	
	private Double totalAmount;
	
	private Integer totalItems;
	
	private Status status;
	
	private Date deliveredOn;
	
	private Date checkedOn;

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
	public Double getTotalAmount() {
		return totalAmount;
	}
	
	@Transient
	public String getFormattedTotalAmount() {
		return CurrencyFormatter.pesoFormat(getTotalAmount());
	}


	public void setTotalAmount(Double totalAmount) {
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
	
	@Transient
	public Boolean isChecked() {
		return status.equals(Status.CHECKED);
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "delivered_on")
	public Date getDeliveredOn() {
		return deliveredOn;
	}
	
	@Transient
	public String getFormattedDeliveredOn() {
		final String formattedDeliveredOn;
		Calendar cal = Calendar.getInstance();
		cal.setTime(deliveredOn);
		
		if(cal.getTimeInMillis() == DateUtil.getDefaultDateInMillis()) {
			formattedDeliveredOn = "n/a";
		} else {
			formattedDeliveredOn = DateFormatter.shortFormat(deliveredOn);
		}
		return formattedDeliveredOn;
	}
	
	@Transient
	public String getFormattedTerms() {
		final String formattedTerms;
		Calendar min = Calendar.getInstance();
		min.setTime(deliveredOn);
		
		min.add(Calendar.DAY_OF_MONTH, company.getMinTerms());
		
		Calendar max = Calendar.getInstance();
		max.setTime(deliveredOn);
		
		max.add(Calendar.DAY_OF_MONTH, company.getMaxTerms());
		
		formattedTerms = DateFormatter.prettyFormat(min.getTime()) + " - " + DateFormatter.prettyFormat(max.getTime());
		
		return formattedTerms;
	}
	
	@Transient
	public String getFormattedMaxPaymentDate() {
		final String formattedMaxPaymentDate;
		Calendar cal = Calendar.getInstance();
		cal.setTime(deliveredOn);
		
		cal.add(Calendar.DAY_OF_MONTH, company.getMaxTerms());
		formattedMaxPaymentDate = DateFormatter.prettyFormat(cal.getTime());
		
		return formattedMaxPaymentDate;
	}

	public void setDeliveredOn(Date deliveredOn) {
		this.deliveredOn = deliveredOn;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "checked_on")
	public Date getCheckedOn() {
		return checkedOn;
	}
	
	@Transient
	public String getFormattedCheckedOn() {
		final String formattedCheckedOn;
		Calendar cal = Calendar.getInstance();
		cal.setTime(checkedOn);
		
		if(cal.getTimeInMillis() == DateUtil.getDefaultDateInMillis()) {
			formattedCheckedOn = "n/a";
		} else {
			formattedCheckedOn = DateFormatter.shortFormat(checkedOn);
		}
		return formattedCheckedOn;
	}

	public void setCheckedOn(Date checkedOn) {
		this.checkedOn = checkedOn;
	}
}
