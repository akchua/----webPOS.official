package com.chua.evergrocery.beans;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Nov 17, 2019
 */
public class ProductDailySalesSummaryBean {

	private String formattedSalesDate;
	
	private Double baseTotal;
	
	private Double netTotal;
	
	public ProductDailySalesSummaryBean(String formattedSalesDate, Double baseTotal, Double netTotal) {
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

	public Double getBaseTotal() {
		return baseTotal;
	}

	public void setBaseTotal(Double baseTotal) {
		this.baseTotal = baseTotal;
	}

	public Double getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(Double netTotal) {
		this.netTotal = netTotal;
	}

	public Double getProfit() {
		return netTotal - baseTotal;
	}
}
