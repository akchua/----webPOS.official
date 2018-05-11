package com.chua.evergrocery.enums;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 11, 2018
 */
public enum DocType {

	PRINT("print"),
	
	DEFAULT("template");
	
	private final String folderName;
	
	DocType(final String folderName) {
		this.folderName = folderName;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getFolderName() {
		return folderName;
	}
}
