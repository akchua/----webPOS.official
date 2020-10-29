package com.chua.evergrocery.beans;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public class CustomerSalesSummaryBean extends SalesSummaryBean {

	private Long customerId;
	
	private Float luxuryTotal;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Float getLuxuryTotal() {
		return luxuryTotal;
	}

	public void setLuxuryTotal(Float luxuryTotal) {
		this.luxuryTotal = luxuryTotal;
	}
}
