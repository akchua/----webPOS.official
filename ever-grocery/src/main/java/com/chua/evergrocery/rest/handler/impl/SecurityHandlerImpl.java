package com.chua.evergrocery.rest.handler.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.UserBean;
import com.chua.evergrocery.rest.handler.ActivityLogHandler;
import com.chua.evergrocery.rest.handler.SecurityHandler;

@Transactional
@Component
public class SecurityHandlerImpl implements SecurityHandler {

	//private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ActivityLogHandler activityLogHandler;
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null) {
			activityLogHandler.log(UserContextHolder.getShortName(), "logged out", request.getRemoteAddr());
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
	}

	@Override
	public UserBean getUser() {
		return UserContextHolder.getUser();
	}

	@Override
	public Boolean ipAuth(HttpServletRequest request) {
		final Boolean success = true;
		
		/*List<String> allowedIp = new ArrayList<String>();
		allowedIp.add("0:0:0:0:0:0:0:1");
		for(int i = 2; i <= 99; i++) allowedIp.add("192.168.0." + i); //2.25.99
		
		success = allowedIp.contains(request.getRemoteAddr());
		
		if(success) LOG.info("Successful IP Auth : " + request.getRemoteAddr());
		else LOG.info("Declined IP Auth : " + request.getRemoteAddr());*/
		
		return success;
	}
}
