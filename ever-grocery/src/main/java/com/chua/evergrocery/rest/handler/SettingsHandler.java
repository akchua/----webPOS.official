package com.chua.evergrocery.rest.handler;

import com.chua.evergrocery.beans.PasswordBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.UserFormBean;

public interface SettingsHandler {

	ResultBean updateSettings(UserFormBean userForm);
	
	ResultBean changePassword(PasswordBean passwordBean);
}
