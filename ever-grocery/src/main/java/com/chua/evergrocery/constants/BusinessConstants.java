package com.chua.evergrocery.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 11, 2018
 */
@Component
public class BusinessConstants {

private final String businessName;
	
	private final String businessShortName;
	
	private final String businessOfficialEmail;
	
	private final String businessPrimaryContactNumber;
	
	@Autowired
	public BusinessConstants(@Value("${business.name}") String businessName,
							@Value("${business.shortName}") String businessShortName,
							@Value("${business.officialEmail}") String businessOfficialEmail,
							@Value("${business.primaryContactNumber}") String businessPrimaryContactNumber) {
		this.businessName = businessName;
		this.businessShortName = businessShortName;
		this.businessOfficialEmail = businessOfficialEmail;
		this.businessPrimaryContactNumber = businessPrimaryContactNumber;
	}

	public String getBusinessName() {
		return businessName;
	}

	public String getBusinessShortName() {
		return businessShortName;
	}

	public String getBusinessOfficialEmail() {
		return businessOfficialEmail;
	}

	public String getBusinessPrimaryContactNumber() {
		return businessPrimaryContactNumber;
	}
}
