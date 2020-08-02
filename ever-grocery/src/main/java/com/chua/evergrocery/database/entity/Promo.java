package com.chua.evergrocery.database.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.enums.PromoType;
import com.chua.evergrocery.serializer.json.ProductSerializer;
import com.chua.evergrocery.serializer.json.UserSerializer;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.chua.evergrocery.utility.format.NumberFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Jul 31, 2020
 */
@Entity(name = "Promo")
@Table(name = Promo.TABLE_NAME)
public class Promo extends BaseObject {

	private static final long serialVersionUID = -2767987428339887046L;
	
	public static final String TABLE_NAME = "promo";
	
	@JsonSerialize(using = UserSerializer.class)
	private User creator;
	
	@JsonSerialize(using = ProductSerializer.class)
	private Product product;
	
	private Date startDate;
	
	private Date endDate;
	
	private Float budget;
	
	private Float usedBudget;
	
	private Float discountPercent;
	
	private PromoType promoType;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "start_date")
	public Date getStartDate() {
		return startDate;
	}
	
	@Transient
	public String getFormattedDuration() {
		return DateFormatter.shortFormat(startDate) + " - " + DateFormatter.shortFormat(endDate);
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "end_date")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Basic
	@Column(name = "budget")
	public Float getBudget() {
		return budget;
	}
	
	@Transient
	public String getFormattedBudget() {
		return usedBudget + " / " + budget;
	}

	public void setBudget(Float budget) {
		this.budget = budget;
	}

	@Basic
	@Column(name = "used_budget")
	public Float getUsedBudget() {
		return usedBudget;
	}
	
	@Transient
	public Float getAvailableBudget() {
		return budget - usedBudget;
	}
	
	@Transient
	public String getFormattedAvailability() {
		final Float percentUsed = (usedBudget / budget) * 100;
		if(percentUsed < 50) return "HIGH";
		else if(percentUsed < 75) return "MEDIUM";
		else if(percentUsed < 100) return "LOW";
		else return "N/A";
	}
	
	@Transient
	public String getAvailabilityColor() {
		switch(getFormattedAvailability()) {
		case "HIGH": 
			return "green";
		case "MEDIUM":
			return "green";
		case "LOW":
			return "orange";
		default :
			return "red";
		}
	}

	public void setUsedBudget(Float usedBudget) {
		this.usedBudget = usedBudget;
	}

	@Basic
	@Column(name = "discount_percent")
	public Float getDiscountPercent() {
		return discountPercent;
	}
	
	@Transient
	public String getFormattedDiscountPercent() {
		return NumberFormatter.decimalFormat(discountPercent, 2);
	}
	
	public void setDiscountPercent(Float discountPercent) {
		this.discountPercent = discountPercent;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "promo_type", length = 50)
	public PromoType getPromoType() {
		return promoType;
	}
	
	public void setPromoType(PromoType promoType) {
		this.promoType = promoType;
	}
	
	@Transient
	public String getFormattedStatus() {
		final Date today = DateUtil.floorDay(new Date());
		if(startDate.after(today)) return "Scheduled";
		else if(endDate.before(today)) return "Completed";
		else return "Active";
	}
}
