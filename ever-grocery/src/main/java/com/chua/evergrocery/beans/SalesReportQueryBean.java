package com.chua.evergrocery.beans;

import com.chua.evergrocery.enums.DiscountType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   8 Jul 2018
 */
public class SalesReportQueryBean extends ReportQueryBean {

	private Boolean sendMail;
	
	private DiscountType discountType;

	public Boolean getSendMail() {
		return sendMail;
	}

	public void setSendMail(Boolean sendMail) {
		this.sendMail = sendMail;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}
}
