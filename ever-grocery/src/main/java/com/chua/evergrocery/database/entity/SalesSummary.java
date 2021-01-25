package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.chua.evergrocery.database.entity.base.BaseObject;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
@MappedSuperclass
public class SalesSummary extends BaseObject {

	private static final long serialVersionUID = -874966182231311765L;

	private Double netTotal;
	
	private Double baseTotal;

	@Basic
	@Column(name = "net_total")
	public Double getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(Double netTotal) {
		this.netTotal = netTotal;
	}
	
	@Basic
	@Column(name = "base_total")
	public Double getBaseTotal() {
		return baseTotal;
	}
	
	@Transient
	public Double getProfit() {
		return netTotal - baseTotal;
	}

	public void setBaseTotal(Double baseTotal) {
		this.baseTotal = baseTotal;
	}
}
