package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "summary_type", 
	discriminatorType = DiscriminatorType.STRING)
@Entity(name = "MTDPurchaseSummary")
@Table(name = MTDPurchaseSummary.TABLE_NAME)
public class MTDPurchaseSummary extends PurchaseSummary {

	private static final long serialVersionUID = -3717915212067707850L;
	
	public static final String TABLE_NAME = "MTD_purchase_summary";
	
	private Integer monthId;

	@Basic
	@Column(name = "month_id")
	public Integer getMonthId() {
		return monthId;
	}

	public void setMonthId(Integer monthId) {
		this.monthId = monthId;
	}
}
