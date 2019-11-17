package com.chua.evergrocery.beans;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Nov 17, 2019
 */
public class ProductDailySalesSummaryBean {

	private String formattedSalesDate;
	
	private Float baseTotal;
	
	private Float netTotal;
	
	public ProductDailySalesSummaryBean(String formattedSalesDate, Float baseTotal, Float netTotal) {
		super();
		this.formattedSalesDate = formattedSalesDate;
		this.baseTotal = baseTotal;
		this.netTotal = netTotal;
	}

	public String getFormattedSalesDate() {
		return formattedSalesDate;
	}

	public void setFormattedSalesDate(String formattedSalesDate) {
		this.formattedSalesDate = formattedSalesDate;
	}

	public Float getBaseTotal() {
		return baseTotal;
	}

	public void setBaseTotal(Float baseTotal) {
		this.baseTotal = baseTotal;
	}

	public Float getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(Float netTotal) {
		this.netTotal = netTotal;
	}

	public Float getProfit() {
		return netTotal - baseTotal;
	}
}
