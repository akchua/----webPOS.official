package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.evergrocery.serializer.json.CustomerCategorySerializer;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
@Entity(name = "CustomerCategoryMTDSalesSummary")
@Table(name = CustomerCategoryMTDSalesSummary.TABLE_NAME)
public class CustomerCategoryMTDSalesSummary extends SalesSummary {

	private static final long serialVersionUID = -1959339913698168213L;

	public static final String TABLE_NAME = "customer_category_mtd_sales_summary";
	
	private Integer monthId;
	
	@JsonSerialize(using = CustomerCategorySerializer.class)
	private CustomerCategory customerCategory;
	
	private Double luxuryTotal;

	@Basic
	@Column(name = "month_id")
	public Integer getMonthId() {
		return monthId;
	}
	
	@Transient
	public String getFormattedMonth() {
		return DateFormatter.prettyMonthFormat(monthId);
	}

	public void setMonthId(Integer monthId) {
		this.monthId = monthId;
	}

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
	@Column(name = "luxury_total")
	public Double getLuxuryTotal() {
		return luxuryTotal;
	}

	public void setLuxuryTotal(Double luxuryTotal) {
		this.luxuryTotal = luxuryTotal;
	}
}
