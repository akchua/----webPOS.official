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

import com.chua.evergrocery.beans.CompanyFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.enums.ReceiptType;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.CompanyHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/company")
public class CompanyEndpoint {

	@Autowired
	private CompanyHandler companyHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Company> getCompanyList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return companyHandler.getCompanyObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/listbyrank")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Company> getCompanyListByRank(@QueryParam("pageNumber") Integer pageNumber, 
			@QueryParam("searchKey") String searchKey) {
		return companyHandler.getCompanyListByRank(pageNumber, searchKey);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Company getCompany(@QueryParam("companyId") Long companyId) {
		return companyHandler.getCompany(companyId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveCompany(@FormParam("companyFormData") String companyFormData,
				@Context HttpServletRequest request) throws IOException {
		final ResultBean result;
		
		final CompanyFormBean companyForm = new ObjectMapper().readValue(companyFormData, CompanyFormBean.class);
		if(companyForm.getId() != null) {
			result = companyHandler.updateCompany(companyForm, request.getRemoteAddr());
		} else {
			result = companyHandler.createCompany(companyForm, request.getRemoteAddr());
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeCompany(@FormParam("companyId") Long companyId,
				@Context HttpServletRequest request) {
		return companyHandler.removeCompany(companyId, request.getRemoteAddr());
	}
	
	@GET
	@Path("/listbyname")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Company> getCompanyList() {
		return companyHandler.getCompanyList();
	}
	
	@GET
	@Path("/listreceipttype")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<ReceiptType> getReceiptTypeList() {
		return companyHandler.getReceiptTypeList();
	}
}
