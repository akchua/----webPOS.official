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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.enums.UnitType;

@Entity(name = "ProductDetail")
@Table(name = ProductDetail.TABLE_NAME)
public class ProductDetail extends BaseObject {
	private static final long serialVersionUID = 4868322850907223089L;

	public static final String TABLE_NAME = "product_detail";
	
	private Product product;
	
	private String title;
	private String barcode;
	private Integer quantity;
	private Float grossPrice;
	private Float discount;
	private Float netPrice;
	private Float percentProfit;
	private Float sellingPrice;
	private Float netProfit;
	private Integer storageStockCount;
	private Integer storeStockCount;
	
	private UnitType unitType;
	
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
	
	@Basic
	@Column(name = "barcode")
	public String getBarcode() {
		return barcode;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	@Basic
	@Column(name = "quantity")
	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Basic
	@Column(name = "gross_price")
	public Float getGrossPrice() {
		return grossPrice;
	}
	
	public void setGrossPrice(Float grossPrice) {
		this.grossPrice = grossPrice;
	}
	
	@Basic
	@Column(name = "discount")
	public Float getDiscount() {
		return discount;
	}
	
	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	
	@Basic
	@Column(name = "net_price")
	public Float getNetPrice() {
		return netPrice;
	}
	
	public void setNetPrice(Float netPrice) {
		this.netPrice = netPrice;
	}
	
	@Basic
	@Column(name = "percent_profit")
	public Float getPercentProfit() {
		return percentProfit;
	}
	
	public void setPercentProfit(Float percentProfit) {
		this.percentProfit = percentProfit;
	}
	
	@Basic
	@Column(name = "selling_price")
	public Float getSellingPrice() {
		return sellingPrice;
	}
	
	public void setSellingPrice(Float sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	
	@Basic
	@Column(name = "net_profit")
	public Float getNetProfit() {
		return netProfit;
	}
	
	public void setNetProfit(Float netProfit) {
		this.netProfit = netProfit;
	}
	
	@Basic
	@Column(name = "storage_stock_count")
	public Integer getStorageStockCount() {
		return storageStockCount;
	}
	
	public void setStorageStockCount(Integer storageStockCount) {
		this.storageStockCount = storageStockCount;
	}
	
	@Basic
	@Column(name = "store_stock_count")
	public Integer getStoreStockCount() {
		return storeStockCount;
	}
	
	public void setStoreStockCount(Integer storeStockCount) {
		this.storeStockCount = storeStockCount;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "unit_type", length = 50)
	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}
}
