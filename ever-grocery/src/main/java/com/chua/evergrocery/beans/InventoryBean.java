package com.chua.evergrocery.beans;

import com.chua.evergrocery.enums.UnitType;

public class InventoryBean {

	private Long productId;
	
	private String productName;
	
	private String productDisplayName;
	
	private Integer wholeQuantity;
	
	private Float pieceQuantity;
	
	private UnitType wholeUnit;
	
	private UnitType pieceUnit;

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

	public String getProductDisplayName() {
		return productDisplayName;
	}

	public void setProductDisplayName(String productDisplayName) {
		this.productDisplayName = productDisplayName;
	}

	public Integer getWholeQuantity() {
		return wholeQuantity;
	}

	public void setWholeQuantity(Integer wholeQuantity) {
		this.wholeQuantity = wholeQuantity;
	}

	public Float getPieceQuantity() {
		return pieceQuantity;
	}

	public void setPieceQuantity(Float pieceQuantity) {
		this.pieceQuantity = pieceQuantity;
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
