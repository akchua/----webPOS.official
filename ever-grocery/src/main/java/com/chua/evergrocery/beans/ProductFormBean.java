package com.chua.evergrocery.beans;

public class ProductFormBean extends FormBean {

	private String name;
	private String displayName;
	
	private Long brandId;
	private Long categoryId;
	private Long companyId;
	private Long distributorId;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public Long getBrandId() {
		return brandId;
	}
	
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	
	public Long getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	public Long getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public Long getDistributorId() {
		return distributorId;
	}
	
	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}
}
