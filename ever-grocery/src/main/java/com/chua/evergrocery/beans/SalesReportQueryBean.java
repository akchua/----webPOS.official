package com.chua.evergrocery.beans;

import com.chua.evergrocery.enums.ReportType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   8 Jul 2018
 */
public class SalesReportQueryBean extends ReportQueryBean {

	private Boolean sendMail;
	
	private ReportType reportType;

	public Boolean getSendMail() {
		return sendMail;
	}

	public void setSendMail(Boolean sendMail) {
		this.sendMail = sendMail;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}
}
