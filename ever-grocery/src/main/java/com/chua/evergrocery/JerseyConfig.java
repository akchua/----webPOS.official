package com.chua.evergrocery;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.rest.endpoint.BrandEndpoint;
import com.chua.evergrocery.rest.endpoint.CategoryEndpoint;
import com.chua.evergrocery.rest.endpoint.CompanyEndpoint;
import com.chua.evergrocery.rest.endpoint.CustomerEndpoint;
import com.chua.evergrocery.rest.endpoint.CustomerOrderEndpoint;
import com.chua.evergrocery.rest.endpoint.DistributorEndpoint;
import com.chua.evergrocery.rest.endpoint.ProductEndpoint;
import com.chua.evergrocery.rest.endpoint.PurchaseOrderEndpoint;
import com.chua.evergrocery.rest.endpoint.SecurityEndpoint;
import com.chua.evergrocery.rest.endpoint.SettingsEndpoint;
import com.chua.evergrocery.rest.endpoint.UserEndpoint;

@Component
@ApplicationPath("services")
public class JerseyConfig extends ResourceConfig {
	
	public JerseyConfig() {
		//
		
		// Register End Points
		register(BrandEndpoint.class);
		register(CategoryEndpoint.class);
		register(CompanyEndpoint.class);
		register(DistributorEndpoint.class);
		register(ProductEndpoint.class);
		
		register(SettingsEndpoint.class);
		register(UserEndpoint.class);
		register(SecurityEndpoint.class);
		
		register(CustomerEndpoint.class);
		register(CustomerOrderEndpoint.class);
		
		register(PurchaseOrderEndpoint.class);
	}
}
