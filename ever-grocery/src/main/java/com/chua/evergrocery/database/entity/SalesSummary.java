package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.chua.evergrocery.database.entity.base.BaseObject;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
@MappedSuperclass
public class SalesSummary extends BaseObject {

	private static final long serialVersionUID = -874966182231311765L;

	private Float netTotal;
	
	private Float baseTotal;

	@Basic
	@Column(name = "net_total")
	public Float getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(Float netTotal) {
		this.netTotal = netTotal;
	}
	
	@Basic
	@Column(name = "base_total")
	public Float getBaseTotal() {
		return baseTotal;
	}

	public void setBaseTotal(Float baseTotal) {
		this.baseTotal = baseTotal;
	}
}
