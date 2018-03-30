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
	
	private final String generatePurchasesHome;
	
	private final String inventoryHome;
	
	@Autowired
	public FileConstants(@Value("${file.home}") String fileHome) {
		this.fileHome = fileHome;
		this.generatePurchasesHome = fileHome + "files/generate_purchase/";
		this.inventoryHome = fileHome + "files/inventory/";
	}

	public String getFileHome() {
		return fileHome;
	}

	public String getGeneratePurchasesHome() {
		return generatePurchasesHome;
	}
	
	public String getInventoryHome() {
		return inventoryHome;
	}
}
