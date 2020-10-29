package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.utility.format.NumberFormatter;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
@Entity(name = "CustomerCategory")
@Table(name = CustomerCategory.TABLE_NAME)
public class CustomerCategory extends BaseObject {

	private static final long serialVersionUID = -108349122989290050L;
	
	public static final String TABLE_NAME = "customer_category";
	
	private String name;
	
	private Float saleValuePercentage;
	
	private Float profitPercentage;

	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Basic
	@Column(name = "sale_value_percentage")
	public Float getSaleValuePercentage() {
		return saleValuePercentage;
	}
	
	@Transient 
	public String getFormattedSaleValuePercentage() {
		return NumberFormatter.toPercent(saleValuePercentage);
	}

	public void setSaleValuePercentage(Float saleValuePercentage) {
		this.saleValuePercentage = saleValuePercentage;
	}
	
	@Basic
	@Column(name = "profit_percentage")
	public Float getProfitPercentage() {
		return profitPercentage;
	}
	
	@Transient 
	public String getFormattedProfitPercentage() {
		return NumberFormatter.toPercent(profitPercentage);
	}

	public void setProfitPercentage(Float profitPercentage) {
		this.profitPercentage = profitPercentage;
	}
}
