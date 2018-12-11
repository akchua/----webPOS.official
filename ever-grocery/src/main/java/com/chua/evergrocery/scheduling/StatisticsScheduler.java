package com.chua.evergrocery.scheduling;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.rest.handler.TransactionSummaryHandler;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	6 Jan 2017
 */
@Component
public class StatisticsScheduler {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TransactionSummaryHandler transactionSummaryHandler;
	
	/**
	 * Monthly Statistics Update
	 * fires at 8:00PM every first day of the month
	 */
	@Scheduled(cron = "0 0 20 1 * ?")
	public void monthlyStatisticsUpdate() {
		final Date start = new Date();
		LOG.info("Starting monthly statistics update");
		
		transactionSummaryHandler.updateAllPurchaseSummaries(3);
		
		final Date end = new Date();
		final Float seconds = (end.getTime() - start.getTime()) / 1000.0f;
		LOG.info("Monthly statistics update complete. Total execution time : " + seconds + "s");
	}
	
	/*@Autowired
	private InventoryHandler inventoryHandler;
	
	@Scheduled(cron = "0/15 * * * * ?")
	public void test() {
		inventoryHandler.getProductInventory(4146l);
	}*/
	
	/*@Autowired
	private CustomerOrderHandler customerOrderHandler;
	
	@Scheduled(cron = "0/10 * * * * ?")
	public void test() {
		System.out.println("RUN#################################");
		customerOrderHandler.printReceipt(8l, -12404.4f);
	}*/
}
