package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
public class PurchaseSummaryBean {

	private Float grossTotal;
	
	private Float netTotal;

	public Float getGrossTotal() {
		return grossTotal == null ? 0.0f : grossTotal;
	}
	
	public void setGrossTotal(Float grossTotal) {
		this.grossTotal = grossTotal;
	}

	public Float getNetTotal() {
		return netTotal == null ? 0.0f : netTotal;
	}

	public void setNetTotal(Float netTotal) {
		this.netTotal = netTotal;
	}
}
