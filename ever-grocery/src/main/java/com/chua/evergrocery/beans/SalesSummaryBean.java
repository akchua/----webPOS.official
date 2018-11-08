package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 7, 2018
 */
public class SalesSummaryBean {

	private Float netTotal;
	
	private Float totalProfit;
	
	public Float getNetTotal() {
		return netTotal == null ? 0.0f : netTotal;
	}

	public void setNetTotal(Float netTotal) {
		this.netTotal = netTotal;
	}

	public Float getTotalProfit() {
		return totalProfit == null ? 0.0f : totalProfit;
	}

	public void setTotalProfit(Float totalProfit) {
		this.totalProfit = totalProfit;
	}
}
