package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 3, 2018
 */
public class CashTransferFormBean extends FormBean {

	private Long cashToId;
	
	private Double amount;

	public Long getCashToId() {
		return cashToId;
	}

	public void setCashToId(Long cashToId) {
		this.cashToId = cashToId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
