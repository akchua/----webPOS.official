package com.chua.evergrocery.beans;

import com.chua.evergrocery.enums.TaxType;

public class ProductFormBean extends FormBean {

	private String name;
	
	private String displayName;
	
	private String code;
	
	/*private Long brandId;*/
	
	private Long categoryId;
	
	private Long companyId;
	
	private TaxType taxType;
	
	private Boolean allowSeniorDiscount;
	
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
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	/*public Long getBrandId() {
		return brandId;
	}
	
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}*/
	
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
	
	public TaxType getTaxType() {
		return taxType;
	}

	public void setTaxType(TaxType taxType) {
		this.taxType = taxType;
	}

	public Boolean getAllowSeniorDiscount() {
		return allowSeniorDiscount;
	}

	public void setAllowSeniorDiscount(Boolean allowSeniorDiscount) {
		this.allowSeniorDiscount = allowSeniorDiscount;
	}
}
