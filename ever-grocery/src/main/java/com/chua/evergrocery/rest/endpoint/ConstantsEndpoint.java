package com.chua.evergrocery.rest.endpoint;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.PromoType;
import com.chua.evergrocery.enums.ReportType;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.enums.TaxType;
import com.chua.evergrocery.enums.UserType;
import com.chua.evergrocery.rest.handler.ConstantsHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   6 Dec 2017
 */
@Path("/constants")
public class ConstantsEndpoint {

	@Autowired
	private ConstantsHandler constantsHandler;
	
	@GET
	@Path("/version")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getVersion() {
		return constantsHandler.getVersion();
	}
	
	@GET
	@Path("/taxtype")
	@Produces({ MediaType.APPLICATION_JSON })
	public TaxType getDefaultTaxType() {
		return constantsHandler.getDefaultTaxType();
	}
	
	@GET
	@Path("/usertype")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<UserType> getUserTypeList() {
		return constantsHandler.getUserTypeList();
	}
	
	@GET
	@Path("/taxtypelist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<TaxType> getTaxTypeList() {
		return constantsHandler.getTaxTypeList();
	}
	
	@GET
	@Path("/discounttypelist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<DiscountType> getDiscountTypeList() {
		return constantsHandler.getDiscountTypeList();
	}
	
	@GET
	@Path("/promotypelist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<PromoType> getPromoTypeList() {
		return constantsHandler.getPromoTypeList();
	}
	
	@GET
	@Path("/reporttypelist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<ReportType> getReportTypeList() {
		return constantsHandler.getReportTypeList();
	}
	
	@GET
	@Path("/cashtransferstatus")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Status> getCashTransferStatusList() {
		return constantsHandler.getCashTransferStatusList();
	}
}
