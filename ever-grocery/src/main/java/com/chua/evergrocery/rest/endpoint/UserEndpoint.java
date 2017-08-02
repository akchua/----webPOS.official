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

import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.UserFormBean;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.UserHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/user")
public class UserEndpoint {

	@Autowired
	private UserHandler userHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<User> getUserList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return userHandler.getUserObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public User getUser(@QueryParam("userId") Long userId) {
		return userHandler.getUser(userId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveUser(@FormParam("userFormData") String userFormData) throws IOException {
		final ResultBean result;
		
		final UserFormBean userForm = new ObjectMapper().readValue(userFormData, UserFormBean.class);
		if(userForm.getId() != null) {
			result = userHandler.updateUser(userForm);
		} else {
			result = userHandler.createUser(userForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeUser(@FormParam("userId") Long userId) {
		return userHandler.removeUser(userId);
	}
	
	@GET
	@Path("/listbyusername")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<User> getUserList() {
		return userHandler.getUserList();
	}
	
	@GET
	@Path("/listusertype")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<String> getUserTypeList() {
		return userHandler.getUserTypeList();
	}
}
