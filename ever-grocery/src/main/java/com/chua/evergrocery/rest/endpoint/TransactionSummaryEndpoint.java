package com.chua.evergrocery.rest.endpoint;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;
import com.chua.evergrocery.database.entity.MTDPurchaseSummary;
import com.chua.evergrocery.rest.handler.TransactionSummaryHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 21, 2018
 */
@Path("/transactionsummary")
public class TransactionSummaryEndpoint {

	@Autowired
	private TransactionSummaryHandler mtdPurchaseSummaryHandler;
	
	@GET
	@Path("/mtdpurchaselist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<MTDPurchaseSummary> getMTDPurchaseSummaryList() {
		return mtdPurchaseSummaryHandler.getMTDPurchaseSummaryList();
	}
	
	@GET
	@Path("/mtdpurchaselistbyyear")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<MTDPurchaseSummary> getMTDPurchaseSummaryListByYear(@QueryParam("year") Integer year) {
		return mtdPurchaseSummaryHandler.getMTDPurchaseSummaryListByYear(year);
	}
	
	@GET
	@Path("/companymtdpurchaselist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<CompanyMTDPurchaseSummary> getCompanyMTDPurchaseSummaryList(@QueryParam("companyId") Long companyId) {
		return mtdPurchaseSummaryHandler.getCompanyMTDPurchaseSummaryList(companyId);
	}
}
