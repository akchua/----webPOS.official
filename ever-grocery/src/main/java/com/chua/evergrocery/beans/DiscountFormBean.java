package com.chua.evergrocery.beans;

import com.chua.evergrocery.enums.DiscountType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   21 Mar 2019
 */
public class DiscountFormBean extends FormBean {

	private Long customerOrderId;
	
	private DiscountType discountType;
	
	private Float grossAmountLimit;
	
	private String discountIdNumber;

	public Long getCustomerOrderId() {
		return customerOrderId;
	}

	public void setCustomerOrderId(Long customerOrderId) {
		this.customerOrderId = customerOrderId;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public Float getGrossAmountLimit() {
		return grossAmountLimit;
	}

	public void setGrossAmountLimit(Float grossAmountLimit) {
		this.grossAmountLimit = grossAmountLimit;
	}

	public String getDiscountIdNumber() {
		return discountIdNumber;
	}

	public void setDiscountIdNumber(String discountIdNumber) {
		this.discountIdNumber = discountIdNumber;
	}
}
