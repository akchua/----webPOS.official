package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 7, 2018
 */
public class SalesSummaryBean {

	private Float netTotal;
	
	private Float baseTotal;
	
	public Float getNetTotal() {
		return netTotal == null ? 0.0f : netTotal;
	}

	public void setNetTotal(Float netTotal) {
		this.netTotal = netTotal;
	}

	public Float getBaseTotal() {
		return baseTotal == null ? 0.0f : baseTotal;
	}
	
	public Float getTotalProfit() {
		return getNetTotal() - getBaseTotal();
	}

	public void setBaseTotal(Float baseTotal) {
		this.baseTotal = baseTotal;
	}
}
