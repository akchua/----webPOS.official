package com.chua.evergrocery.beans;

import com.chua.evergrocery.enums.UnitType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 25, 2019
 */
public class GeneratedOfftakeBean {
	
	private Long productId;

	private String productName;
	
	private String categoryName;
	
	private String productDisplayName;
	
	private UnitType productWholeUnit;
	
	private Float offtake;

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
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getProductDisplayName() {
		return productDisplayName;
	}

	public void setProductDisplayName(String productDisplayName) {
		this.productDisplayName = productDisplayName;
	}

	public UnitType getProductWholeUnit() {
		return productWholeUnit;
	}

	public void setProductWholeUnit(UnitType productWholeUnit) {
		this.productWholeUnit = productWholeUnit;
	}

	public Float getOfftake() {
		return offtake;
	}
	
	public Integer getSuggestedOrder() {
		return (int) Math.ceil(offtake * 1.20);
	}

	public void setOfftake(Float offtake) {
		this.offtake = offtake;
	}
}
