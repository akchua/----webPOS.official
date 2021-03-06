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
import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.utility.format.CurrencyFormatter;

@Entity(name = "PurchaseOrderDetail")
@Table(name = PurchaseOrderDetail.TABLE_NAME)
public class PurchaseOrderDetail extends BaseObject {

	private static final long serialVersionUID = 6191402895309255478L;

	public static final String TABLE_NAME = "purchase_order_detail";
	
	private PurchaseOrder purchaseOrder;
	
	private ProductDetail productDetail;
	
	private Product product;
	
	private String productName;
	
	private String productCode;
	
	private UnitType unitType;
	
	private Float grossPrice;
	
	private Float netPrice;
	
	private Integer quantity;
	
	private Double totalPrice;

	@ManyToOne(targetEntity = PurchaseOrder.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_order_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	@ManyToOne(targetEntity = ProductDetail.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_detail_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public ProductDetail getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(ProductDetail productDetail) {
		this.productDetail = productDetail;
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

	@Basic
	@Column(name = "product_name")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Basic
	@Column(name = "product_code")
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "unit_type", length = 50)
	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	@Basic
	@Column(name = "gross_price")
	public Float getGrossPrice() {
		return grossPrice;
	}
	
	@Transient
	public String getFormattedGrossPrice() {
		return CurrencyFormatter.pesoFormat(getGrossPrice());
	}
	
	@Transient
	public String getFormattedBeforeVatAndDiscountPrice() {
		return CurrencyFormatter.pesoFormat((getGrossPrice() * 100 / 1.12) / 100);
	}
	
	@Transient
	public String getFormattedBeforeVatAfterDiscountPrice() {
		return CurrencyFormatter.pesoFormat((getGrossPrice() * 100 / 1.12) / 100);
	}

	public void setGrossPrice(Float grossPrice) {
		this.grossPrice = grossPrice;
	}

	@Basic
	@Column(name = "net_price")
	public Float getNetPrice() {
		return netPrice;
	}
	
	@Transient
	public String getFormattedNetPrice() {
		return CurrencyFormatter.pesoFormat(getNetPrice());
	}


	public void setNetPrice(Float netPrice) {
		this.netPrice = netPrice;
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
	@Column(name = "total_price")
	public Double getTotalPrice() {
		return totalPrice;
	}
	
	@Transient
	public String getFormattedTotalPrice() {
		return CurrencyFormatter.pesoFormat(getTotalPrice());
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
