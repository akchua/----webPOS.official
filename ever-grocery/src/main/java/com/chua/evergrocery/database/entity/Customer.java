package com.chua.evergrocery.database.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.utility.format.CurrencyFormatter;

@Entity(name = "Customer")
@Table(name = Customer.TABLE_NAME)
public class Customer extends BaseObject {

	private static final long serialVersionUID = -1975395949715167723L;
	
	public static final String TABLE_NAME = "customer";
	
	private String firstName;
	
	private String lastName;
	
	private String contactNumber;
	
	private String address;
	
	private String cardId;
	
	private Float totalPoints;
	
	private Float usedPoints;
	
	private Date lastPurchase;

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
	
	@Transient
	public String getFormattedName() {
		return lastName + ", " + firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Basic
	@Column(name = "contact_number")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
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
	@Column(name = "card_id")
	public String getCardId() {
		return cardId;
	}
	
	@Transient
	public String getFormattedCardId() {
		return "*********" + getCardId().substring(9);
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	@Basic
	@Column(name = "total_points")
	public Float getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Float totalPoints) {
		this.totalPoints = totalPoints;
	}

	@Basic
	@Column(name = "used_points")
	public Float getUsedPoints() {
		return usedPoints;
	}
	
	@Transient
	public Float getAvailablePoints() {
		return getTotalPoints() - getUsedPoints();
	}
	
	@Transient
	public String getFormattedAvailablePoints() {
		return CurrencyFormatter.pesoFormat(getTotalPoints() - getUsedPoints());
	}

	public void setUsedPoints(Float usedPoints) {
		this.usedPoints = usedPoints;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_purchase", nullable = false)
	public Date getLastPurchase() {
		return lastPurchase;
	}

	public void setLastPurchase(Date lastPurchase) {
		this.lastPurchase = lastPurchase;
	}
}
