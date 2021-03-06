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
import com.chua.evergrocery.enums.PromoType;
import com.chua.evergrocery.enums.TaxType;
import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.utility.format.CurrencyFormatter;

@Entity(name = "CustomerOrderDetail")
@Table(name = CustomerOrderDetail.TABLE_NAME)
public class CustomerOrderDetail extends BaseObject {

	private static final long serialVersionUID = 6024149457706035805L;
	
	public static final String TABLE_NAME = "customer_order_detail";
	
	private CustomerOrder customerOrder;
	
	private ProductDetail productDetail;
	
	private Product product;
	
	private String productName;
	
	private String productDisplayName;
	
	private String productCode;
	
	private UnitType unitType;
	
	private Integer content;
	
	private UnitType contentUnit;
	
	private Float upgradedQuantity;
	
	private Float unitPrice;
	
	private Float quantity;
	
	private Float totalPrice;
	
	private Long promoId;
	
	private Float promoPercentDiscount;
	
	private PromoType promoType;
	
	private Float margin;
	
	private Float purchasePrice;
	
	private TaxType taxType;
	
	private Float taxAdjustment;
	
	private TaxType origTaxType;
	
	@ManyToOne(targetEntity = CustomerOrder.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_order_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public CustomerOrder getCustomerOrder() {
		return customerOrder;
	}

	public void setCustomerOrder(CustomerOrder customerOrder) {
		this.customerOrder = customerOrder;
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
	
	@Transient
	public String getFormattedItemName() {
		final String formattedContent = getFormattedContent();
		return productName + (!formattedContent.equals("-") ? " (" + formattedContent + ")" : "");
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Basic
	@Column(name = "product_display_name")
	public String getProductDisplayName() {
		return productDisplayName;
	}
	
	@Transient
	public String getFormattedDisplayName() {
		return (productDisplayName != null && !productDisplayName.isEmpty()) ? productDisplayName : productName;
	}

	public void setProductDisplayName(String productDisplayName) {
		this.productDisplayName = productDisplayName;
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
	@Column(name = "content")
	public Integer getContent() {
		return content;
	}
	
	@Transient
	public String getFormattedContent() {
		return (content != null && content != 0)
				? content + " " + (contentUnit != null ? contentUnit.getDisplayName() : "") 
				: "-";
	}

	public void setContent(Integer content) {
		this.content = content;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "content_unit", length = 50)
	public UnitType getContentUnit() {
		return contentUnit;
	}

	public void setContentUnit(UnitType contentUnit) {
		this.contentUnit = contentUnit;
	}

	@Basic
	@Column(name = "upgraded_quantity")
	public Float getUpgradedQuantity() {
		return upgradedQuantity;
	}

	public void setUpgradedQuantity(Float upgradedQuantity) {
		this.upgradedQuantity = upgradedQuantity;
	}

	@Basic
	@Column(name = "unit_price")
	public Float getUnitPrice() {
		return unitPrice;
	}
	
	@Transient
	public String getFormattedUnitPrice() {
		return CurrencyFormatter.pesoFormat(getUnitPrice());
	}

	public void setUnitPrice(Float unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Basic
	@Column(name = "quantity")
	public Float getQuantity() {
		return quantity;
	}

	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}

	@Basic
	@Column(name = "total_price")
	public Float getTotalPrice() {
		return totalPrice;
	}
	
	@Transient
	public String getFormattedTotalPrice() {
		return CurrencyFormatter.pesoFormat(getTotalPrice()) + taxType.getSymbol();
	}
	
	@Transient
	public String getFormattedBeforeTaxAdjustmentPrice() {
		return CurrencyFormatter.pesoFormat(getTotalPrice() - getTaxAdjustment()) + origTaxType.getSymbol();
	}

	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Basic
	@Column(name = "promo_id")
	public Long getPromoId() {
		return promoId;
	}
	
	@Transient
	public String getFormattedPromo() {
		return promoId.equals(0l) ? "x" : "$";
	}

	public void setPromoId(Long promoId) {
		this.promoId = promoId;
	}

	@Basic
	@Column(name = "promo_percent_discount")
	public Float getPromoPercentDiscount() {
		return promoPercentDiscount;
	}
	
	@Transient
	public Float getPromoDiscountAmount() {
		return totalPrice * (promoPercentDiscount / 100);
	}

	public void setPromoPercentDiscount(Float promoPercentDiscount) {
		this.promoPercentDiscount = promoPercentDiscount;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "promo_type", length = 50)
	public PromoType getPromoType() {
		return promoType;
	}

	public void setPromoType(PromoType promoType) {
		this.promoType = promoType;
	}

	@Basic
	@Column(name = "margin")
	public Float getMargin() {
		return margin;
	}

	public void setMargin(Float margin) {
		this.margin = margin;
	}

	@Basic
	@Column(name = "purchase_price")
	public Float getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Float purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "tax_type")
	public TaxType getTaxType() {
		return taxType;
	}

	public void setTaxType(TaxType taxType) {
		this.taxType = taxType;
	}

	@Basic
	@Column(name = "tax_adjustment")
	public Float getTaxAdjustment() {
		return taxAdjustment;
	}

	public void setTaxAdjustment(Float taxAdjustment) {
		this.taxAdjustment = taxAdjustment;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "orig_tax_type")
	public TaxType getOrigTaxType() {
		return origTaxType;
	}

	public void setOrigTaxType(TaxType origTaxType) {
		this.origTaxType = origTaxType;
	}
}
