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

import com.chua.evergrocery.beans.PurchaseOrderFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.PurchaseOrderHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/purchaseorder")
public class PurchaseOrderEndpoint {

	@Autowired
	private PurchaseOrderHandler purchaseOrderHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<PurchaseOrder> getAllPurhcaseOrderList(@QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("companyId") Long companyId,
			@QueryParam("showChecked") Boolean showChecked) {
		return purchaseOrderHandler.getPurchaseOrderList(pageNumber, companyId, showChecked);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public PurchaseOrder getPurchaseOrder(@QueryParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderHandler.getPurchaseOrder(purchaseOrderId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean savePurchaseOrder(@FormParam("purchaseOrderFormData") String purchaseOrderFormData) throws IOException {
		final PurchaseOrderFormBean purchaseOrderForm = new ObjectMapper().readValue(purchaseOrderFormData, PurchaseOrderFormBean.class);
		
		return purchaseOrderHandler.createPurchaseOrder(purchaseOrderForm);
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removePurchaseOrder(@FormParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderHandler.removePurchaseOrder(purchaseOrderId);
	}
	
	@POST
	@Path("/refreshpurchaseorder")
	@Produces({ MediaType.APPLICATION_JSON })
	public void refreshPurchaseOrder(@FormParam("purchaseOrderId") Long purchaseOrderId) {
		purchaseOrderHandler.refreshPurchaseOrder(purchaseOrderId);
	}
	
	@GET
	@Path("/detaillist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<PurchaseOrderDetail> getPurchaseOrderDetailList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderHandler.getPurchaseOrderDetailList(pageNumber, purchaseOrderId);
	}
	
	@POST
	@Path("/additem")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean addItem(@FormParam("productDetailId") Long productDetailId,
			@FormParam("purchaseOrderId") Long purchaseOrderId,
			@FormParam("quantity") Integer quantity) {
		return purchaseOrderHandler.addItemByProductDetailId(productDetailId, purchaseOrderId, quantity);
	}
	
	@POST
	@Path("/removeitem")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removePurchaseOrderDetail(@FormParam("purchaseOrderDetailId") Long purchaseOrderDetailId) {
		return purchaseOrderHandler.removePurchaseOrderDetail(purchaseOrderDetailId);
	}
	
	@POST
	@Path("/changequantity")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changePurchaseOrderDetailQuantity(@FormParam("purchaseOrderDetailId") Long purchaseOrderDetailId, @FormParam("quantity") Integer quantity) {
		return purchaseOrderHandler.changePurchaseOrderDetailQuantity(purchaseOrderDetailId, quantity);
	}
	
	@POST
	@Path("/checkpurchaseorder")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean checkPurchaserOrder(@FormParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderHandler.checkPurchaseOrder(purchaseOrderId);
	}
}
