package com.chua.evergrocery.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 17, 2016
 */
@Component
public class FileConstants {

	private final String fileHome;
	
	private final String userImageHome;
	
	private final String salesHome;
	
	private final String backendReportHome;
	
	private final String generatePurchasesHome;
	
	private final String generateOfftakeHome;
	
	private final String currentPromoHome;
	
	private final String inventoryHome;
	
	private final String activityLogHome;
	
	private final String journalFile;
	
	private final String imageDefaultFileName;
	
	@Autowired
	public FileConstants(@Value("${file.home}") String fileHome,
				@Value("${file.image.defaultFileName}") String imageDefaultFileName) {
		this.fileHome = fileHome;
		this.userImageHome = fileHome + "program_data/user_image/";
		this.salesHome = fileHome + "files/sales_report/";
		this.journalFile = fileHome + "files/journal/journal.txt";
		this.backendReportHome = fileHome + "files/backend_report/";
		this.generatePurchasesHome = fileHome + "files/generate_purchase/";
		this.generateOfftakeHome = fileHome + "files/generate_offtake/";
		this.currentPromoHome = fileHome + "files/current_promo/";
		this.inventoryHome = fileHome + "files/inventory/";
		this.activityLogHome = fileHome + "files/activity_log/";
		this.imageDefaultFileName = imageDefaultFileName;
	}

	public String getFileHome() {
		return fileHome;
	}
	
	public String getUserImageHome() {
		return userImageHome;
	}

	public String getSalesHome() {
		return salesHome;
	}

	public String getBackendReportHome() {
		return backendReportHome;
	}

	public String getGeneratePurchasesHome() {
		return generatePurchasesHome;
	}
	
	public String getGenerateOfftakeHome() {
		return generateOfftakeHome;
	}

	public String getCurrentPromoHome() {
		return currentPromoHome;
	}

	public String getInventoryHome() {
		return inventoryHome;
	}
	
	public String getActivityLogHome() {
		return activityLogHome;
	}
	
	public String getJournalFile() {
		return journalFile;
	}

	public String getImageDefaultFileName() {
		return imageDefaultFileName;
	}
}
