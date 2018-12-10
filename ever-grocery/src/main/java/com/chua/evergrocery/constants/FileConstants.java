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
	
	private final String receiptHome;
	
	private final String generatePurchasesHome;
	
	private final String inventoryHome;
	
	private final String imageDefaultFileName;
	
	@Autowired
	public FileConstants(@Value("${file.home}") String fileHome,
				@Value("${file.image.defaultFileName}") String imageDefaultFileName) {
		this.fileHome = fileHome;
		this.userImageHome = fileHome + "program_data/user_image/";
		this.salesHome = fileHome + "files/sales_report/";
		this.receiptHome = fileHome + "files/receipt/";
		this.generatePurchasesHome = fileHome + "files/generate_purchase/";
		this.inventoryHome = fileHome + "files/inventory/";
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

	public String getReceiptHome() {
		return receiptHome;
	}

	public String getGeneratePurchasesHome() {
		return generatePurchasesHome;
	}
	
	public String getInventoryHome() {
		return inventoryHome;
	}
	
	public String getImageDefaultFileName() {
		return imageDefaultFileName;
	}
}
