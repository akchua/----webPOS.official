package com.chua.evergrocery.beans;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Nov 11, 2020
 */
public class PromoBean {

	private String productName;
	
	private Float discountPercent;
	
	private Float caseSellingPrice;
	
	private Float pieceSellingPrice;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(Float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public Float getCaseSellingPrice() {
		return caseSellingPrice;
	}

	public void setCaseSellingPrice(Float caseSellingPrice) {
		this.caseSellingPrice = caseSellingPrice;
	}

	public Float getPieceSellingPrice() {
		return pieceSellingPrice;
	}

	public void setPieceSellingPrice(Float pieceSellingPrice) {
		this.pieceSellingPrice = pieceSellingPrice;
	}
}
