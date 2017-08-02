package com.chua.evergrocery.rest.endpoint;

import java.io.IOException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.beans.CustomerOrderFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.CustomerOrderHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/customerorder")
public class CustomerOrderEndpoint {

	@Autowired
	private CustomerOrderHandler customerOrderHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<CustomerOrder> getCustomerOrderList(@QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("searchKey") String searchKey,
			@QueryParam("showPaid") Boolean showPaid,
			@QueryParam("daysAgo") Integer daysAgo) {
		return customerOrderHandler.getCustomerOrderList(pageNumber, searchKey, showPaid, daysAgo);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public CustomerOrder getCustomerOrder(@QueryParam("customerOrderId") Long customerOrderId) {
		return customerOrderHandler.getCustomerOrder(customerOrderId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveCustomerOrder(@FormParam("customerOrderFormData") String customerOrderFormData) throws IOException {
		final ResultBean result;
		
		final CustomerOrderFormBean customerOrderForm = new ObjectMapper().readValue(customerOrderFormData, CustomerOrderFormBean.class);
		if(customerOrderForm.getId() != null) {
			result = customerOrderHandler.updateCustomerOrder(customerOrderForm);
		} else {
			result = customerOrderHandler.createCustomerOrder(customerOrderForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeCustomerOrder(@FormParam("customerOrderId") Long customerOrderId) {
		return customerOrderHandler.removeCustomerOrder(customerOrderId);
	}
	
	@POST
	@Path("/paycustomerorder")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean payCustomerOrder(@FormParam("customerOrderId") Long customerOrderId, @FormParam("cash") Float cash) {
		return customerOrderHandler.payCustomerOrder(customerOrderId, cash);
	}
	
	@POST
	@Path("/refreshcustomerorder")
	@Produces({ MediaType.APPLICATION_JSON })
	public void refreshCustomerOrder(@FormParam("customerOrderId") Long customerOrderId) {
		customerOrderHandler.refreshCustomerOrder(customerOrderId);
	}
	
	@GET
	@Path("/detaillist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<CustomerOrderDetail> getCustomerOrderDetailList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("customerOrderId") Long customerOrderId) {
		return customerOrderHandler.getCustomerOrderDetailList(pageNumber, customerOrderId);
	}
	
	@POST
	@Path("/additembybarcode")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean addItemByBarcode(@FormParam("barcode") String barcode, @FormParam("customerOrderId") Long customerOrderId) {
		return customerOrderHandler.addItemByBarcode(barcode, customerOrderId);
	}
	
	@POST
	@Path("/additem")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean addItem(@FormParam("productDetailId") Long productDetailId,
			@FormParam("customerOrderId") Long customerOrderId,
			@FormParam("quantity") Float quantity) {
		return customerOrderHandler.addItemByProductDetailId(productDetailId, customerOrderId, quantity);
	}
	
	@POST
	@Path("/removeitem")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeCustomerOrderDetail(@FormParam("customerOrderDetailId") Long customerOrderDetailId) {
		return customerOrderHandler.removeCustomerOrderDetail(customerOrderDetailId);
	}
	
	@POST
	@Path("/changequantity")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changeCustomerOrderDetailQuantity(@FormParam("customerOrderDetailId") Long customerOrderDetailId, @FormParam("quantity") Float quantity) {
		return customerOrderHandler.changeCustomerOrderDetailQuantity(customerOrderDetailId, quantity);
	}
	
	@POST
	@Path("/printorderlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean printCustomerOrderList(@FormParam("customerOrderId") Long customerOrderId) {
		return customerOrderHandler.printCustomerOrderList(customerOrderId);
	}
	
	@POST
	@Path("/printreceipt")
	@Produces({ MediaType.APPLICATION_JSON })
	public void printReceipt(@FormParam("customerOrderId") Long customerOrderId, @FormParam("cash") Float cash) {
		customerOrderHandler.printReceipt(customerOrderId, cash);
	}
}
