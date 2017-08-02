package com.chua.evergrocery.rest.endpoint;

import java.io.IOException;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.beans.PasswordBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.UserFormBean;
import com.chua.evergrocery.rest.handler.SettingsHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/settings")
public class SettingsEndpoint {

	@Autowired
	private SettingsHandler settingsHandler;
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveSettings(@FormParam("settingsFormData") String settingsFormData) throws IOException {
		final ResultBean result;
		
		final UserFormBean userForm = new ObjectMapper().readValue(settingsFormData, UserFormBean.class);

		result = settingsHandler.updateSettings(userForm);
		
		return result;
	}
	
	@POST
	@Path("/changepassword")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changePassword(@FormParam("passwordData") String passwordData) throws IOException {
		final ResultBean result;
		
		final PasswordBean passwordBean = new ObjectMapper().readValue(passwordData, PasswordBean.class);
		
		result = settingsHandler.changePassword(passwordBean);
		
		return result;
	}
}
