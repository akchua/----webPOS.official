package com.chua.evergrocery.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   30 Aug 2017
 */
@Component
public class SystemConstants {

	private final String version;
	
	private final String serverDomain;
	
	@Autowired
	public SystemConstants(@Value("${ever.grocery.version}") String version,
							@Value("${server.domain}") String serverDomain) {
		this.version = version;
		this.serverDomain = serverDomain;
	}

	public String getVersion() {
		return version;
	}

	public String getServerDomain() {
		return serverDomain;
	}
}
