package com.chua.evergrocery.beans;

import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.NumberFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 22, 2018
 */
public class ProductStatisticsBean {

	private Long productId;
	
	private String productName;
	
	private Integer quantity;
	
	private UnitType unit;
	
	private Float sales;
	
	private Float currentPurchaseBudget;
	
	private Float previousSaleRate;
	
	private Float currentSaleRate;
	
	private Float previousTotalBudget;
	
	private Float currentTotalBudget;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public UnitType getUnit() {
		return unit;
	}

	public void setUnit(UnitType unit) {
		this.unit = unit;
	}

	public Float getSales() {
		return sales;
	}
	
	public String getFormattedSales() {
		return CurrencyFormatter.pesoFormat(getSales());
	}

	public void setSales(Float sales) {
		this.sales = sales;
	}

	public Float getCurrentPurchaseBudget() {
		return currentPurchaseBudget;
	}

	public void setCurrentPurchaseBudget(Float currentPurchaseBudget) {
		this.currentPurchaseBudget = currentPurchaseBudget;
	}

	public Float getPreviousSaleRate() {
		return previousSaleRate;
	}
	
	public String getFormattedPreviousSaleRate() {
		return NumberFormatter.toPercent(getPreviousSaleRate());
	}

	public void setPreviousSaleRate(Float previousSaleRate) {
		this.previousSaleRate = previousSaleRate;
	}

	public Float getCurrentSaleRate() {
		return currentSaleRate;
	}
	
	public String getFormattedCurrentSaleRate() {
		return NumberFormatter.toPercent(getCurrentSaleRate());
	}

	public void setCurrentSaleRate(Float currentSaleRate) {
		this.currentSaleRate = currentSaleRate;
	}

	public Float getPreviousTotalBudget() {
		return previousTotalBudget;
	}
	
	public String getFormattedPreviousTotalBudget() {
		return CurrencyFormatter.pesoFormat(getPreviousTotalBudget());
	}

	public void setPreviousTotalBudget(Float previousTotalBudget) {
		this.previousTotalBudget = previousTotalBudget;
	}

	public Float getCurrentTotalBudget() {
		return currentTotalBudget;
	}
	
	public String getFormattedCurrentTotalBudget() {
		return CurrencyFormatter.pesoFormat(getCurrentTotalBudget());
	}

	public void setCurrentTotalBudget(Float currentTotalBudget) {
		this.currentTotalBudget = currentTotalBudget;
	}
}
