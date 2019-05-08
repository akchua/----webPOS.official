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
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.serializer.json.CustomerSerializer;
import com.chua.evergrocery.serializer.json.UserSerializer;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.chua.evergrocery.utility.format.NumberFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name = "CustomerOrder")
@Table(name = CustomerOrder.TABLE_NAME)
public class CustomerOrder extends BaseObject {

	private static final long serialVersionUID = -2196136906086142891L;

	public static final String TABLE_NAME = "customer_order";

	private Long serialInvoiceNumber;
	
	private Long referenceSerialInvoiceNumber;
	
	private Long refundNumber;
	
	/*private String name;*/
	
	@JsonSerialize(using = CustomerSerializer.class)
	private Customer customer;
	
	@JsonSerialize(using = UserSerializer.class)
	private User creator;
	
	@JsonSerialize(using = UserSerializer.class)
	private User cashier;
	
	private Float vatSales;
	
	private Float vatExSales;
	
	private Float zeroRatedSales;
	
	private Float taxAdjustment;
	
	private DiscountType discountType;
	
	private Float vatDiscount;
	
	private Float vatExDiscount;
	
	private Float zeroRatedDiscount;
	
	private String discountIdNumber;
	
	private String discountName;
	
	private String discountAddress;
	
	private String discountTin;
	
	private Float totalItems;
	
	private Status status;
	
	private Date paidOn;
	
	private Float cash;
	
	private String checkAccountNumber;
	
	private String checkNumber;
	
	private Float checkAmount;
	
	private String cardTransactionNumber;
	
	private Float cardAmount;
	
	private Float pointsAmount;
	
