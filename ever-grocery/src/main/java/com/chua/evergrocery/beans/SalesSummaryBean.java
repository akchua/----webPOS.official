package com.chua.evergrocery.beans;

import com.chua.evergrocery.utility.format.CurrencyFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 7, 2018
 */
public class SalesSummaryBean {

	private Double netTotal;
	
	private Double baseTotal;
	
	public Double getNetTotal() {
		return netTotal == null ? 0.0d : netTotal;
	}
	
	public String getFormattedNetTotal() {
		return CurrencyFormatter.pesoFormat(getNetTotal());
	}

	public void setNetTotal(Double netTotal) {
		this.netTotal = netTotal;
	}
	
	public Double getBaseTotal() {
		return baseTotal == null ? 0.0d : baseTotal;
	}
	
	public Double getTotalProfit() {
		return getNetTotal() - getBaseTotal();
	}
	
	public String getFormattedTotalProfit() {
		return CurrencyFormatter.pesoFormat(getTotalProfit());
	}
	public void setBaseTotal(Double baseTotal) {
		this.baseTotal = baseTotal;
	}
}
