package com.chua.evergrocery.beans;

import java.util.Date;
import java.util.List;

import com.chua.evergrocery.utility.format.CurrencyFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 8, 2018
 */
public class DailySalesReportBean {

	private Date saleDate;
	
	private SINRangeBean sinRange;
	
	private Float totalVatSales;
	
	private Float totalVatExSales;
	
	private Float totalZeroRatedSales;
	
	private Float totalDiscountAmount;
	
	private List<CashierSalesSummaryBean> cashierSalesSummaries;

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public SINRangeBean getSinRange() {
		return sinRange;
	}

	public void setSinRange(SINRangeBean sinRange) {
		this.sinRange = sinRange;
	}

	public Float getTotalVatSales() {
		return totalVatSales;
	}
	
	public String getFormattedTotalVatSales() {
		return CurrencyFormatter.pesoFormat(totalVatSales);
	}
	
	public String getFormattedTotalVatableSales() {
		return CurrencyFormatter.pesoFormat(totalVatSales / 1.12);
	}
	
	public String getFormattedTotalVatAmount() {
		return CurrencyFormatter.pesoFormat(totalVatSales - (totalVatSales / 1.12));
	}

	public void setTotalVatSales(Float totalVatSales) {
		this.totalVatSales = totalVatSales;
	}

	public Float getTotalVatExSales() {
		return totalVatExSales;
	}
	
	public String getFormattedTotalVatExSales() {
		return CurrencyFormatter.pesoFormat(totalVatExSales);
	}

	public void setTotalVatExSales(Float totalVatExSales) {
		this.totalVatExSales = totalVatExSales;
	}

	public Float getTotalZeroRatedSales() {
		return totalZeroRatedSales;
	}
	
	public String getFormattedTotalZeroRatedSales() {
		return CurrencyFormatter.pesoFormat(totalZeroRatedSales);
	}

	public void setTotalZeroRatedSales(Float totalZeroRatedSales) {
		this.totalZeroRatedSales = totalZeroRatedSales;
	}
	
	public Float getTotalDiscountAmount() {
		return totalDiscountAmount;
	}
	
	public String getFormattedTotalDiscountAmount() {
		return CurrencyFormatter.pesoFormat(totalDiscountAmount);
	}

	public void setTotalDiscountAmount(Float totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}
	
	public Float getTotalSales() {
		return totalVatSales + totalVatExSales + totalZeroRatedSales - totalDiscountAmount;
	}
	
	public String getFormattedTotalSales() {
		return CurrencyFormatter.pesoFormat(getTotalSales());
	}
	
	public List<CashierSalesSummaryBean> getCashierSalesSummaries() {
		return cashierSalesSummaries;
	}

	public void setCashierSalesSummaries(List<CashierSalesSummaryBean> cashierSalesSummaries) {
		this.cashierSalesSummaries = cashierSalesSummaries;
		this.setTotalVatSales((float) cashierSalesSummaries.stream()
											.mapToDouble(css -> css.getVatSales())
											.sum());
		this.setTotalVatExSales((float) cashierSalesSummaries.stream()
				.mapToDouble(css -> css.getVatExSales())
				.sum());
		this.setTotalZeroRatedSales((float) cashierSalesSummaries.stream()
				.mapToDouble(css -> css.getZeroRatedSales())
				.sum());
		this.setTotalDiscountAmount((float) cashierSalesSummaries.stream()
				.mapToDouble(css -> css.getDiscountAmount())
				.sum());
	}
}
