package com.chua.evergrocery.rest.handler.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.PasswordFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SettingsFormBean;
import com.chua.evergrocery.beans.UserFormBean;
import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.entity.UserImage;
import com.chua.evergrocery.database.service.UserImageService;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.enums.UserType;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.UserHandler;
import com.chua.evergrocery.rest.validator.PasswordFormValidator;
import com.chua.evergrocery.rest.validator.UserFormValidator;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.EmailUtil;
import com.chua.evergrocery.utility.EncryptionUtil;
import com.chua.evergrocery.utility.Html;
import com.chua.evergrocery.utility.StringGenerator;
import com.chua.evergrocery.utility.StringHelper;

@Transactional
@Component
public class UserHandlerImpl implements UserHandler {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserImageService userImageService;
	
	@Autowired
	private EmailUtil emailUtil;

	@Autowired
	private UserFormValidator userFormValidator;
	
	@Autowired
	private PasswordFormValidator passwordFormValidator;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Override
	public User getUser(Long userId) {
		return userService.find(userId);
	}
	
	@Override
	public File findUserImageByFileName(String fileName) {
		return new File(fileConstants.getUserImageHome() + fileName);
	}

	@Override
	public ObjectList<User> getUserObjectList(Integer pageNumber, String searchKey) {
		return userService.findAllOrderByNameAndUserType(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public List<UserImage> getUserImageList(Long userId) {
		return userImageService.findAllByUserId(userId);
	}
	
	@Override
	public User getUserByUsernameOrEmail(String username, String emailAddress) {
		return userService.findByUsernameOrEmail(username, emailAddress);
	}

	@Override
	public ResultBean createUser(UserFormBean userForm) {
		final ResultBean result;
		final Map<String, String> errors = userFormValidator.validate(userForm);
		errors.putAll(passwordFormValidator.validate(new PasswordFormBean("", userForm.getPassword(), userForm.getConfirmPassword())));
		
		if(errors.isEmpty()) {
			if(userService.isExistByUsername(userForm.getUsername().trim())) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Username already exists.") + "  Please choose another username."));
			} else {
				final User user = new User();
				
				user.setImage(fileConstants.getImageDefaultFileName());
				user.setPassword(EncryptionUtil.getMd5(userForm.getPassword()));
				user.setLastSuccessfulLogin(DateUtil.getDefaultDate());
				user.setLastAudit(DateUtil.getDefaultDate());
				setUser(user, userForm);
				setSettings(user, userForm);
				
				result = new ResultBean();
				result.setSuccess(userService.insert(user) != null);
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created account of Mr./Ms. " + Html.text(Color.BLUE, user.getFirstName() + " " + user.getLastName()) + ". Thank you."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "");
			result.addToExtras("errors", errors);
		}
		
		return result;
	}
	
	@Override
	public ResultBean saveUserImage(Long userId, InputStream in, FormDataContentDisposition info) throws IOException {
		final ResultBean result;
		final String fileName = UUID.randomUUID().toString() + "." + StringHelper.getFileExtension(info.getFileName());
		
		File imageFile = new File(fileConstants.getUserImageHome() + fileName);
		if(imageFile.getParentFile() != null) imageFile.getParentFile().mkdirs();
		
		if(!imageFile.exists()) {
			Files.copy(in, imageFile.toPath());
			final User user = userService.find(userId);
			if(user != null) {
				result = new ResultBean();
				
				final UserImage userImage = new UserImage();
				userImage.setUser(user);
				userImage.setFileName(fileName);
				
				result.setSuccess(userImageService.insert(userImage) != null);
				if(result.getSuccess()) {
					this.setUserImageAsThumbnail(userImage.getId());
					result.setMessage(Html.line(Color.GREEN, "Upload Successful."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "Error please try uploading again.");
		}
		
		return result;
	}

	@Override
	public ResultBean updateUser(UserFormBean userForm) {
		final ResultBean result;
		final User user = userService.find(userForm.getId());
		
		if(user != null) {
			final Map<String, String> errors = userFormValidator.validate(userForm);
			
			if(errors.isEmpty()) {
				final User uzer = userService.findByUsername(userForm.getUsername());
				if(uzer != null && !userForm.getUsername().equals(uzer.getUsername())) {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Username already exists.") + " Please choose another username."));
				} else {
					setUser(user, userForm);
					setSettings(user, userForm);
					
					result = new ResultBean();
					result.setSuccess(userService.update(user));
					if(result.getSuccess()) {
						UserContextHolder.refreshUser(user);
						result.setMessage(Html.line("Your profile has been " + Html.text(Color.GREEN, "updated") + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				}
			} else {
				result = new ResultBean(Boolean.FALSE, "");
				result.addToExtras("errors", errors);
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please re-log your account."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean setUserImageAsThumbnail(Long userImageId) {
		final ResultBean result;
		final UserImage userImage = userImageService.find(userImageId);
		
		if(userImage != null) {
			result = new ResultBean();
			final User user = userImage.getUser();
			
			user.setImage(userImage.getFileName());
			result.setSuccess(userService.update(user));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " set Image as picture for " + Html.text(Color.BLUE, user.getFormattedName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user image. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean removeUser(Long userId) {
		final ResultBean result;
		
		if(userId != UserContextHolder.getUser().getId()) {
			final User user = userService.find(userId);
			if(user != null) {
				if(user.getUserType().equals(UserType.ADMINISTRATOR)) {
					result = new ResultBean(Boolean.FALSE, Html.line("You are " + Html.text(Color.RED, "NOT ALLOWED") + " to delete an admin account.")
														+ Html.line("Please contact your database administrator for assistance."));
				} else {
					result = new ResultBean();
					
					result.setSuccess(userService.delete(userId));
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed the account of Mr./Ms. " + Html.text(Color.BLUE, user.getFirstName() + " " + user.getLastName()) + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("You " + Html.text(Color.RED, "CANNOT") + " delete your account."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean removeUserImage(Long userImageId) {
		final ResultBean result;
		final UserImage userImage = userImageService.find(userImageId);
		
		if(userImage != null) {
			result = new ResultBean();
			final User user = userImage.getUser();
			
			// REMOVE AS THUMBNAIL IF DELETED
			if(user.getImage().equals(userImage.getFileName())) {
				user.setImage(fileConstants.getImageDefaultFileName());
				userService.update(user);
			}
			
			result.setSuccess(userImageService.delete(userImage));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Image."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user image. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean changePassword(PasswordFormBean passwordForm) {
		final ResultBean result;
		final User user = userService.find(passwordForm.getId());
		
		if(user != null) {
			final Map<String, String> errors = passwordFormValidator.validate(passwordForm);
			if(errors.get("oldPassword") == null && 
					!user.getPassword().equals(EncryptionUtil.getMd5(passwordForm.getOldPassword()))) {
				errors.put("oldPassword", "Incorrect password.");
			}
				
			if(errors.isEmpty()) {
				result = new ResultBean();
				user.setPassword(EncryptionUtil.getMd5(passwordForm.getPassword()));
				
				result.setSuccess(userService.update(user));
				if(result.getSuccess()) {
					UserContextHolder.refreshUser(user);
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " changed your password."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, "");
				result.addToExtras("errors", errors);
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please re-log your account."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean resetPassword(Long userId) {
		final ResultBean result;
		final User user = userService.find(userId);
		
		if(user != null) {
			result = new ResultBean();
			String randomPassword = StringGenerator.nextString();
			
			user.setPassword(EncryptionUtil.getMd5(randomPassword));
			result.setSuccess(userService.update(user) &&
					emailUtil.send(user.getEmailAddress()
					, "Ever Reset Password"
					, "Hi, " + user.getFirstName() + " " + user.getLastName()
						+ "\n\n\nUsername - " + user.getUsername()
						+ "\nPasswrod - " + randomPassword
						+ "\nPlease login and change your password as soon as possible."));
			
			if(result.getSuccess()) {
				UserContextHolder.refreshUser(user);
				result.setMessage(Html.line(Color.GREEN, "Password successfully reset.") 
						+ Html.line("New password sent to " + Html.text(Color.BLUE, user.getEmailAddress()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please re-log your account."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean changeSettings(SettingsFormBean settingsForm) {
		final ResultBean result;
		final User user = userService.find(settingsForm.getId());
		
		if(user != null) {
			result = new ResultBean();
			setSettings(user, settingsForm);
			
			result.setSuccess(userService.update(user));
			if(result.getSuccess()) {
				UserContextHolder.refreshUser(user);
				result.setMessage(Html.line(Color.GREEN, "Settings successfully updated."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please re-log your account."));
		}
		
		return result;
	}
	
	private void setUser(User user, UserFormBean userForm) {
		user.setUsername(userForm.getUsername().trim());
		user.setFirstName(userForm.getFirstName().trim());
		user.setLastName(userForm.getLastName().trim());
		user.setEmailAddress(userForm.getEmailAddress().trim());
		user.setContactNumber(userForm.getContactNumber().trim());
		user.setUserType(userForm.getUserType() != null ? userForm.getUserType() : UserType.ADMINISTRATOR);
	}
	
	private void setSettings(User user, UserFormBean userFormBean) {
		setSettings(user, new SettingsFormBean(userFormBean.getItemsPerPage()));
	}
	
	private void setSettings(User user, SettingsFormBean settingsForm) {
		user.setItemsPerPage(settingsForm.getItemsPerPage() != null ? settingsForm.getItemsPerPage() : 10);
	}
	
	@Override
	public List<User> getUserList() {
		return userService.findAllOrderByLastName();
	}
	
	@Override
	public List<String> getUserTypeList() {
		return Arrays.asList(UserType.values()).stream().map(UserType::name).collect(Collectors.toList());
	}
}
