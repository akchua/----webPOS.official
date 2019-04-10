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
	
	private final String businessIncome;
	
	private final String businessOpeningHour;
	
	private final String businessCutoffHour;
	
	private final String zedMinutes;
	
	@Autowired
	public BusinessConstants(@Value("${business.name}") String businessName,
							@Value("${business.shortName}") String businessShortName,
							@Value("${business.officialEmail}") String businessOfficialEmail,
							@Value("${business.primaryContactNumber}") String businessPrimaryContactNumber,
							@Value("${business.income}") String businessIncome,
							@Value("${business.openingHour}") String businessOpeningHour,
							@Value("${business.cutoffHour}") String businessCutoffHour,
							@Value("${business.zedMinutes}") String zedMinutes) {
		this.businessName = businessName;
		this.businessShortName = businessShortName;
		this.businessOfficialEmail = businessOfficialEmail;
		this.businessPrimaryContactNumber = businessPrimaryContactNumber;
		this.businessIncome = businessIncome;
		this.businessOpeningHour = businessOpeningHour;
		this.businessCutoffHour = businessCutoffHour;
		this.zedMinutes = zedMinutes;
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

	public Float getBusinessIncome() {
		return Float.parseFloat(businessIncome);
	}
	
	public Integer getBusinessOpeningHour() {
		return Integer.parseInt(businessOpeningHour);
	}
	
	public Integer getBusinessCutoffHour() {
		return Integer.parseInt(businessCutoffHour);
	}
	
	public Integer getZedMinutes() {
		return Integer.parseInt(zedMinutes);
	}
}
