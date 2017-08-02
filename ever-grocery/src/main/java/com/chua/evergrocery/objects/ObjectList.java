package com.chua.evergrocery.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A wrapper to a list that contains the total items. This object is created for paging
 * purpose since the item of a list will contain fewer than the actual total number of
 * items. The total items can be retrieved using the {@link #getTotal()}.
 * 
 * <p>
 * This object can be iterated by the for-each loop and can be iterated by the
 * {@link Iterator}.
 * </p>
 * 
 * <p>
 * <b>Note</b>: The total in this object is a field and must be set by the methods that
 * returns this type. The total can be set by {@link #setTotal(int)}, and retrieved by
 * {@link #getTotal()}.
 * </p>
 * 
 * @param <T> the type of the item inside the list.
 * 
 * @version 1.0, Jan 22, 2016
 */
public class ObjectList<T>
		implements Iterable<T>
{
	/**
	 * The total number of items. Usually this will be the total number of items in the
	 * database.
	 */
	private int total;

	/**
	 * The list containing the result items. The size of this list can be lesser than the
	 * {@link #total} value.
	 */
	private List<T> list;

	/**
	 * Construct a empty list.
	 */
	public ObjectList()
	{
	}

	/**
	 * Construct a new list by copying the content of another list.
	 * 
	 * @param list
	 */
	public ObjectList(ObjectList<T> list)
	{
		setList(list.getList());
		setTotal(list.getTotal());
	}

	/**
	 * Set the list.
	 * 
	 * @param list
	 */
	public void setList(List<T> list)
	{
		this.list = list;
	}

	/**
	 * Set the total
	 * 
	 * @param total
	 */
	public void setTotal(int total)
	{
		this.total = total;
	}

	/**
	 * Get the list, this method is assured to return a list and will not return a
	 * <code>null</code> value.
	 * 
	 * @return the list.
	 */
	public List<T> getList()
	{
		if (list == null)
		{
			list = new ArrayList<T>(5);
		}
		return list;
	}

	/**
	 * Get the total number of items. The size of the actual list may be smaller than the
	 * size returned by this method.
	 * 
	 * @return the total size.
	 */
	public int getTotal()
	{
		return total;
	}

	/**
	 * Get the actual size of the <b>list</b>.
	 * 
	 * @return the actual size of the list.
	 */
	public int getSize()
	{
		return getList().size();
	}

	@Override
	public Iterator<T> iterator()
	{
		return getList().iterator();
	}

	/**
	 * Get the first content.
	 * 
	 * @return the first content. <code>null</code> when there are no content or the list
	 *         is empty.
	 */
	public T uniqueResult()
	{
		return getUniqueResult(getList());
	}

	@Override
	public String toString()
	{
		String strlist = (list != null) ? list.toString() : "[]";

		StringBuilder sb = new StringBuilder(100 + strlist.length());

		sb.append("Total: " + getTotal()).append(", Size: " + getSize()).append(", List: ").append(
				strlist);

		return sb.toString();
	}

	/**
	 * Get the first item (index 0) of the list. This will return <code>null</code> when
	 * the list given is null; or the list has the size of 0. This method will not produce
	 * or throw a exception when the list have more than 1 items.
	 * 
	 * @param <E> the type of the items inside the list.
	 * @param list the list to retrieve the first item.
	 * @return the first item. <code>null</code> when the list is null or empty.
	 */
	public static <E> E getUniqueResult(List<E> list)
	{
		E ret = null;

		if (list != null && !list.isEmpty())
		{
			ret = list.get(0);
		}

		return ret;
	}
}
