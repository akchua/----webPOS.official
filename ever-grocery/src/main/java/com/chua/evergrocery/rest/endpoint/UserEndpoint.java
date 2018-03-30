package com.chua.evergrocery.rest.endpoint;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.beans.PasswordFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SettingsFormBean;
import com.chua.evergrocery.beans.UserFormBean;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.entity.UserImage;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.UserHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 30, 2017
 */
@Path("/user")
public class UserEndpoint {

	@Autowired
	private UserHandler userHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public User getUser(@QueryParam("userId") Long userId) {
		return userHandler.getUser(userId);
	}
	
	@GET
	@Path("/getimage/{fileName}")
	@Produces("image/*")
	public Response getUserImageByFileName(@PathParam("fileName") String fileName) throws IOException {
		File userImage = userHandler.findUserImageByFileName(fileName);
		if(userImage.exists())
			return Response.ok(userImage, new MimetypesFileTypeMap().getContentType(userImage))
				.build();
		else return null;
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<User> getUserObjectList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return userHandler.getUserObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/imagelist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<UserImage> getUserImageList(@QueryParam("userId") Long userId) {
		return userHandler.getUserImageList(userId);
	}
	
	@GET
	@Path("/userretrieval")
	@Produces({ MediaType.APPLICATION_JSON })
	public User getUserByUsernameOrEmail(@QueryParam("retrievalKey") String retrievalKey) {
		return userHandler.getUserByUsernameOrEmail(retrievalKey, retrievalKey);
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
	@Path("/uploadimage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)	
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean uploadUserImage(@FormDataParam("userId") Long userId,
			@FormDataParam("imageFile") InputStream in,
			@FormDataParam("imageFile") FormDataContentDisposition info) throws IOException {
		return userHandler.saveUserImage(userId, in, info);
	}
	
	@POST
	@Path("/setthumbnail")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean setUserImageAsThumbnail(@FormParam("userImageId") Long userImageId) {
		return userHandler.setUserImageAsThumbnail(userImageId);
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeUser(@FormParam("userId") Long userId) {
		return userHandler.removeUser(userId);
	}
	
	@POST
	@Path("/removeimage")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeUserImage(@FormParam("userImageId") Long userImageId) {
		return userHandler.removeUserImage(userImageId);
	}
	
	@POST
	@Path("/changepassword")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changePassword(@FormParam("passwordFormData") String passwordFormData) throws IOException {
		final PasswordFormBean passwordForm = new ObjectMapper().readValue(passwordFormData, PasswordFormBean.class);
		return userHandler.changePassword(passwordForm);
	}
	
	@POST
	@Path("/resetpassword")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean resetPassword(@FormParam("userId") Long userId) {
		return userHandler.resetPassword(userId);
	}
	
	@POST
	@Path("/changesettings")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changeSettings(@FormParam("settingsFormData") String settingsFormData) throws IOException {
		final SettingsFormBean settingsForm = new ObjectMapper().readValue(settingsFormData, SettingsFormBean.class);
		return userHandler.changeSettings(settingsForm);
	}
}
