package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
public class PurchaseSummaryBean {

	private Float grossTotal;
	
	private Float netTotal;
	
	public PurchaseSummaryBean() {
		this.grossTotal = 0.0f;
		this.netTotal = 0.0f;
	}

	public Float getGrossTotal() {
		return grossTotal;
	}

	public Float getNetTotal() {
		return netTotal;
	}

	public void setGrossTotal(Float grossTotal) {
		this.grossTotal = grossTotal;
	}

	public void setNetTotal(Float netTotal) {
		this.netTotal = netTotal;
	}
}
