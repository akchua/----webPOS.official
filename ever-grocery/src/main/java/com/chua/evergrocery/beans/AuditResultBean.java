package com.chua.evergrocery.beans;

import java.util.Calendar;
import java.util.Date;

import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 5, 2018
 */
public class AuditResultBean {

	private Float withheldCash;
	
	private Float cashTransferOut;
	
	private Float cashTransferReceived;
	
	private Float sales;
	
	private Date lastAudit;
	
	public Float getWithheldCash() {
		return withheldCash;
	}
	
	public String getFormattedWithheldCash() {
		return CurrencyFormatter.pesoFormat(withheldCash);
	}

	public void setWithheldCash(Float withheldCash) {
		this.withheldCash = withheldCash;
	}

	public Float getCashTransferOut() {
		return cashTransferOut;
	}
	
	public String getFormattedCashTransferOut() {
		return CurrencyFormatter.pesoFormat(cashTransferOut);
	}

	public void setCashTransferOut(Float cashTransferOut) {
		this.cashTransferOut = cashTransferOut;
	}

	public Float getCashTransferReceived() {
		return cashTransferReceived;
	}
	
	public String getFormattedCashTransferReceived() {
		return CurrencyFormatter.pesoFormat(cashTransferReceived);
	}

	public void setCashTransferReceived(Float cashTransferReceived) {
		this.cashTransferReceived = cashTransferReceived;
	}

	public Float getSales() {
		return sales;
	}
	
	public String getFormattedSales() {
		return CurrencyFormatter.pesoFormat(sales);
	}

	public void setSales(Float sales) {
		this.sales = sales;
	}

	public Date getLastAudit() {
		return lastAudit;
	}
	
	public String getFormattedLastAudit() {
		final String formattedTransferredOn;
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastAudit);
		
		if(cal.getTimeInMillis() == DateUtil.getDefaultDateInMillis()) {
			formattedTransferredOn = "Start of time";
		} else {
			formattedTransferredOn = DateFormatter.longFormat(lastAudit);
		}
		return formattedTransferredOn;
	}

	public void setLastAudit(Date lastAudit) {
		this.lastAudit = lastAudit;
	}
}
