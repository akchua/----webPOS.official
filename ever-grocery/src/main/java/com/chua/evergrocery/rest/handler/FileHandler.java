package com.chua.evergrocery.rest.handler;

import java.io.File;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 22, 2018
 */
public interface FileHandler {

	File findGeneratedPurchaseByFileName(String fileName);
	
	File findInventoryByFileName(String fileName);
}
