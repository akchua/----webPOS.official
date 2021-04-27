package com.chua.evergrocery.database.entity;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.enums.TaxType;
import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.serializer.json.BrandSerializer;
import com.chua.evergrocery.serializer.json.CategorySerializer;
import com.chua.evergrocery.serializer.json.CompanySerializer;
import com.chua.evergrocery.serializer.json.ProductDetailSerializer;
import com.chua.evergrocery.utility.format.NumberFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name = "Product")
@Table(name = Product.TABLE_NAME)
public class Product extends BaseObject {
	private static final long serialVersionUID = -4829246496849289618L;
	
	public static final String TABLE_NAME = "product";
	
	@JsonSerialize(using = BrandSerializer.class)
	private Brand brand;
	
	@JsonSerialize(using = CategorySerializer.class)
	private Category category;
	
	@JsonSerialize(using = CompanySerializer.class)
	private Company company;
	
	@JsonSerialize(using = ProductDetailSerializer.class)
	private List<ProductDetail> productDetails;
	
	private String name;
	
	private String displayName;
	
	private String code;
	
	private TaxType taxType;
	
	private Boolean promo;
	
	private Boolean allowSeniorDiscount;
	
	private Boolean allowPWDDiscount;
	
	private Float saleRate;
	
	private Double purchaseBudget;
	
	private Double totalBudget;
	
	// Percentage in respective company only (previous month only) (net amount is used)
	private Float purchaseValuePercentage;
	
	private Float saleValuePercentage;
	
	private Float profitPercentage;
	
	// Percentage in respective category only (previous month only) (net amount is used)
	private Float categoryProfitPercentage;
	
	// Monthly profit ranking
	private Integer currentProfitRank;
	
	private Integer previousProfitRank;
	
	// Month to day average offtake
	private Float mtdOfftake;
	
	private UnitType mtdOfftakeUnit;
	
