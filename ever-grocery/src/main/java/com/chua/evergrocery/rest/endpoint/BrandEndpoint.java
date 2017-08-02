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

import com.chua.evergrocery.beans.BrandFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Brand;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.BrandHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/brand")
public class BrandEndpoint {
	
	@Autowired
	private BrandHandler brandHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Brand> getBrandList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return brandHandler.getBrandObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Brand getBrand(@QueryParam("brandId") Long brandId) {
		return brandHandler.getBrand(brandId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveBrand(@FormParam("brandFormData") String brandFormData) throws IOException {
		final ResultBean result;
		
		final BrandFormBean brandForm = new ObjectMapper().readValue(brandFormData, BrandFormBean.class);
		if(brandForm.getId() != null) {
			result = brandHandler.updateBrand(brandForm);
		} else {
			result = brandHandler.createBrand(brandForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeBrand(@FormParam("brandId") Long brandId) {
		return brandHandler.removeBrand(brandId);
	}
	
	@GET
	@Path("/listbyname")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Brand> getBrandList() {
		return brandHandler.getBrandList();
	}
}
