package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.enums.PriceHistoryType;
import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.serializer.json.ProductSerializer;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 10, 2018
 */
@Entity(name = "PriceHistory")
@Table(name = PriceHistory.TABLE_NAME)
public class PriceHistory extends BaseObject {

	private static final long serialVersionUID = 3945773857900822094L;
	
	public static final String TABLE_NAME = "price_history";
	
	@JsonSerialize(using = ProductSerializer.class)
	private Product product;
	
	private String title;
	
	private UnitType unitType;
	
	private PriceHistoryType priceHistoryType;
	
	private Float oldPrice;
	
	private Float newPrice;

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

	@Basic
	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "unit_type", length = 50)
	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "price_history_type", length = 50)
	public PriceHistoryType getPriceHistoryType() {
		return priceHistoryType;
	}

	public void setPriceHistoryType(PriceHistoryType priceHistoryType) {
		this.priceHistoryType = priceHistoryType;
	}

	@Basic
	@Column(name = "old_price")
	public Float getOldPrice() {
		return oldPrice;
	}
	
	@Transient
	public String getFormattedOldPrice() {
		if(oldPrice.equals(0.0f)) {
			return "NEW";
		} else {
			return CurrencyFormatter.pesoFormat(oldPrice);
		}
		
	}

	public void setOldPrice(Float oldPrice) {
		this.oldPrice = oldPrice;
	}

	@Basic
	@Column(name = "new_price")
	public Float getNewPrice() {
		return newPrice;
	}
	
	@Transient
	public String getFormattedNewPrice() {
		return CurrencyFormatter.pesoFormat(newPrice);
	}
	
	@Transient
	public Boolean isIncrease() {
		return newPrice > oldPrice;
	}

	public void setNewPrice(Float newPrice) {
		this.newPrice = newPrice;
	}
}
