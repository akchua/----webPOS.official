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

import com.chua.evergrocery.beans.CashTransferFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.CashTransfer;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.CashTransferHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 3, 2018
 */
@Path("/cashtransfer")
public class CashTransferEndpoint {

	@Autowired
	private CashTransferHandler cashTransferHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public CashTransfer getCashTransfer(@QueryParam("cashTransferId") Long cashTransferId) {
		return cashTransferHandler.getCashTransfer(cashTransferId);
	}
	
	@GET
	@Path("/mycashtransfer")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<CashTransfer> getMyCashTransfer(@QueryParam("pageNumber") Integer pageNumber) {
		return cashTransferHandler.getMyCashTransfer(pageNumber);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<CashTransfer> getCashTransferObjectList(@QueryParam("pageNumber") Integer pageNumber, 
				@QueryParam("userId") Long userId, 
				@QueryParam("status") Status status) {
		return cashTransferHandler.getCashTransferObjectList(pageNumber, userId, status);
	}
	
	@POST
	@Path("/request")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean requestCashTransfer(@FormParam("cashTransferFormData") String cashTransferFormData) throws IOException {
		final CashTransferFormBean cashTransferForm = new ObjectMapper().readValue(cashTransferFormData, CashTransferFormBean.class);
		return cashTransferHandler.requestCashTransfer(cashTransferForm);
	}
	
	@POST
	@Path("accept")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean acceptCashTransfer(@FormParam("cashTransferId") Long cashTransferId, @FormParam("auth") String auth) {
		return cashTransferHandler.acceptCashTransfer(cashTransferId, auth);
	}
	
	@POST
	@Path("cancel")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean cancelCashTransfer(@FormParam("cashTransferId") Long cashTransferId) {
		return cashTransferHandler.cancelCashTransfer(cashTransferId);
	}
	
	@POST
	@Path("audit")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean auditUser(@FormParam("userId") Long userId,
				@FormParam("fullAudit") Boolean fullAudit) {
		return cashTransferHandler.auditUser(userId, fullAudit);
	}
}
