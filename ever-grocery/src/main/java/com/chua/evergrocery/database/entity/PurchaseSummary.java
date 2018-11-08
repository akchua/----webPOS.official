package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.chua.evergrocery.database.entity.base.BaseObject;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
@MappedSuperclass
public class PurchaseSummary extends BaseObject {

	private static final long serialVersionUID = -3971918047824128940L;

	private Float grossTotal;
	
	private Float netTotal;
	
	@Basic
	@Column(name = "gross_total")
	public Float getGrossTotal() {
		return grossTotal;
	}

	public void setGrossTotal(Float grossTotal) {
		this.grossTotal = grossTotal;
	}
	
	@Basic
	@Column(name = "net_total")
	public Float getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(Float netTotal) {
		this.netTotal = netTotal;
	}
}
