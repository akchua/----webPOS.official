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
import com.chua.evergrocery.enums.ReceiptType;
import com.chua.evergrocery.serializer.json.DistributorSerializer;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.chua.evergrocery.utility.format.NumberFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name = "Company")
@Table(name = Company.TABLE_NAME)
public class Company extends BaseObject {
	private static final long serialVersionUID = 5717539669561642586L;
	
	public static final String TABLE_NAME = "company";
	
	private String name;
	
	private String address;
	
	private String agent;
	
	private String phoneNumber;
	
	private ReceiptType receiptType;
	
	@JsonSerialize(using = DistributorSerializer.class)
	private Distributor distributor;
	
	private Integer minTerms;
	
	private Integer maxTerms;
	
	private Float daysBooked;
	
	private Date lastPurchaseOrderDate;
	
	private Date lastStatisticsUpdate;
	
	// Percentage in whole system (previous month only) (net amount is used)
	private Float purchaseValuePercentage;
	
	private Float saleValuePercentage;
	
	private Float profitPercentage;
	
	// Monthly profit ranking
	private Integer currentProfitRank;
	
	private Integer previousProfitRank;
	
	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	@Column(name = "agent")
	public String getAgent() {
		return agent;
	}
	
	public void setAgent(String agent) {
		this.agent = agent;
	}
	
	@Basic
	@Column(name = "phone_number")
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "receipt_type", length = 50)
	public ReceiptType getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(ReceiptType receiptType) {
		this.receiptType = receiptType;
	}
	
	@ManyToOne(targetEntity = Distributor.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "distributor_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Distributor getDistributor() {
		return distributor;
	}
	
	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

	@Basic
	@Column(name = "min_terms")
	public Integer getMinTerms() {
		return minTerms;
	}

	public void setMinTerms(Integer minTerms) {
		this.minTerms = minTerms;
	}

	@Basic
	@Column(name = "max_terms")
	public Integer getMaxTerms() {
		return maxTerms;
	}

	public void setMaxTerms(Integer maxTerms) {
		this.maxTerms = maxTerms;
	}

	@Basic
	@Column(name = "days_booked")
	public Float getDaysBooked() {
		return daysBooked;
	}

	public void setDaysBooked(Float daysBooked) {
		this.daysBooked = daysBooked;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_purchase_order_date")
	public Date getLastPurchaseOrderDate() {
		return lastPurchaseOrderDate;
	}
	
	@Transient
	public String getFormattedLastPurchaseOrderDate() {
		return DateUtil.getDefaultDate().equals(lastPurchaseOrderDate) ? "n/a" : DateFormatter.prettyFormat(lastPurchaseOrderDate);
	}
	
	@Transient
	public Boolean isAutoOrderActive() {
		return !DateUtil.getDefaultDate().equals(lastPurchaseOrderDate);
	}

	public void setLastPurchaseOrderDate(Date lastPurchaseOrderDate) {
		this.lastPurchaseOrderDate = lastPurchaseOrderDate;
	}
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_statistics_update")
	public Date getLastStatisticsUpdate() {
		return lastStatisticsUpdate;
	}

	public void setLastStatisticsUpdate(Date lastStatisticsUpdate) {
		this.lastStatisticsUpdate = lastStatisticsUpdate;
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
}