	@ManyToOne(targetEntity = Brand.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Brand getBrand() {
		return brand;
	}
	
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	@ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@ManyToOne(targetEntity = Company.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Company getCompany() {
		return company;
	}
	
	public void setCompany(Company company) {
		this.company = company;
	}
	
	@Transient
	@OneToMany(fetch = FetchType.LAZY, targetEntity = ProductDetail.class, mappedBy = "product")
	@Fetch(value = FetchMode.SELECT)
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	public List<ProductDetail> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}

	@Basic
	@Column(name = "name") // length = 255
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "display_name")
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Basic
	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "tax_type", length = 50)
	public TaxType getTaxType() {
		return taxType;
	}

	public void setTaxType(TaxType taxType) {
		this.taxType = taxType;
	}

	@Basic
	@Column(name = "promo")
	public Boolean getPromo() {
		return promo;
	}

	public void setPromo(Boolean promo) {
		this.promo = promo;
	}

	@Basic
	@Column(name = "allow_senior_discount")
	public Boolean getAllowSeniorDiscount() {
		return allowSeniorDiscount;
	}

	public void setAllowSeniorDiscount(Boolean allowSeniorDiscount) {
		this.allowSeniorDiscount = allowSeniorDiscount;
	}

	@Basic
	@Column(name = "allow_pwd_discount")
	public Boolean getAllowPWDDiscount() {
		return allowPWDDiscount;
	}

	public void setAllowPWDDiscount(Boolean allowPWDDiscount) {
		this.allowPWDDiscount = allowPWDDiscount;
	}

	@Basic
	@Column(name = "sale_rate")
	public Float getSaleRate() {
		return saleRate;
	}
	
	public void setSaleRate(Float saleRate) {
		this.saleRate = saleRate;
	}

	@Basic
	@Column(name = "purchase_budget")
	public Double getPurchaseBudget() {
		return purchaseBudget;
	}
	
	public void setPurchaseBudget(Double purchaseBudget) {
		this.purchaseBudget = purchaseBudget;
	}

	@Basic
	@Column(name = "total_budget")
	public Double getTotalBudget() {
		return totalBudget;
	}
	
	@Transient
	public Double getStockBudget() {
		return totalBudget - purchaseBudget;
	}

	public void setTotalBudget(Double totalBudget) {
		this.totalBudget = totalBudget;
	}

	@Basic
	@Column(name = "purchase_value_percentage")
	public Float getPurchaseValuePercentage() {
		return purchaseValuePercentage;
	}
	
	@Transient 
	public String getFormattedPurchaseValuePercentage() {
		return NumberFormatter.toPercent(purchaseValuePercentage);
	}
	
	@Transient
	public Float getOverallPurchaseValuePercentage() {
		return purchaseValuePercentage * company.getPurchaseValuePercentage() / 100.0f;
	}
	
	@Transient 
	public String getFormattedOverallPurchaseValuePercentage() {
		return NumberFormatter.toPercent(getOverallPurchaseValuePercentage());
	}

	public void setPurchaseValuePercentage(Float purchaseValuePercentage) {
		this.purchaseValuePercentage = purchaseValuePercentage;
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
	
	@Transient
	public Float getOverallSaleValuePercentage() {
		return saleValuePercentage * company.getSaleValuePercentage() / 100.0f;
	}
	
	@Transient 
	public String getFormattedOverallSaleValuePercentage() {
		return NumberFormatter.toPercent(getOverallSaleValuePercentage());
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
	
	@Transient
	public Float getOverallProfitPercentage() {
		return profitPercentage * company.getProfitPercentage() / 100.0f;
	}
	
	@Transient 
	public String getFormattedOverallProfitPercentage() {
		return NumberFormatter.toPercent(getOverallProfitPercentage());
	}

	public void setProfitPercentage(Float profitPercentage) {
		this.profitPercentage = profitPercentage;
	}
	
	@Basic
	@Column(name = "category_profit_percentage")
	public Float getCategoryProfitPercentage() {
		return categoryProfitPercentage;
	}
	
	@Transient 
	public String getFormattedCategoryProfitPercentage() {
		return NumberFormatter.toPercent(categoryProfitPercentage);
	}

	public void setCategoryProfitPercentage(Float categoryProfitPercentage) {
		this.categoryProfitPercentage = categoryProfitPercentage;
	}

	@Basic
	@Column(name = "current_profit_rank")
	public Integer getCurrentProfitRank() {
		return currentProfitRank;
	}
	
	@Transient
	public Integer getRankDeviation() {
		if(previousProfitRank == 0) return 0;
		else return Math.abs(this.currentProfitRank - this.previousProfitRank);
	}
	
	@Transient
	public Boolean isRankIncrease() {
		return previousProfitRank == 0 ? Boolean.FALSE : currentProfitRank < previousProfitRank;
	}
	
	@Transient
	public Boolean isRankDecrease() {
		return previousProfitRank == 0 ? Boolean.FALSE : currentProfitRank > previousProfitRank;
	}
	
	@Transient
	public Boolean isRankSame() {
		return getRankDeviation() == 0;
	}

	public void setCurrentProfitRank(Integer currentProfitRank) {
		this.currentProfitRank = currentProfitRank;
	}

	@Basic
	@Column(name = "previous_profit_rank")
	public Integer getPreviousProfitRank() {
		return previousProfitRank;
	}

	public void setPreviousProfitRank(Integer previousProfitRank) {
		this.previousProfitRank = previousProfitRank;
	}

	@Basic
	@Column(name = "mtd_offtake")
	public Float getMtdOfftake() {
		return mtdOfftake;
	}
	
	@Transient
	public String getFormattedMtdOfftake() {
		return NumberFormatter.decimalFormat(mtdOfftake, 2);
	}

	public void setMtdOfftake(Float mtdOfftake) {
		this.mtdOfftake = mtdOfftake;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "mtd_offtake_unit", length = 50)
	public UnitType getMtdOfftakeUnit() {
		return mtdOfftakeUnit;
	}

	public void setMtdOfftakeUnit(UnitType mtdOfftakeUnit) {
		this.mtdOfftakeUnit = mtdOfftakeUnit;
	}
}
