package com.chua.evergrocery.rest.endpoint;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.database.entity.CustomerCategoryMTDSalesSummary;
import com.chua.evergrocery.database.entity.CustomerMTDSalesSummary;
import com.chua.evergrocery.rest.handler.CustomerSummaryHandler;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 29, 2020
 */
@Path("/customersummary")
public class CustomerSummaryEndpoint {

	@Autowired
	private CustomerSummaryHandler customerSummaryHandler;
	
	@GET
	@Path("/customermtdsaleslist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<CustomerMTDSalesSummary> getCustomerMTDSalesSummaryList(@QueryParam("customerId") Long customerId) {
		return customerSummaryHandler.getCustomerMTDSalesSummaryList(customerId);
	}
	
	@GET
	@Path("/customercategorymtdsaleslist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<CustomerCategoryMTDSalesSummary> getCustomerCategoryMTDSalesSummaryList(@QueryParam("customerCategoryId") Long customerCategoryId) {
		return customerSummaryHandler.getCustomerCategoryMTDSalesSummaryList(customerCategoryId);
	}
}
