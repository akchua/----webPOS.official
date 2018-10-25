package com.chua.evergrocery.scheduling;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.rest.handler.PurchaseStatisticsHandler;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	6 Jan 2017
 */
@Component
public class StatisticsScheduler {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PurchaseStatisticsHandler purchaseStatisticsHandler;
	
	/**
	 * Monthly Statistics Update
	 * fires at 8:00PM every first day of the month
	 */
	@Scheduled(cron = "0 0 20 1 * ?")
	public void monthlyStatisticsUpdate() {
		final Date start = new Date();
		LOG.info("Starting monthly statistics update");
		
		purchaseStatisticsHandler.updateAllPurchaseStatistics(3);
		
		final Date end = new Date();
		final Long seconds = (end.getTime() - start.getTime()) / 1000;
		LOG.info("Monthly statistics update complete. Total execution time : " + seconds + "s");
	}
}
