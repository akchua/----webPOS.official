package com.chua.evergrocery.utility.print;

/**
 * @author Kevin Roy K. Chua
 *
 * @version 1.0, Jul 9, 2016
 * @since 1.0, Jul 9, 2016
 *
 */
public class OrderReceiptConfig
{
	private String title;
	
	public OrderReceiptConfig() {}
	
	public OrderReceiptConfig(String title) 
	{
		setTitle(title);
	}
	
	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
}
