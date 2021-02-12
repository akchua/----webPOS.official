package com.chua.evergrocery.rest.endpoint;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.beans.InventoryBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.rest.handler.InventoryHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 7, 2018
 */
@Path("/inventory")
public class InventoryEndpoint {

	@Autowired
	private InventoryHandler inventoryHandler;
	
	@GET
	@Path("/byproduct")
	@Produces({ MediaType.APPLICATION_JSON })
	public InventoryBean getProductInventory(@QueryParam("productId") Long productId) {
		return inventoryHandler.getProductInventory(productId);
	}
	
	@POST
	@Path("/bycompany")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean generateProductInventoryByCompany(@FormParam("companyId") Long companyId) {
		return inventoryHandler.generateInventory(companyId);
	}
	
	@POST
	@Path("/bycategoryname")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean generateProductInventoryByCategoryName(@FormParam("categoryName") String categoryName) {
		return inventoryHandler.generateInventoryByCategoryName(categoryName);
	}
	
	@POST
	@Path("/cashier")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean generateCashierInventory() {
		return inventoryHandler.generateCashierInventory();
	}
}
