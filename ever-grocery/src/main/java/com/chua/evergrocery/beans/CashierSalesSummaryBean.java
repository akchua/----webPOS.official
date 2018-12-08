package com.chua.evergrocery.beans;

import com.chua.evergrocery.utility.format.CurrencyFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 8, 2018
 */
public class CashierSalesSummaryBean {

	private Long cashierId;
	
	private String cashierFirstName;
	
	private String cashierLastName;
	
	private Float vatSales;
	
	private Float vatExSales;
	
	private Float zeroRatedSales;
	
	private Float discountAmount;

	public Long getCashierId() {
		return cashierId;
	}

	public void setCashierId(Long cashierId) {
		this.cashierId = cashierId;
	}

	public String getCashierFirstName() {
		return cashierFirstName;
	}
	
	public String getCashierFormattedName() {
		return cashierLastName + ", " + cashierFirstName;
	}

	public void setCashierFirstName(String cashierFirstName) {
		this.cashierFirstName = cashierFirstName;
	}

	public String getCashierLastName() {
		return cashierLastName;
	}

	public void setCashierLastName(String cashierLastName) {
		this.cashierLastName = cashierLastName;
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

	public Float getDiscountAmount() {
		return discountAmount;
	}
	
	public String getFormattedDiscountAmount() {
		return CurrencyFormatter.pesoFormat(discountAmount);
	}

	public void setDiscountAmount(Float discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	public Float getTotalSales() {
		return vatSales + vatExSales + zeroRatedSales;
	}
	
	public String getFormattedTotalSales() {
		return CurrencyFormatter.pesoFormat(getTotalSales());
	}
}
