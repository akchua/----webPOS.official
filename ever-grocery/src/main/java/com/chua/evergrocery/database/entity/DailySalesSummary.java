package com.chua.evergrocery.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "summary_type", 
	discriminatorType = DiscriminatorType.STRING)
@Entity(name = "DailySalesSummary")
@Table(name = DailySalesSummary.TABLE_NAME)
public class DailySalesSummary extends SalesSummary {
	
	private static final long serialVersionUID = 6410454679401475070L;
	
	public static final String TABLE_NAME = "daily_sales_summary";
	
	private Date salesDate;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "sales_date")
	public Date getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}
}
