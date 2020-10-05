package com.chua.evergrocery.scheduling;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.rest.handler.ProfitRankingHandler;
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
	
	@Autowired
	private ProfitRankingHandler profitRankingHandler;
	
	/**
	 * Monthly Purchase Statistics Update
	 * fires at 8:00PM every first day of the month
	 */
	@Scheduled(cron = "0 0 20 1 * ?")
	//@Scheduled(cron = "0 29 15 * * ?")
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
	 * fires at 8:05PM every first day of the month
	 */
	@Scheduled(cron = "0 5 20 1 * ?")
	//@Scheduled(cron = "0 26 15 * * ?")
	public void monthlySalesStatisticsUpdate() {
		final Date start = new Date();
		LOG.info("Starting monthly sales statistics update");
		
		transactionSummaryHandler.updateMonthlySalesSummaries(1);
		
		final Date end = new Date();
		final Float seconds = (end.getTime() - start.getTime()) / 1000.0f;
		LOG.info("Monthly sales statistics update complete. Total execution time : " + seconds + "s");
		
		this.monthlyProfitRankingUpdate();
	}
	
	/**
	 * Monthly Profit Ranking Update
	 * fired by @monthlySalesStatisticsUpdate after completion
	 */
	//@Scheduled(cron = "0 15 21 * * ?")
	public void monthlyProfitRankingUpdate() {
		final Date start = new Date();
		LOG.info("Starting monthly profit ranking update");
		
		profitRankingHandler.updateAllProductProfitRankings();
		profitRankingHandler.updateAllCompanyProfitRankings();
		
		final Date end = new Date();
		final Float seconds = (end.getTime() - start.getTime()) / 1000.0f;
		LOG.info("Monthly profit ranking update complete. Total execution time : " + seconds + "s");
	}
	
	/**
	 * Monthly product month to day average offtake update
	 * fires at 8:10PM every first day of the month
	 */
	@Scheduled(cron = "0 10 20 1 * ?")
	//@Scheduled(cron = "0 02 10 * * ?")
	public void monthlyProductMTDOfftakeUpdate() {
		final Date start = new Date();
		LOG.info("Starting monthly product mtd offtake update");
		
		transactionSummaryHandler.updateAllProductMTDOfftake();
		
		final Date end = new Date();
		final Float seconds = (end.getTime() - start.getTime()) / 1000.0f;
		LOG.info("Monthly product mtd offtake update complete. Total execution time : " + seconds + "s");
	}
	
	/**
	 * Daily Sales Statistics Update
	 * fires at 7:55PM every day
	 */
	@Scheduled(cron = "0 55 19 * * ?")
	public void dailySalesStatisticsUpdate() {
		final Date start = new Date();
		LOG.info("Starting daily sales statistics update");
		
		transactionSummaryHandler.updateDailySalesSummaries(1);
		
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
