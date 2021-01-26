package com.chua.evergrocery.database.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.utility.format.CurrencyFormatter;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since 22 Mar 2019
 */
@MappedSuperclass
public abstract class Reading extends BaseObject {

	private static final long serialVersionUID = -6884487623884782110L;
	
	private Date readingDate;

	private Double beginningBalance;
	
	private Float zeroRatedRemovedVat;

	private Float regularDiscountAmount;

	private Float seniorDiscountAmount;

	private Float pwdDiscountAmount;

	private Float refundAmount;
	
	private Float totalCheckPayment;
	
	private Float totalCardPayment;
	
	private Float totalPointsPayment;

	@Transient
	public abstract Double getNetSales();
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "reading_date", nullable = false)
	public Date getReadingDate() {
		return readingDate;
	}
	
	public void setReadingDate(Date readingDate) {
		this.readingDate = readingDate;
	}
	
	@Basic
	@Column(name = "beginning_balance")
	public Double getBeginningBalance() {
		return beginningBalance;
	}

	@Transient
	public String getFormattedBeginningBalance() {
		return CurrencyFormatter.pesoFormat(getBeginningBalance());
	}
	
	@Transient
	public Double getEndingBalance() {
		return getBeginningBalance() + getGrossSales();
	}
	
	@Transient
	public String getFormattedEndingBalance() {
		return CurrencyFormatter.pesoFormat(getEndingBalance());
	}

	public void setBeginningBalance(Double beginningBalance) {
		this.beginningBalance = beginningBalance;
	}

	@Basic
	@Column(name = "zero_rated_removed_vat")
	public Float getZeroRatedRemovedVat() {
		return zeroRatedRemovedVat;
	}

	public void setZeroRatedRemovedVat(Float zeroRatedRemovedVat) {
		this.zeroRatedRemovedVat = zeroRatedRemovedVat;
	}

	@Basic
	@Column(name = "regular_discount_amount")
	public Float getRegularDiscountAmount() {
		return regularDiscountAmount;
	}

	@Transient
	public String getFormattedRegularDiscountAmount() {
		return CurrencyFormatter.pesoFormat(getRegularDiscountAmount());
	}

	public void setRegularDiscountAmount(Float regularDiscountAmount) {
		this.regularDiscountAmount = regularDiscountAmount;
	}

	@Basic
	@Column(name = "senior_discount_amount")
	public Float getSeniorDiscountAmount() {
		return seniorDiscountAmount;
	}

	@Transient
	public String getFormattedSeniorDiscountAmount() {
		return CurrencyFormatter.pesoFormat(getSeniorDiscountAmount());
	}

	public void setSeniorDiscountAmount(Float seniorDiscountAmount) {
		this.seniorDiscountAmount = seniorDiscountAmount;
	}

	@Basic
	@Column(name = "pwd_discount_amount")
	public Float getPwdDiscountAmount() {
		return pwdDiscountAmount;
	}
	
	@Transient
	public Float getTotalSpecialDiscountAmount() {
		return seniorDiscountAmount + pwdDiscountAmount;
	}
	
	@Transient
	public String getFormattedTotalSpecialDiscountAmount() {
		return CurrencyFormatter.pesoFormat(getTotalSpecialDiscountAmount());
	}

	@Transient
	public String getFormattedPwdDiscountAmount() {
		return CurrencyFormatter.pesoFormat(getPwdDiscountAmount());
	}

	@Transient
	public Float getTotalDiscountAmount() {
		return getRegularDiscountAmount() + getSeniorDiscountAmount() + getPwdDiscountAmount();
	}
	
	@Transient
	public String getFormattedTotalDiscountAmount() {
		return CurrencyFormatter.pesoFormat(getTotalDiscountAmount());
	}

	@Transient
	public Double getGrossSales() {
		return getNetSales() + getZeroRatedRemovedVat() + getTotalDiscountAmount() - getRefundAmount();
	}

	@Transient
	public String getFormattedGrossSales() {
		return CurrencyFormatter.pesoFormat(getGrossSales());
	}

	public void setPwdDiscountAmount(Float pwdDiscountAmount) {
		this.pwdDiscountAmount = pwdDiscountAmount;
	}

	@Basic
	@Column(name = "refund_amount")
	public Float getRefundAmount() {
		return refundAmount;
	}

	@Transient
	public String getFormattedRefundAmount() {
		return CurrencyFormatter.pesoFormat(getRefundAmount());
	}
	
	@Transient
	public Float getTotalDeductions() {
		return getTotalDiscountAmount() - getRefundAmount();
	}
	
	@Transient
	public String getFormattedTotalDeductions() {
		return CurrencyFormatter.pesoFormat(getTotalDeductions());
	}

	public void setRefundAmount(Float refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	@Basic
	@Column(name = "total_check_payment")
	public Float getTotalCheckPayment() {
		return totalCheckPayment;
	}

	@Transient
	public String getFormattedTotalCheckPayment() {
		return CurrencyFormatter.pesoFormat(getTotalCheckPayment());
	}

	public void setTotalCheckPayment(Float totalCheckPayment) {
		this.totalCheckPayment = totalCheckPayment;
	}

	@Basic
	@Column(name = "total_card_payment")
	public Float getTotalCardPayment() {
		return totalCardPayment;
	}
	
	@Transient
	public String getFormattedTotalCardPayment() {
		return CurrencyFormatter.pesoFormat(getTotalCardPayment());
	}
	
	public void setTotalCardPayment(Float totalCardPayment) {
		this.totalCardPayment = totalCardPayment;
	}

	@Basic
	@Column(name = "total_points_payment")
	public Float getTotalPointsPayment() {
		return totalPointsPayment;
	}
	
	@Transient
	public String getFormattedTotalPointsPayment() {
		return CurrencyFormatter.pesoFormat(getTotalPointsPayment());
	}
	
	@Transient
	public Double getTotalCashPayment() {
		return getNetSales() - getTotalCheckPayment() - getTotalCardPayment() - getTotalPointsPayment();
	}

	@Transient
	public String getFormattedTotalCashPayment() {
		return CurrencyFormatter.pesoFormat(getTotalCashPayment());
	}

	public void setTotalPointsPayment(Float totalPointsPayment) {
		this.totalPointsPayment = totalPointsPayment;
	}
}
