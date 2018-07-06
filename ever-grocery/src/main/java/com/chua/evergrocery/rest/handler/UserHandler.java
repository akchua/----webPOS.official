package com.chua.evergrocery.rest.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.chua.evergrocery.beans.PasswordFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SettingsFormBean;
import com.chua.evergrocery.beans.UserFormBean;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.entity.UserImage;
import com.chua.evergrocery.objects.ObjectList;

public interface UserHandler {

	User getUser(Long userId);
	
	File findUserImageByFileName(String fileName);
	
	ObjectList<User> getUserObjectList(Integer pageNumber, String searchKey);
	
	List<UserImage> getUserImageList(Long userId);
	
	User getUserByUsernameOrEmail(String username, String emailAddress);
	
	ResultBean createUser(UserFormBean userForm);
	
	ResultBean saveUserImage(Long userId, InputStream in, FormDataContentDisposition info) throws IOException;
	
	ResultBean updateUser(UserFormBean userForm);
	
	ResultBean setUserImageAsThumbnail(Long userImageId);
	
	ResultBean removeUser(Long userId);
	
	ResultBean removeUserImage(Long userImageId);
	
	ResultBean changePassword(PasswordFormBean passwordForm);
	
	ResultBean resetPassword(Long userId);
	
	ResultBean changeSettings(SettingsFormBean settingsForm);
	
	List<User> getUserListOrderByName();
	
	List<User> getLesserUserListOrderByName();
	
	List<User> getManagerList();
	
	List<String> getUserTypeList();
}
