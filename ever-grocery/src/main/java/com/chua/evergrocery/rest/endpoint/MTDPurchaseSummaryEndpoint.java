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
import com.chua.evergrocery.rest.handler.MTDPurchaseSummaryHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 21, 2018
 */
@Path("/mtdpurchasesummary")
public class MTDPurchaseSummaryEndpoint {

	@Autowired
	private MTDPurchaseSummaryHandler mtdPurchaseSummaryHandler;
	
	@GET
	@Path("/mtdlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<MTDPurchaseSummary> getMTDPurchaseSummaryList() {
		return mtdPurchaseSummaryHandler.getMTDPurchaseSummaryList();
	}
	
	@GET
	@Path("/mtdlistbyyear")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<MTDPurchaseSummary> getMTDPurchaseSummaryListByYear(@QueryParam("year") Integer year) {
		return mtdPurchaseSummaryHandler.getMTDPurchaseSummaryListByYear(year);
	}
	
	@GET
	@Path("/companymtdlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<CompanyMTDPurchaseSummary> getCompanyMTDPurchaseSummaryList(@QueryParam("companyId") Long companyId) {
		return mtdPurchaseSummaryHandler.getCompanyMTDPurchaseSummaryList(companyId);
	}
}
