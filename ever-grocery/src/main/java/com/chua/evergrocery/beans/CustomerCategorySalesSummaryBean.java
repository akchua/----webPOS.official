package com.chua.evergrocery.beans;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public class CustomerCategorySalesSummaryBean extends SalesSummaryBean {

	private Long customerCategoryId;
	
	private Double luxuryTotal;

	public Long getCustomerCategoryId() {
		return customerCategoryId;
	}

	public void setCustomerCategoryId(Long customerCategoryId) {
		this.customerCategoryId = customerCategoryId;
	}

	public Double getLuxuryTotal() {
		return luxuryTotal;
	}

	public void setLuxuryTotal(Double luxuryTotal) {
		this.luxuryTotal = luxuryTotal;
	}
}
