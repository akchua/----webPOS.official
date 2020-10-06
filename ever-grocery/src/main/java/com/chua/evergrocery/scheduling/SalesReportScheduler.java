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
	 * fires at 8:10PM every day
	 */
	@Scheduled(cron = "0 10 20 * * ?")
	//@Scheduled(cron = "0/30 * * * * ?")
	public void updateZReading() {
		salesReportHandler.updateZReading();
	}
	
	/*@Scheduled(cron = "0/15 * * * * ?")
	public void temp() {
		System.out.println("testing ..........");
		BIRBackendReport test = new BIRBackendReport(new ArrayList<ZReading>());
		test.write(fileConstants.getBackendReportHome() + "test.xlsx");
	}*/
}
