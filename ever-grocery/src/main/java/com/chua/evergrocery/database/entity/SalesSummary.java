package com.chua.evergrocery.database.entity;

import javax.persistence.MappedSuperclass;

import com.chua.evergrocery.database.entity.base.BaseObject;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 7, 2018
 */
@MappedSuperclass
public class SalesSummary extends BaseObject {

	private static final long serialVersionUID = -874966182231311765L;

	private Float netTotal;
	
	private Float totalProfit;

	public Float getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(Float netTotal) {
		this.netTotal = netTotal;
	}

	public Float getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(Float totalProfit) {
		this.totalProfit = totalProfit;
	}
}
