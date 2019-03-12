package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 12, 2019
 */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "summary_type", 
	discriminatorType = DiscriminatorType.STRING)
@Entity(name = "MTDSalesSummary")
@Table(name = MTDSalesSummary.TABLE_NAME)
public class MTDSalesSummary extends SalesSummary {

	private static final long serialVersionUID = -3891045482848044570L;
	
	public static final String TABLE_NAME = "MTD_sales_summary";
	
	private Integer monthId;

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
}
