package com.chua.evergrocery.beans;

import com.chua.evergrocery.utility.format.CurrencyFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   8 Jul 2018
 */
public class DailySalesBreakdownBean {

	private String cashierName;
	
	private Float vatSales;
	
	private Float vatExSales;
	
	private Float zeroRatedSales;

	public String getCashierName() {
		return cashierName;
	}

	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

	public Float getVatSales() {
		return vatSales;
	}
	
	public String getFormattedVatSales() {
		return CurrencyFormatter.pesoFormat(vatSales);
	}
	
	public String getFormattedVatableSales() {
		return CurrencyFormatter.pesoFormat(vatSales / 1.12);
	}
	
	public String getFormattedVatAmount() {
		return CurrencyFormatter.pesoFormat(vatSales - (vatSales / 1.12));
	}

	public void setVatSales(Float vatSales) {
		this.vatSales = vatSales;
	}

	public Float getVatExSales() {
		return vatExSales;
	}
	
	public String getFormattedVatExSales() {
		return CurrencyFormatter.pesoFormat(vatExSales);
	}

	public void setVatExSales(Float vatExSales) {
		this.vatExSales = vatExSales;
	}

	public Float getZeroRatedSales() {
		return zeroRatedSales;
	}
	
	public String getFormattedZeroRatedSales() {
		return CurrencyFormatter.pesoFormat(zeroRatedSales);
	}

	public void setZeroRatedSales(Float zeroRatedSales) {
		this.zeroRatedSales = zeroRatedSales;
	}
	
	public Float getTotalSales() {
		return vatSales + vatExSales + zeroRatedSales;
	}
	
	public String getFormattedTotalSales() {
		return CurrencyFormatter.pesoFormat(getTotalSales());
	}
}
