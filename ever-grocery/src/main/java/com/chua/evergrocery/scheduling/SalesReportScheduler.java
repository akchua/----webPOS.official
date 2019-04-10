package com.chua.evergrocery.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.rest.handler.SalesReportHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   22 Mar 2019
 */
@Component
public class SalesReportScheduler {

	@Autowired
	private SalesReportHandler salesReportHandler;
	
	/**
	 * Daily Z Reading Update
	 * fires at 7:10PM every day
	 */
	@Scheduled(cron = "0 10 19 * * ?")
	public void updateZReading() {
		salesReportHandler.updateZReading();
	}
	
	/*@Scheduled(cron = "40 41 * * * ?")
	public void temp() {
		Calendar temp = Calendar.getInstance();
		temp.add(Calendar.DAY_OF_MONTH, -2);
		salesReportHandler.printZReading(temp.getTime());
	}*/
}
