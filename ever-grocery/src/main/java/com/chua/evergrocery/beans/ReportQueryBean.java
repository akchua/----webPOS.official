package com.chua.evergrocery.beans;

import java.util.Calendar;
import java.util.Date;

import com.chua.evergrocery.deserializer.json.DateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	23 Jan 2017
 */
public class ReportQueryBean {

	@JsonDeserialize(using = DateDeserializer.class)
	private Date from;
	
	@JsonDeserialize(using = DateDeserializer.class)
	private Date to;
	
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	/**
	 * Inclusive end date.
	 * @param to The end date
	 */
	public void setTo(Date to) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(to);
		calendar.add(Calendar.HOUR_OF_DAY, 23);
		calendar.add(Calendar.MINUTE, 59);
		calendar.add(Calendar.SECOND, 59);
		this.to= calendar.getTime();
	}
}
