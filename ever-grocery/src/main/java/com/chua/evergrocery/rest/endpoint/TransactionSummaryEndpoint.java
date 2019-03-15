package com.chua.evergrocery.rest.endpoint;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;
import com.chua.evergrocery.database.entity.CompanyMTDSalesSummary;
import com.chua.evergrocery.database.entity.DailySalesSummary;
import com.chua.evergrocery.database.entity.MTDPurchaseSummary;
import com.chua.evergrocery.database.entity.MTDSalesSummary;
import com.chua.evergrocery.rest.handler.TransactionSummaryHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 21, 2018
 */
@Path("/transactionsummary")
public class TransactionSummaryEndpoint {

	@Autowired
	private TransactionSummaryHandler transactionSummaryHandler;
	
	@GET
	@Path("/mtdpurchaselist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<MTDPurchaseSummary> getMTDPurchaseSummaryList() {
		return transactionSummaryHandler.getMTDPurchaseSummaryList();
	}
	
	@GET
	@Path("/mtdpurchaselistbyyear")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<MTDPurchaseSummary> getMTDPurchaseSummaryListByYear(@QueryParam("year") Integer year) {
		return transactionSummaryHandler.getMTDPurchaseSummaryListByYear(year);
	}
	
	@GET
	@Path("/companymtdpurchaselist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<CompanyMTDPurchaseSummary> getCompanyMTDPurchaseSummaryList(@QueryParam("companyId") Long companyId) {
		return transactionSummaryHandler.getCompanyMTDPurchaseSummaryList(companyId);
	}
	
	@GET
	@Path("/mtdsaleslist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<MTDSalesSummary> getMTDSalesSummaryList() {
		return transactionSummaryHandler.getMTDSalesSummaryList();
	}
	
	@GET
	@Path("/mtdsaleslistbyyear")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<MTDSalesSummary> getMTDSalesSummaryListByYear(@QueryParam("year") Integer year) {
		return transactionSummaryHandler.getMTDSalesSummaryListByYear(year);
	}
	
	@GET
	@Path("/companymtdsaleslist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<CompanyMTDSalesSummary> getCompanyMTDSalesSummaryList(@QueryParam("companyId") Long companyId) {
		return transactionSummaryHandler.getCompanyMTDSalesSummaryList(companyId);
	}
	
	@GET
	@Path("/dailysaleslist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<DailySalesSummary> getDailySalesSummaryList(@QueryParam("daysAgo") Integer daysAgo) {
		return transactionSummaryHandler.getDailySalesSummaryList(daysAgo);
	}
}
