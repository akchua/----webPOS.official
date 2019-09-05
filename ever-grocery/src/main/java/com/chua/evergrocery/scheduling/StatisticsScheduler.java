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
	 * Monthly Purchase Statistics Update
	 * fires at 7:00PM every first day of the month
	 */
	@Scheduled(cron = "0 0 19 1 * ?")
	public void monthlyPurchaseStatisticsUpdate() {
		final Date start = new Date();
		LOG.info("Starting monthly purchase statistics update");
		
		transactionSummaryHandler.updateAllPurchaseSummaries(3);
		
		final Date end = new Date();
		final Float seconds = (end.getTime() - start.getTime()) / 1000.0f;
		LOG.info("Monthly purchase statistics update complete. Total execution time : " + seconds + "s");
	}
	
	/**
	 * Monthly Sales Statistics Update
	 * fires at 7:05PM every first day of the month
	 */
	@Scheduled(cron = "0 5 19 1 * ?")
	//@Scheduled(cron = "0 15 23 * * ?")
	public void monthlySalesStatisticsUpdate() {
		final Date start = new Date();
		LOG.info("Starting monthly sales statistics update");
		
		transactionSummaryHandler.updateMonthlySalesSummaries(3);
		
		final Date end = new Date();
		final Float seconds = (end.getTime() - start.getTime()) / 1000.0f;
		LOG.info("Monthly sales statistics update complete. Total execution time : " + seconds + "s");
	}
	
	/**
	 * Daily Sales Statistics Update
	 * fires at 6:55PM every day
	 */
	@Scheduled(cron = "0 55 18 * * ?")
	public void dailySalesStatisticsUpdate() {
		final Date start = new Date();
		LOG.info("Starting daily sales statistics update");
		
		transactionSummaryHandler.updateDailySalesSummaries(90);
		
		final Date end = new Date();
		final Float seconds = (end.getTime() - start.getTime()) / 1000.0f;
		LOG.info("Daily sales statistics update complete. Total execution time : " + seconds + "s");
	}
	
	/*@Autowired
	private InventoryHandler inventoryHandler;
	
	@Scheduled(cron = "0/15 * * * * ?")
	public void test() {
		inventoryHandler.getProductInventory(4146l);
	}*/
	
	/*@Autowired
	private CustomerOrderHandler customerOrderHandler;
	
	@Scheduled(cron = "0/15 * * * * ?")
	public void test() {
		customerOrderHandler.printReceipt(8l, -12404.4f);
		System.out.println("RUN#################################");
	}*/
}
