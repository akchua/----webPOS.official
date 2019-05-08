package com.chua.evergrocery.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.beans.CustomerFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.CustomerHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/customer")
public class CustomerEndpoint {
	
	@Autowired
	private CustomerHandler customerHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Customer> getCustomerList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return customerHandler.getCustomerObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Customer getCustomer(@QueryParam("customerId") Long customerId) {
		return customerHandler.getCustomer(customerId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveCustomer(@FormParam("customerFormData") String customerFormData,
				@Context HttpServletRequest request) throws IOException {
		final ResultBean result;
		
		final CustomerFormBean customerForm = new ObjectMapper().readValue(customerFormData, CustomerFormBean.class);
		if(customerForm.getId() != null) {
			result = customerHandler.updateCustomer(customerForm, request.getRemoteAddr());
		} else {
			result = customerHandler.createCustomer(customerForm, request.getRemoteAddr());
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeCustomer(@FormParam("customerId") Long customerId,
				@Context HttpServletRequest request) {
		return customerHandler.removeCustomer(customerId, request.getRemoteAddr());
	}
	
	@GET
	@Path("/listbylastname")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Customer> getCustomerList() {
		return customerHandler.getCustomerList();
	}
}
