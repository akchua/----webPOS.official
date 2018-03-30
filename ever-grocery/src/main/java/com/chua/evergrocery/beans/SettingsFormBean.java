package com.chua.evergrocery.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 30, 2018
 */
public class SettingsFormBean extends FormBean {

	private Integer itemsPerPage;
	
	public SettingsFormBean() {
		
	}
	
	public SettingsFormBean(Integer itemsPerPage) {
		setItemsPerPage(itemsPerPage);
	}

	public Integer getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
}
