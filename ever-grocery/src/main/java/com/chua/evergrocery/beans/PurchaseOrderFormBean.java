package com.chua.evergrocery.beans;

import java.util.Date;

import com.chua.evergrocery.deserializer.json.DateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class PurchaseOrderFormBean extends FormBean {

	private Long companyId;
	
	@JsonDeserialize(using = DateDeserializer.class)
	private Date deliveredOn;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Date getDeliveredOn() {
		return deliveredOn;
	}

	public void setDeliveredOn(Date deliveredOn) {
		this.deliveredOn = deliveredOn;
	}
}
