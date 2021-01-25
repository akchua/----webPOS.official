package com.chua.evergrocery.beans;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public class CustomerSalesSummaryBean extends SalesSummaryBean {

	private Long customerId;
	
	private Double luxuryTotal;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Double getLuxuryTotal() {
		return luxuryTotal;
	}

	public void setLuxuryTotal(Double luxuryTotal) {
		this.luxuryTotal = luxuryTotal;
	}
}
