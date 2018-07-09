package com.chua.evergrocery.beans;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.chua.evergrocery.utility.format.CurrencyFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   8 Jul 2018
 */
public class DailySalesBean {

	private Date saleDate;
	
	private Long sinStart;
	
	private Long sinEnd;
	
	private Float totalVatSales;
	
	private Float totalVatExSales;
	
	private Float totalZeroRatedSales;
	
	List<DailySalesBreakdownBean> dailySalesBreakdowns;

	public Date getSaleDate() {
		return saleDate;
	}
	
	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public Long getSinStart() {
		return sinStart;
	}
	
	public String getFormattedSinStart() {
		DecimalFormat SIN_FORMAT = new DecimalFormat("00000000");
		return SIN_FORMAT.format(sinStart);
	}

	public void setSinStart(Long sinStart) {
		this.sinStart = sinStart;
	}

	public Long getSinEnd() {
		return sinEnd;
	}
	
	public String getFormattedSinEnd() {
		DecimalFormat SIN_FORMAT = new DecimalFormat("00000000");
		return SIN_FORMAT.format(sinEnd);
	}

	public void setSinEnd(Long sinEnd) {
		this.sinEnd = sinEnd;
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
	
	public Float getTotalSales() {
		return totalVatSales + totalVatExSales + totalZeroRatedSales;
	}
	
	public String getFormattedTotalSales() {
		return CurrencyFormatter.pesoFormat(getTotalSales());
	}

	public List<DailySalesBreakdownBean> getDailySalesBreakdowns() {
		return dailySalesBreakdowns;
	}

	public void setDailySalesBreakdowns(List<DailySalesBreakdownBean> dailySalesBreakdowns) {
		this.dailySalesBreakdowns = dailySalesBreakdowns;
	}
}
