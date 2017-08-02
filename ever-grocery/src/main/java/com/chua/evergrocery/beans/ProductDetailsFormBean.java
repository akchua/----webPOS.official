package com.chua.evergrocery.beans;

import com.chua.evergrocery.enums.UnitType;

public class ProductDetailsFormBean extends FormBean {

	private String title;
	
	private String barcode;
	
	private Integer quantity;
	
	private UnitType unitType;
	
	private Float grossPrice;
	
	private Float discount;
	
	private Float netPrice;
	
	private Float percentProfit;
	
	private Float sellingPrice;
	
	private Float netProfit;
	
	private Integer storageStockCount;
	
	private Integer storeStockCount;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	public Float getGrossPrice() {
		return grossPrice;
	}

	public void setGrossPrice(Float grossPrice) {
		this.grossPrice = grossPrice;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Float getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(Float netPrice) {
		this.netPrice = netPrice;
	}

	public Float getPercentProfit() {
		return percentProfit;
	}

	public void setPercentProfit(Float percentProfit) {
		this.percentProfit = percentProfit;
	}

	public Float getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Float sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Float getNetProfit() {
		return netProfit;
	}

	public void setNetProfit(Float netProfit) {
		this.netProfit = netProfit;
	}

	public Integer getStorageStockCount() {
		return storageStockCount;
	}

	public void setStorageStockCount(Integer storageStockCount) {
		this.storageStockCount = storageStockCount;
	}

	public Integer getStoreStockCount() {
		return storeStockCount;
	}

	public void setStoreStockCount(Integer storeStockCount) {
		this.storeStockCount = storeStockCount;
	}
}
