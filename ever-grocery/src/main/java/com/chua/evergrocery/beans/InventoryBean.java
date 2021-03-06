package com.chua.evergrocery.beans;

import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.utility.format.NumberFormatter;

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
	
	private Integer wholeContent;
	
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
		return piecePurchasePrice.equals(0.0f) ? 0.0f : (float) ((stockBudget % wholePurchasePrice) / piecePurchasePrice);
	}
	
	public Float getPurchasedPieceQuantity() {
		return piecePurchasePrice.equals(0.0f) ? 0.0f : (float) ((totalNetPurchase % wholePurchasePrice) / piecePurchasePrice);
	}
	
	public Float getSoldPieceQuantity() {
		return piecePurchasePrice.equals(0.0f) ? 0.0f : (float) ((totalBaseSales % wholePurchasePrice) / piecePurchasePrice);
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

	public Integer getWholeContent() {
		return wholeContent;
	}

	public void setWholeContent(Integer wholeContent) {
		this.wholeContent = wholeContent;
	}
	
	public String getFormattedInventory() {
		String formattedInventory = "";
		
		if(this.getWholeQuantity() != 0) {
			formattedInventory += this.getWholeQuantity() + this.getWholeUnit().getShorthand();
			if(this.getPieceQuantity() != 0.0f) {
				formattedInventory += " & " + NumberFormatter.decimalFormat(this.getPieceQuantity(), 2) + this.getPieceUnit().getShorthand();
			}
		} else if(this.getPieceQuantity() != 0.0f) {
			formattedInventory += NumberFormatter.decimalFormat(this.getPieceQuantity(), 2) + this.getPieceUnit().getShorthand();
		} else {
			formattedInventory += "=";
		}
		
		return formattedInventory;
	}
	
	public String getFormattedSoldCount() {
		String formattedSold = "";
		
		if(this.getSoldWholeQuantity() != 0) {
			formattedSold += this.getSoldWholeQuantity() + this.getWholeUnit().getShorthand();
			if(this.getSoldPieceQuantity() != 0.0f) {
				formattedSold += " & " + NumberFormatter.decimalFormat(this.getSoldPieceQuantity(), 2) + this.getPieceUnit().getShorthand();
			}
		} else if(this.getSoldPieceQuantity() != 0.0f) {
			formattedSold += NumberFormatter.decimalFormat(this.getSoldPieceQuantity(), 2) + this.getPieceUnit().getShorthand();
		} else {
			formattedSold += "=";
		}
		
		return formattedSold;
	}
}
