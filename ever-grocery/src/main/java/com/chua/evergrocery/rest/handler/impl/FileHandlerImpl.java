package com.chua.evergrocery.rest.handler.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.rest.handler.FileHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 22, 2018
 */
@Component
public class FileHandlerImpl implements FileHandler {

	@Autowired
	private FileConstants fileConstants;
	
	@Override
	public File findGeneratedPurchaseByFileName(String fileName) {
		return new File(fileConstants.getGeneratePurchasesHome() + fileName);
	}

	@Override
	public File findInventoryByFileName(String fileName) {
		return new File(fileConstants.getInventoryHome() + fileName);
	}
	
	@Override
	public File findSalesReportByFileName(String fileName) {
		return new File(fileConstants.getSalesHome() + fileName);
	}

	@Override
	public File findBackendReportByFileName(String fileName) {
		return new File(fileConstants.getBackendReportHome() + fileName);
	}
}
