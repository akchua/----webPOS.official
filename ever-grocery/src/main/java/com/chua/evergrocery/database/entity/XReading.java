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

import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   23 Mar 2019
 */
@Entity(name = "XReading")
@Table(name = XReading.TABLE_NAME)
public class XReading extends Reading {

	private static final long serialVersionUID = -6878278258349443048L;
	
	public static final String TABLE_NAME = "x_reading";

	private User cashier;
	
	private Double netSales;
	
	@Transient
	public String getFormattedReadingDate() {
		return DateFormatter.longFormat(getReadingDate());
	}
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "cashier_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCashier() {
		return cashier;
	}

	public void setCashier(User cashier) {
		this.cashier = cashier;
	}
	
	@Override
	@Basic
	@Column(name = "net_sales")
	public Double getNetSales() {
		return netSales;
	}
	
	@Transient
	public String getFormattedNetSales() {
		return CurrencyFormatter.pesoFormat(getNetSales());
	}

	public void setNetSales(Double netSales) {
		this.netSales = netSales;
	}
}
