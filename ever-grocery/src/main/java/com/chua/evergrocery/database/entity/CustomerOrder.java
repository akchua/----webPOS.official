package com.chua.evergrocery.database.entity;

import java.text.DecimalFormat;
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
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.serializer.json.CustomerSerializer;
import com.chua.evergrocery.serializer.json.UserSerializer;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name = "CustomerOrder")
@Table(name = CustomerOrder.TABLE_NAME)
public class CustomerOrder extends BaseObject {

	private static final long serialVersionUID = -2196136906086142891L;

	public static final String TABLE_NAME = "customer_order";

	private Long serialInvoiceNumber;
	
	private String name;
	
	@JsonSerialize(using = CustomerSerializer.class)
	private Customer customer;
	
	@JsonSerialize(using = UserSerializer.class)
	private User creator;
	
	@JsonSerialize(using = UserSerializer.class)
	private User cashier;
	
	private Float vatSales;
	
	private Float vatExSales;
	
	private Float zeroRatedSales;
	
	private DiscountType discountType;
	
	private Float totalDiscountAmount;
	
	private Float totalItems;
	
	private Status status;
	
	private Date paidOn;
	
	@Transient
	public String getOrderNumber() {
		return String.valueOf(this.getId() % 1000);
	}

	@Basic
	@Column(name = "SIN")
	public Long getSerialInvoiceNumber() {
		return serialInvoiceNumber;
	}
	
	@Transient
	public String getFormattedSIN() {
		DecimalFormat SIN_FORMAT = new DecimalFormat("0000000000");
		return SIN_FORMAT.format(serialInvoiceNumber);
	}

	public void setSerialInvoiceNumber(Long serialInvoiceNumber) {
		this.serialInvoiceNumber = serialInvoiceNumber;
	}

	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	@Transient
	public String getFormattedName() {
		final String formattedName;
		
		if(customer != null) {
			formattedName = customer.getFormattedName();
		} else {
			formattedName = name;
		}
		
		return formattedName;
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
	@Column(name = "vat_sales")
	public Float getVatSales() {
		return vatSales;
	}
	
	@Transient
	public String getFormattedVatSales() {
		return CurrencyFormatter.pesoFormat(vatSales);
	}

	public void setVatSales(Float vatSales) {
		this.vatSales = vatSales;
	}

	@Basic
	@Column(name = "vat_ex_sales")
	public Float getVatExSales() {
		return vatExSales;
	}
	
	@Transient
	public String getFormattedVatExSales() {
		return CurrencyFormatter.pesoFormat(vatExSales);
	}

	public void setVatExSales(Float vatExSales) {
		this.vatExSales = vatExSales;
	}

	@Basic
	@Column(name = "zero_rated_sales")
	public Float getZeroRatedSales() {
		return zeroRatedSales;
	}
	
	@Transient
	public String getFormattedZeroRatedSales() {
		return CurrencyFormatter.pesoFormat(zeroRatedSales);
	}

	public void setZeroRatedSales(Float zeroRatedSales) {
		this.zeroRatedSales = zeroRatedSales;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "discount_type", length = 50)
	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	@Transient
	public Float getGrossAmount() {
		return vatSales + vatExSales + zeroRatedSales;
	}
	
	@Transient
	public String getFormattedGrossAmount() {
		return CurrencyFormatter.pesoFormat(getGrossAmount());
	}
	
	@Basic
	@Column(name = "discount_amount")
	public Float getTotalDiscountAmount() {
		return totalDiscountAmount;
	}
	
	@Transient
	public String getFormattedTotalDiscountAmount() {
		return CurrencyFormatter.pesoFormat(getTotalDiscountAmount());
	}

	public void setTotalDiscountAmount(Float totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	@Transient
	public Float getTotalAmount() {
		return getGrossAmount() - getTotalDiscountAmount();
	}
	
	@Transient
	public String getFormattedTotalAmount() {
		return CurrencyFormatter.pesoFormat(getTotalAmount());
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

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "paid_on")
	public Date getPaidOn() {
		return paidOn;
	}
	
	@Transient
	public String getFormattedPaidOn() {
		final String formattedPaidOn;
		Calendar cal = Calendar.getInstance();
		cal.setTime(paidOn);
		
		if(cal.getTimeInMillis() == DateUtil.getDefaultDateInMillis()) {
			formattedPaidOn = "n/a";
		} else {
			formattedPaidOn = DateFormatter.shortFormat(paidOn);
		}
		return formattedPaidOn;
	}

	public void setPaidOn(Date paidOn) {
		this.paidOn = paidOn;
	}
}