	private Float pointsEarned;
	
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
		return NumberFormatter.SINFormat(getSerialInvoiceNumber());
	}

	public void setSerialInvoiceNumber(Long serialInvoiceNumber) {
		this.serialInvoiceNumber = serialInvoiceNumber;
	}
	
	@Basic()
	@Column(name = "ref_SIN")
	public Long getReferenceSerialInvoiceNumber() {
		return referenceSerialInvoiceNumber;
	}
	
	@Transient
	public String getFormattedRefSIN() {
		return NumberFormatter.SINFormat(getReferenceSerialInvoiceNumber());
	}

	public void setReferenceSerialInvoiceNumber(Long referenceSerialInvoiceNumber) {
		this.referenceSerialInvoiceNumber = referenceSerialInvoiceNumber;
	}

	@Basic
	@Column(name = "refund_number")
	public Long getRefundNumber() {
		return refundNumber;
	}
	
	@Transient
	public String getFormattedRefundNumber() {
		return NumberFormatter.SINFormat(getRefundNumber());
	}

	public void setRefundNumber(Long refundNumber) {
		this.refundNumber = refundNumber;
	}

	/*@Basic
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
	}*/

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
	public Float getNetVatSales() {
		return vatSales - vatDiscount;
	}
	
	@Transient
	public String getFormattedNetVatSales() {
		return CurrencyFormatter.pesoFormat(getNetVatSales());
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
		return CurrencyFormatter.pesoFormat(getVatExSales());
	}
	
	@Transient
	public Float getNetVatExSales() {
		return vatExSales - vatExDiscount;
	}
	
	@Transient
	public String getFormattedNetVatExSales() {
		return CurrencyFormatter.pesoFormat(getNetVatExSales());
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
		return CurrencyFormatter.pesoFormat(getZeroRatedSales());
	}
	
	@Transient
	public Float getNetZeroRatedSales() {
		return zeroRatedSales - zeroRatedDiscount;
	}
	
	@Transient
	public String getFormattedNetZeroRatedSales() {
		return CurrencyFormatter.pesoFormat(getNetZeroRatedSales());
	}

	public void setZeroRatedSales(Float zeroRatedSales) {
		this.zeroRatedSales = zeroRatedSales;
	}

	@Basic
	@Column(name = "tax_adjustment")
	public Float getTaxAdjustment() {
		return taxAdjustment;
	}

	public void setTaxAdjustment(Float taxAdjustment) {
		this.taxAdjustment = taxAdjustment;
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
		return getVatSales() + getVatExSales() + getZeroRatedSales();
	}
	
	@Transient
	public String getFormattedGrossAmount() {
		return CurrencyFormatter.pesoFormat(getGrossAmount());
	}

	@Basic
	@Column(name = "vat_discount")
	public Float getVatDiscount() {
		return vatDiscount;
	}

	public void setVatDiscount(Float vatDiscount) {
		this.vatDiscount = vatDiscount;
	}

	@Basic
	@Column(name = "vat_ex_discount")
	public Float getVatExDiscount() {
		return vatExDiscount;
	}

	public void setVatExDiscount(Float vatExDiscount) {
		this.vatExDiscount = vatExDiscount;
	}

	@Column(name = "zero_rated_discount")
	public Float getZeroRatedDiscount() {
		return zeroRatedDiscount;
	}

	public void setZeroRatedDiscount(Float zeroRatedDiscount) {
		this.zeroRatedDiscount = zeroRatedDiscount;
	}

	@Transient
	public Float getTotalDiscountAmount() {
		return vatDiscount + vatExDiscount + zeroRatedDiscount;
	}
	
	@Transient
	public String getFormattedTotalDiscountAmount() {
		return CurrencyFormatter.pesoFormat(getTotalDiscountAmount());
	}
	
	@Transient
	public Float getTotalDiscountableAmount() {
		return discountType.getPercentDiscount().equals(0.0f) ? 0.0f : getTotalDiscountAmount() / (discountType.getPercentDiscount() / 100.0f);
	}
	
	@Transient
	public String getFormattedTotalDiscountableAmount() {
		return CurrencyFormatter.pesoFormat(getTotalDiscountableAmount());
	}

	@Basic
	@Column(name = "discount_id_number")
	public String getDiscountIdNumber() {
		return discountIdNumber;
	}

	public void setDiscountIdNumber(String discountIdNumber) {
		this.discountIdNumber = discountIdNumber;
	}

	@Basic
	@Column(name = "discount_name")
	public String getDiscountName() {
		return discountName;
	}

	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}

	@Basic
	@Column(name = "discount_address")
	public String getDiscountAddress() {
		return discountAddress;
	}

	public void setDiscountAddress(String discountAddress) {
		this.discountAddress = discountAddress;
	}

	@Basic
	@Column(name = "discount_tin")
	public String getDiscountTin() {
		return discountTin;
	}

	public void setDiscountTin(String discountTin) {
		this.discountTin = discountTin;
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

	@Basic
	@Column(name = "cash")
	public Float getCash() {
		return cash;
	}
	
	@Transient
	public String getFormattedCash() {
		return CurrencyFormatter.pesoFormat(getCash());
	}

	public void setCash(Float cash) {
		this.cash = cash;
	}

	@Basic
	@Column(name = "check_account_number")
	public String getCheckAccountNumber() {
		return checkAccountNumber;
	}

	public void setCheckAccountNumber(String checkAccountNumber) {
		this.checkAccountNumber = checkAccountNumber;
	}

	@Basic
	@Column(name = "check_number")
	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Basic
	@Column(name = "check_amount")
	public Float getCheckAmount() {
		return checkAmount;
	}
	
	@Transient
	public String getFormattedCheckAmount() {
		return CurrencyFormatter.pesoFormat(getCheckAmount());
	}
	
	public void setCheckAmount(Float checkAmount) {
		this.checkAmount = checkAmount;
	}

	@Basic
	@Column(name = "card_txn_number")
	public String getCardTransactionNumber() {
		return cardTransactionNumber;
	}

	public void setCardTransactionNumber(String cardTransactionNumber) {
		this.cardTransactionNumber = cardTransactionNumber;
	}

	@Basic
	@Column(name = "card_amount")
	public Float getCardAmount() {
		return cardAmount;
	}
	
	@Transient
	public String getFormattedCardAmount() {
		return CurrencyFormatter.pesoFormat(getCardAmount());
	}
	
	public void setCardAmount(Float cardAmount) {
		this.cardAmount = cardAmount;
	}

	@Basic
	@Column(name = "points_amount")
	public Float getPointsAmount() {
		return pointsAmount;
	}
	
	@Transient
	public String getFormattedPointsAmount() {
		return CurrencyFormatter.pesoFormat(getPointsAmount());
	}
	
	@Transient
	public Float getTotalPayment() {
		return getCash() + getCheckAmount() + getCardAmount() + getPointsAmount();
	}
	
	@Transient
	public String getFormattedTotalPayment() {
		return CurrencyFormatter.pesoFormat(getTotalPayment());
	}

	public void setPointsAmount(Float pointsAmount) {
		this.pointsAmount = pointsAmount;
	}

	@Basic
	@Column(name = "points_earned")
	public Float getPointsEarned() {
		return pointsEarned;
	}
	
	@Transient
	public String getFormattedPointsEarned() {
		return CurrencyFormatter.pesoFormat(getPointsEarned());
	}

	public void setPointsEarned(Float pointsEarned) {
		this.pointsEarned = pointsEarned;
	}
}
