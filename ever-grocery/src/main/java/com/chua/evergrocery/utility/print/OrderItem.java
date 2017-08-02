package com.chua.evergrocery.utility.print;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin Roy K. Chua
 *
 * @version 1.0, Jul 9, 2016
 * @since 1.0, Jul 9, 2016
 *
 */
public class OrderItem
{
	private String itemName;
	private String unit;
	private Float totalPrice;
	private Float quantity;
	
	private List<String> nextLineList;
	private Boolean breakHere = Boolean.FALSE;
	
	public OrderItem(String itemName, String unit, Float totalPrice, Float quantity)
	{
		this.itemName = itemName;
		this.unit = unit;
		this.totalPrice = totalPrice;
		this.quantity = quantity;
	}
	
	public OrderItem(String itemName, String unit, Float totalPrice, Float quantity, Boolean breakHere)
	{
		this(itemName, unit, totalPrice, quantity);
		this.breakHere = breakHere;
	}
	
	public String getName()
	{	
		String tmp = "";
		
		tmp += unit + " " + itemName;		
		
		int length = tmp.length(), index = 25;
		nextLineList = new ArrayList<String> ();
		
		if(length > 25)
		{			
			try {
				while(length > index) {
					nextLineList.add(tmp.substring(index, 25 + index) + "\n");
					index += 25;
				}
			} catch(Exception e) {				
				nextLineList.add(tmp.substring(index) + "\n");
			}
			tmp = tmp.substring(0, 25);			
		}
		else
		{
			String.format("%-25s", tmp);
		}
		
		return tmp;
		
		/*String tmp = this.item.getImhDesc();
		tmp = tmp.trim();
		
		if(tmp.length() > 20)
		{
			tmp = tmp.substring(0, 17) + "...";			
		}
		else
		{
			while(tmp.length() < 20)
			{
				tmp += " ";
			}
		}
		
		return tmp;*/
	}
	
	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Float getTotalPrice() {
		return totalPrice;
	}	
	
	public String getFormattedTotalPrice() {
		final String formattedTotalPrice;
		
		if(totalPrice != totalPrice.intValue()) {
			final DecimalFormat df = new DecimalFormat("#.##");
			formattedTotalPrice = df.format(totalPrice);
		} else {
			formattedTotalPrice = totalPrice.intValue() + "";
		}
		
		return formattedTotalPrice;
	}
		
	public String getTransaction()
	{
		String transaction = "";
		
		final String qty;
		
		if(quantity == quantity.intValue()) {
			qty = String.format("%-4d", quantity.intValue());
		} else {
			qty = String.format("%-4s", quantity);
		}
			
		transaction = qty + String.format("%-25s", getName()) + " " + this.getFormattedTotalPrice() + "\n";
		for(String nextLine : nextLineList)
		{
			transaction += "    " + nextLine;
		}
		
		if(breakHere)
		{
			transaction += "\n\n";
		}
		return transaction;
	}
	
	/**
	 * 
	 * @param breakHere the break control to separate lab from imaging tests
	 */
	public void setBreakHere(Boolean breakHere)
	{
		this.breakHere = breakHere;
	}
}