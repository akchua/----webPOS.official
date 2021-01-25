package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
public class PurchaseSummaryBean {

	private Double grossTotal;
	
	private Double netTotal;

	public Double getGrossTotal() {
		return grossTotal == null ? 0.0d : grossTotal;
	}
	
	public void setGrossTotal(Double grossTotal) {
		this.grossTotal = grossTotal;
	}

	public Double getNetTotal() {
		return netTotal == null ? 0.0d : netTotal;
	}

	public void setNetTotal(Double netTotal) {
		this.netTotal = netTotal;
	}
}
