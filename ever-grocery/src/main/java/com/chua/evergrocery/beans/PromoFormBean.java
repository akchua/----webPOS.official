package com.chua.evergrocery.beans;

import java.util.Date;

import com.chua.evergrocery.deserializer.json.DateDeserializer;
import com.chua.evergrocery.enums.PromoType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Aug 1, 2020
 */
public class PromoFormBean extends FormBean {

	private Long productId;
	
	@JsonDeserialize(using = DateDeserializer.class)
	private Date startDate;
	
	@JsonDeserialize(using = DateDeserializer.class)
	private Date endDate;
	
	private Float budget;
	
	private Float usedBudget;
	
	private Float discountPercent;
	
	private PromoType promoType;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Float getBudget() {
		return budget;
	}

	public void setBudget(Float budget) {
		this.budget = budget;
	}

	public Float getUsedBudget() {
		return usedBudget;
	}

	public void setUsedBudget(Float usedBudget) {
		this.usedBudget = usedBudget;
	}

	public Float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(Float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public PromoType getPromoType() {
		return promoType;
	}

	public void setPromoType(PromoType promoType) {
		this.promoType = promoType;
	}
}
