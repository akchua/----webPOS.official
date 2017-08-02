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

import com.chua.evergrocery.beans.CategoryFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Category;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.CategoryHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/category")
public class CategoryEndpoint {

	@Autowired
	private CategoryHandler categoryHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Category> getCategoryList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return categoryHandler.getCategoryObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Category getCategory(@QueryParam("categoryId") Long categoryId) {
		return categoryHandler.getCategory(categoryId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveCategory(@FormParam("categoryFormData") String categoryFormData) throws IOException {
		final ResultBean result;
		
		final CategoryFormBean categoryForm = new ObjectMapper().readValue(categoryFormData, CategoryFormBean.class);
		if(categoryForm.getId() != null) {
			result = categoryHandler.updateCategory(categoryForm);
		} else {
			result = categoryHandler.createCategory(categoryForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeCategory(@FormParam("categoryId") Long categoryId) {
		return categoryHandler.removeCategory(categoryId);
	}
	
	@GET
	@Path("/listbyname")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Category> getCategoryList() {
		return categoryHandler.getCategoryList();
	}
}
