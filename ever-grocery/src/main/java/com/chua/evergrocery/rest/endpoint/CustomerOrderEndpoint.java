package com.chua.evergrocery.rest.endpoint;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.beans.DiscountFormBean;
import com.chua.evergrocery.beans.PaymentsFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;
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
	@Path("/cashierlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<CustomerOrder> getCashierCustomerOrderList(@QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("searchKey") String searchKey) {
		return customerOrderHandler.getCashierCustomerOrderList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/listinglist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<CustomerOrder> getListingCustomerOrderList(@QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("searchKey") String searchKey) {
		return customerOrderHandler.getListingCustomerOrderList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public CustomerOrder getCustomerOrder(@QueryParam("customerOrderId") Long customerOrderId) {
		return customerOrderHandler.getCustomerOrder(customerOrderId);
	}
	
	@GET
	@Path("/getbysin")
	@Produces({ MediaType.APPLICATION_JSON })
	public CustomerOrder getCustomerOrderBySIN(@QueryParam("serialInvoiceNumber") Long serialInvoiceNumber) {
		return customerOrderHandler.getCustomerOrderBySIN(serialInvoiceNumber);
	}
	
	@POST
	@Path("/create")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean newCustomerOrder() {
		return customerOrderHandler.createCustomerOrder();
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeCustomerOrder(@FormParam("customerOrderId") Long customerOrderId) {
		return customerOrderHandler.removeCustomerOrder(customerOrderId);
	}
	
	@POST
	@Path("/applydiscount")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean applyDiscount(@FormParam("discountFormData") String discountFormData) throws IOException {
		return customerOrderHandler.applyDiscount(new ObjectMapper().readValue(discountFormData, DiscountFormBean.class));
	}
	
	@POST
	@Path("/paycustomerorder")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean payCustomerOrder(@FormParam("paymentsFormData") String paymentsFormData) throws IOException {
		return customerOrderHandler.payCustomerOrder(new ObjectMapper().readValue(paymentsFormData, PaymentsFormBean.class));
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
	@Path("/submit")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean submitCustomerOrder(@FormParam("customerOrderId") Long customerOrderId) {
		return customerOrderHandler.submitCustomerOrder(customerOrderId);
	}
	
	@POST
	@Path("/printordercopy")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean printCustomerOrderCopy(@FormParam("customerOrderId") Long customerOrderId) {
		return customerOrderHandler.printCustomerOrderCopy(customerOrderId);
	}
	
	@POST
	@Path("/printoriginalreceipt")
	@Produces({ MediaType.APPLICATION_JSON })
	public void printOriginalReceipt(@FormParam("customerOrderId") Long customerOrderId,
					@FormParam("footer") String footer) {
		customerOrderHandler.printReceipt(customerOrderId, footer, Boolean.TRUE);
	}
	
	@POST
	@Path("/printreceiptcopy")
	@Produces({ MediaType.APPLICATION_JSON })
	public void printReceiptCopy(@FormParam("customerOrderId") Long customerOrderId) {
		customerOrderHandler.printReceipt(customerOrderId, "", Boolean.FALSE);
	}
	
	@POST
	@Path("/printzreading")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean printZReading(@FormParam("readingDate") String readingDateData) throws ParseException {
		return customerOrderHandler.printZReading(new SimpleDateFormat("yyyy-MM-dd").parse(readingDateData));
	}
	
	@POST
	@Path("/endofshift")
	@Produces({ MediaType.APPLICATION_JSON })
	public void endOfShift() {
		customerOrderHandler.endOfShift();
	}
	
	@POST
	@Path("/generatereport")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean generateReport(@FormParam("salesReportQueryData") String salesReportQueryData) throws IOException {
		return customerOrderHandler.generateReport(new ObjectMapper().readValue(salesReportQueryData, SalesReportQueryBean.class));
	}
}
