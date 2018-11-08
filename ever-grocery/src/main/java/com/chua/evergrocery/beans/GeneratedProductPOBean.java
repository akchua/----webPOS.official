package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 8, 2018
 */
public class GeneratedProductPOBean {

	private Long productId;
	
	private String productName;
	
	private String productCode;
	
	private InventoryBean toPurchase;

	private InventoryBean inventory;
	
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

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public InventoryBean getToPurchase() {
		return toPurchase;
	}

	public void setToPurchase(InventoryBean toPurchase) {
		this.toPurchase = toPurchase;
	}

	public InventoryBean getInventory() {
		return inventory;
	}

	public void setInventory(InventoryBean inventory) {
		this.inventory = inventory;
	}
}
