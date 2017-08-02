package com.chua.evergrocery.objects;

import org.hibernate.criterion.Order;

/**
 * 
 * The order where how to arrange our retrieved data.
 * 
 * @version 1.0, Jan 22, 2016
 */
public enum SortOrder
{
	/**
	 * Ascending order
	 */
	asc
	{
		@Override
		public Order getOrder(String fieldName)
		{
			return Order.asc(fieldName);
		}
	},
	/**
	 * Descending order
	 */
	desc
	{
		@Override
		public Order getOrder(String fieldName)
		{
			return Order.desc(fieldName);
		}
	},
	/**
	 * Do not sort, this will use the default sort mechanism of the database.
	 */
	noSorting,
	
	/**
	 * Use the default sorting mechanism
	 */
	defaultValue;

	/**
	 * Parse the string to return {@link #asc} when the string matches "asc". And
	 * {@link #desc} when the string matches "desc" (case insensitive).
	 * 
	 * @param str the string to be parsed.
	 * @return a value ({@link #asc}, {@link #desc}, or {@link #defaultValue}
	 *         if not both asc and desc)
	 */
	public static SortOrder parseSortOrder(String str)
	{
		if ("asc".equalsIgnoreCase(str))
			return asc;
		else if ("desc".equalsIgnoreCase(str))
			return desc;
		else
			return defaultValue;
	}

	/**
	 * Get the hibernate equivalent order using the field name.
	 * 
	 * @param fieldName the field name
	 * @return a Order when the enumeration support it (override); <code>null</code> when the
	 *         value do not support this method (which is the default).
	 */
	public Order getOrder(String fieldName)
	{
		return null;
	}
}