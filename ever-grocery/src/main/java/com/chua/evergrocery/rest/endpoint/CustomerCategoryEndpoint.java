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

import com.chua.evergrocery.beans.CustomerCategoryFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.CustomerCategory;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.CustomerCategoryHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
@Path("/custcategory")
public class CustomerCategoryEndpoint {

	@Autowired
	private CustomerCategoryHandler customerCategoryHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<CustomerCategory> getCustomerCategoryList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return customerCategoryHandler.getCustomerCategoryObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public CustomerCategory getCustomerCategory(@QueryParam("customerCategoryId") Long customerCategoryId) {
		return customerCategoryHandler.getCustomerCategory(customerCategoryId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveCustomerCategory(@FormParam("customerCategoryFormData") String customerCategoryFormData,
				@Context HttpServletRequest request) throws IOException {
		final ResultBean result;
		
		final CustomerCategoryFormBean customerCategoryForm = new ObjectMapper().readValue(customerCategoryFormData, CustomerCategoryFormBean.class);
		if(customerCategoryForm.getId() != null) {
			result = customerCategoryHandler.updateCustomerCategory(customerCategoryForm, request.getRemoteAddr());
		} else {
			result = customerCategoryHandler.createCustomerCategory(customerCategoryForm, request.getRemoteAddr());
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeCustomerCategory(@FormParam("customerCategoryId") Long customerCategoryId,
				@Context HttpServletRequest request) {
		return customerCategoryHandler.removeCustomerCategory(customerCategoryId, request.getRemoteAddr());
	}
	
	@GET
	@Path("/listbyname")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<CustomerCategory> getCustomerCategoryList() {
		return customerCategoryHandler.getCustomerCategoryList();
	}
}
