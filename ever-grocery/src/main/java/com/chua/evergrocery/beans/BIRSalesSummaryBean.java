package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   22 Mar 2019
 */
public class BIRSalesSummaryBean {

	private Long beginningSIN;
	
	private Long endingSIN;
	
	private Long beginningRefundNumber;
	
	private Long endingRefundNumber;
	
	private Double vatSales;
	
	private Float vatExSales;
	
	private Float zeroRatedSales;
	
	private Float vatAdjustment;
	
	private Float vatDiscount;
	
	private Float vatExDiscount;
	
	private Float zeroRatedDiscount;
	
	private Float checkAmount;
	
	private Float cardAmount;
	
	private Float pointsAmount;
	
	public Long getBeginningSIN() {
		return beginningSIN;
	}

	public void setBeginningSIN(Long beginningSIN) {
		this.beginningSIN = beginningSIN;
	}

	public Long getEndingSIN() {
		return endingSIN;
	}

	public void setEndingSIN(Long endingSIN) {
		this.endingSIN = endingSIN;
	}

	public Long getBeginningRefundNumber() {
		return beginningRefundNumber;
	}

	public void setBeginningRefundNumber(Long beginningRefundNumber) {
		this.beginningRefundNumber = beginningRefundNumber;
	}

	public Long getEndingRefundNumber() {
		return endingRefundNumber;
	}

	public void setEndingRefundNumber(Long endingRefundNumber) {
		this.endingRefundNumber = endingRefundNumber;
	}

	public Double getVatSales() {
		return vatSales;
	}

	public void setVatSales(Double vatSales) {
		this.vatSales = vatSales;
	}

	public Float getVatExSales() {
		return vatExSales;
	}

	public void setVatExSales(Float vatExSales) {
		this.vatExSales = vatExSales;
	}

	public Float getZeroRatedSales() {
		return zeroRatedSales;
	}

	public void setZeroRatedSales(Float zeroRatedSales) {
		this.zeroRatedSales = zeroRatedSales;
	}

	public Float getVatAdjustment() {
		return vatAdjustment;
	}

	public void setVatAdjustment(Float vatAdjustment) {
		this.vatAdjustment = vatAdjustment;
	}

	public Float getVatDiscount() {
		return vatDiscount;
	}
	
	public Double getNetVatSales() {
		return (getVatSales() != null ? getVatSales() : 0) - 
				(getVatDiscount() != null ? getVatDiscount() : 0);
	}

	public void setVatDiscount(Float vatDiscount) {
		this.vatDiscount = vatDiscount;
	}

	public Float getVatExDiscount() {
		return vatExDiscount;
	}
	
	public Float getNetVatExSales() {
		return (getVatExSales() != null ? getVatExSales() : 0) - 
				(getVatExDiscount() != null ? getVatExDiscount() : 0);
	}

	public void setVatExDiscount(Float vatExDiscount) {
		this.vatExDiscount = vatExDiscount;
	}

	public Float getZeroRatedDiscount() {
		return zeroRatedDiscount;
	}
	
	public Float getNetZeroRatedSales() {
		return (getZeroRatedSales() != null ? getZeroRatedSales() : 0) - 
				(getZeroRatedDiscount() != null ? getZeroRatedDiscount() : 0);
	}

	public void setZeroRatedDiscount(Float zeroRatedDiscount) {
		this.zeroRatedDiscount = zeroRatedDiscount;
	}
	
	public Double getNetSales() {
		return getNetVatSales() + getNetVatExSales() + getNetZeroRatedSales();
	}

	public Float getCheckAmount() {
		return checkAmount;
	}

	public void setCheckAmount(Float checkAmount) {
		this.checkAmount = checkAmount;
	}

	public Float getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(Float cardAmount) {
		this.cardAmount = cardAmount;
	}

	public Float getPointsAmount() {
		return pointsAmount;
	}

	public void setPointsAmount(Float pointsAmount) {
		this.pointsAmount = pointsAmount;
	}
}
