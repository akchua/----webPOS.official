package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   22 Mar 2019
 */
public class PaymentsFormBean extends FormBean {

	private Long customerOrderId;
	
	private Float cash;
	
	private String checkAccountNumber;
	
	private String checkNumber;
	
	private Float checkAmount;
	
	private String cardTransactionNumber;
	
	private Float cardAmount;
	
	private Long refSIN;

	public Long getCustomerOrderId() {
		return customerOrderId;
	}

	public void setCustomerOrderId(Long customerOrderId) {
		this.customerOrderId = customerOrderId;
	}

	public Float getCash() {
		return cash;
	}

	public void setCash(Float cash) {
		this.cash = cash;
	}

	public String getCheckAccountNumber() {
		return checkAccountNumber;
	}

	public void setCheckAccountNumber(String checkAccountNumber) {
		this.checkAccountNumber = checkAccountNumber;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public Float getCheckAmount() {
		return checkAmount;
	}
	
	public void setCheckAmount(Float checkAmount) {
		this.checkAmount = checkAmount;
	}

	public String getCardTransactionNumber() {
		return cardTransactionNumber;
	}
	
	public void setCardTransactionNumber(String cardTransactionNumber) {
		this.cardTransactionNumber = cardTransactionNumber;
	}

	public Float getCardAmount() {
		return cardAmount;
	}
	
	public Float getTotalPayment() {
		return getCash() + getCheckAmount() + getCardAmount();
	}

	public void setCardAmount(Float cardAmount) {
		this.cardAmount = cardAmount;
	}

	public Long getRefSIN() {
		return refSIN;
	}

	public void setRefSIN(Long refSIN) {
		this.refSIN = refSIN;
	}
}
