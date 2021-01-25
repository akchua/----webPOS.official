package com.chua.evergrocery.beans;

import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.enums.UnitType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 6, 2018
 */
public class InventoryBean {

	private Product product;
	
	private Double totalNetPurchase;
	
	private Double totalBaseSales;
	
	private Double stockBudget;
	
	private Float wholePurchasePrice;
	
	private Float piecePurchasePrice;
	
	private UnitType wholeUnit;
	
	private UnitType pieceUnit;
	
	public InventoryBean() {
		this.totalNetPurchase = 0.0d;
		this.totalBaseSales = 0.0d;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getTotalNetPurchase() {
		return totalNetPurchase;
	}

	public void setTotalNetPurchase(Double totalNetPurchase) {
		this.totalNetPurchase = totalNetPurchase;
	}

	public Double getTotalBaseSales() {
		return totalBaseSales;
	}

	public void setTotalBaseSales(Double totalBaseSales) {
		this.totalBaseSales = totalBaseSales;
	}

	public Double getStockBudget() {
		return stockBudget;
	}

	public void setStockBudget(Double stockBudget) {
		this.stockBudget = stockBudget;
	}

	public Float getWholePurchasePrice() {
		return wholePurchasePrice;
	}
	
	public Integer getWholeQuantity() {
		return (int) (stockBudget / wholePurchasePrice);
	}
	
	public Integer getPurchasedWholeQuantity() {
		return (int) (totalNetPurchase / wholePurchasePrice);
	}
	
	public Integer getSoldWholeQuantity() {
		return (int) (totalBaseSales / wholePurchasePrice);
	}

	public void setWholePurchasePrice(Float wholePurchasePrice) {
		this.wholePurchasePrice = wholePurchasePrice;
	}

	public Float getPiecePurchasePrice() {
		return piecePurchasePrice;
	}
	
	public Float getPieceQuantity() {
		return (float) ((stockBudget % wholePurchasePrice) / piecePurchasePrice);
	}
	
	public Float getPurchasedPieceQuantity() {
		return (float) ((totalNetPurchase % wholePurchasePrice) / piecePurchasePrice);
	}
	
	public Float getSoldPieceQuantity() {
		return (float) ((totalBaseSales % wholePurchasePrice) / piecePurchasePrice);
	}

	public void setPiecePurchasePrice(Float piecePurchasePrice) {
		this.piecePurchasePrice = piecePurchasePrice;
	}

	public UnitType getWholeUnit() {
		return wholeUnit;
	}

	public void setWholeUnit(UnitType wholeUnit) {
		this.wholeUnit = wholeUnit;
	}

	public UnitType getPieceUnit() {
		return pieceUnit;
	}

	public void setPieceUnit(UnitType pieceUnit) {
		this.pieceUnit = pieceUnit;
	}
}
