package com.chua.evergrocery.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.beans.DistributorFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Distributor;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.DistributorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/distributor")
public class DistributorEndpoint {

	@Autowired
	private DistributorHandler distributorHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Distributor> getDistributorList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return distributorHandler.getDistributorObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Distributor getDistributor(@QueryParam("distributorId") Long distributorId) {
		return distributorHandler.getDistributor(distributorId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveDistributor(@FormParam("distributorFormData") String distributorFormData) throws IOException {
		final ResultBean result;
		
		final DistributorFormBean distributorForm = new ObjectMapper().readValue(distributorFormData, DistributorFormBean.class);
		if(distributorForm.getId() != null) {
			result = distributorHandler.updateDistributor(distributorForm);
		} else {
			result = distributorHandler.createDistributor(distributorForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeDistributor(@FormParam("distributorId") Long distributorId) {
		return distributorHandler.removeDistributor(distributorId);
	}
	
	@GET
	@Path("/listbyname")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Distributor> getDistributorList() {
		return distributorHandler.getDistributorList();
	}
}
