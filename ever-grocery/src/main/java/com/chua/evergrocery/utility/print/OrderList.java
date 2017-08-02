package com.chua.evergrocery.utility.print;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;

public class OrderList implements Printable {

	public static final int LINELENGTH = 36;
	private String orderNumber;
	
	private String creator;
	private List<OrderItem> orderItems;
	
	@Override
	public void print(VelocityEngine velocityEngine) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("orderList", this);
		//String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "orderList.vm", "UTF-8", model);
		///////////////////////////////////////////TEMP
		String text = "";
		text += this.getFormattedOrderNumber();
		text += "==========================================\n";
		text += "Qty Item(s)                   Amount\n";
		for(OrderItem orderItem: orderItems) {
			text += orderItem.getTransaction();
		}
		text += "\n";
		text += "==========================================\n";
		text += "Prepared by : " + getCreator();
		///////////////////////////////////////////TEMP
		Printer printer = new Printer();
		printer.print(text, getOrderNumber(), "Order List " + getOrderNumber());
	}
	
	public OrderList(String creator, String orderNumber, List<OrderItem> orderItems) {
		this.creator = creator;
		this.orderNumber = orderNumber;
		this.orderItems = orderItems;
	}
	
	public String getFormattedOrderNumber() {
		try {
			String formattedOrderNumber = "Order #  : " + orderNumber + "\n";
			return formattedOrderNumber;
		} catch(Exception e) {
			return "";
		}
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public List<OrderItem> getOrderItems()
	{	
		return orderItems;
	}
	
	public void setOrderItems(List<OrderItem> orderItems)
	{
		this.orderItems = orderItems;
	}
}
