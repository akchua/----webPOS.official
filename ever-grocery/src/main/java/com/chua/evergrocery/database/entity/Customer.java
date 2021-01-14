package com.chua.evergrocery.database.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.chua.evergrocery.serializer.json.CustomerCategorySerializer;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.chua.evergrocery.utility.format.NumberFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name = "Customer")
@Table(name = Customer.TABLE_NAME)
public class Customer extends BaseObject {

	private static final long serialVersionUID = -1975395949715167723L;
	
	public static final String TABLE_NAME = "customer";
	
	@JsonSerialize(using = CustomerCategorySerializer.class)
	private CustomerCategory customerCategory;
	
	private String name;
	
	private String storeName;
	
	private String code;
	
	private String contactNumber;
	
	private String address;
	
	private String cardId;
	
	private Float totalPoints;
	
	private Float usedPoints;
	
	private Date lastPurchase;
	
	private Float saleValuePercentage;
	
	private Float profitPercentage;
	
	// Monthly profit ranking
	private Integer currentProfitRank;
	
	private Integer previousProfitRank;
	
	// Out of Schedule
	private Float averageSchedule;
	
	private Boolean oosFlag;
	
	private Date oosLastFlag;

	@ManyToOne(targetEntity = CustomerCategory.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_category_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public CustomerCategory getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(CustomerCategory customerCategory) {
		this.customerCategory = customerCategory;
	}

	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	@Transient
	public String getFormattedName() {
		final String formattedName;
		
		if(storeName == null || storeName.isEmpty()) {
			formattedName = name;
		} else {
			formattedName = storeName;
		}
		
		return formattedName;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "store_name")
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@Basic
	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Basic
	@Column(name = "contact_number")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Basic
	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Basic
	@Column(name = "card_id")
	public String getCardId() {
		return cardId;
	}
	
	@Transient
	public String getFormattedCardId() {
		return cardId.isEmpty() ? "-" : "*********" + getCardId().substring(9);
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	@Basic
	@Column(name = "total_points")
	public Float getTotalPoints() {
		return totalPoints;
	}
	
	@Transient
	public String getFormattedTotalPoints() {
		return CurrencyFormatter.pesoFormat(getTotalPoints());
	}

	public void setTotalPoints(Float totalPoints) {
		this.totalPoints = totalPoints;
	}

	@Basic
	@Column(name = "used_points")
	public Float getUsedPoints() {
		return usedPoints;
	}
	
	@Transient
	public String getFormattedUsedPoints() {
		return CurrencyFormatter.pesoFormat(getUsedPoints());
	}
	
	@Transient
	public Float getAvailablePoints() {
		return getTotalPoints() - getUsedPoints();
	}
	
	@Transient
	public String getFormattedAvailablePoints() {
		return CurrencyFormatter.pesoFormat(getTotalPoints() - getUsedPoints());
	}

	public void setUsedPoints(Float usedPoints) {
		this.usedPoints = usedPoints;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_purchase", nullable = false)
	public Date getLastPurchase() {
		return lastPurchase;
	}
	
	@Transient
	public String getFormattedLastPurchase() {
		return DateUtil.getDefaultDate().equals(lastPurchase) ? "n/a" : DateFormatter.prettyFormat(lastPurchase);
	}
	
	@Transient
	public String getFormattedLastPurchaseDays() {
		return DateUtil.daysBetween(lastPurchase, new Date()) + " days ago";
	}

	public void setLastPurchase(Date lastPurchase) {
		this.lastPurchase = lastPurchase;
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
	public Float getCategoryOnlySaleValuePercentage() {
		return saleValuePercentage / customerCategory.getSaleValuePercentage() * 100;
	}
	
	@Transient 
	public String getFormattedCategoryOnlySaleValuePercentage() {
		return NumberFormatter.toPercent(getCategoryOnlySaleValuePercentage());
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
	public Float getCategoryOnlyProfitPercentage() {
		return profitPercentage / customerCategory.getProfitPercentage() * 100;
	}
	
	@Transient 
	public String getFormattedCategoryOnlyProfitPercentage() {
		return NumberFormatter.toPercent(getCategoryOnlyProfitPercentage());
	}

	public void setProfitPercentage(Float profitPercentage) {
		this.profitPercentage = profitPercentage;
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
	@Column(name = "average_schedule")
	public Float getAverageSchedule() {
		return averageSchedule;
	}
	
	@Transient
	public String getFormattedAverageSchedule() {
		return NumberFormatter.decimalFormat(averageSchedule, 2);
	}

	public void setAverageSchedule(Float averageSchedule) {
		this.averageSchedule = averageSchedule;
	}

	@Basic
	@Column(name = "oos_flag")
	public Boolean getOosFlag() {
		return oosFlag;
	}

	public void setOosFlag(Boolean oosFlag) {
		this.oosFlag = oosFlag;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "oos_last_flag", nullable = false)
	public Date getOosLastFlag() {
		return oosLastFlag;
	}

	public void setOosLastFlag(Date oosLastFlag) {
		this.oosLastFlag = oosLastFlag;
	}
}
