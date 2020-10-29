package com.chua.evergrocery.beans;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public class CustomerCategorySalesSummaryBean extends SalesSummaryBean {

	private Long customerCategoryId;
	
	private Float luxuryTotal;

	public Long getCustomerCategoryId() {
		return customerCategoryId;
	}

	public void setCustomerCategoryId(Long customerCategoryId) {
		this.customerCategoryId = customerCategoryId;
	}

	public Float getLuxuryTotal() {
		return luxuryTotal;
	}

	public void setLuxuryTotal(Float luxuryTotal) {
		this.luxuryTotal = luxuryTotal;
	}
}
