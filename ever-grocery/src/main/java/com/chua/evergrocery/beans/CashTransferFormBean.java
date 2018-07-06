package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 3, 2018
 */
public class CashTransferFormBean extends FormBean {

	private Long cashToId;
	
	private Float amount;

	public Long getCashToId() {
		return cashToId;
	}

	public void setCashToId(Long cashToId) {
		this.cashToId = cashToId;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
}
