package com.chua.evergrocery.database.entity.base;

import java.util.Date;

import org.joda.time.DateTime;

/**
 * @version 1.0, Jan 18, 2016
 */
public interface IBaseUpdatable extends BaseID<Long>
{

	/**
	 * Get the last date when the entity is updated
	 * 
	 * @return last updated date
	 */
	public Date getUpdatedOn();

	/**
	 * Get the created date for the entity, a new entity will return the current date and
	 * time.
	 * 
	 * @return the date created. (will not return a null, but is not thread safe)
	 */
	public DateTime getCreatedOn();

	/**
	 * Set the last updated date.
	 * 
	 * @param updatedOn a instance of the date, recommended a not null value
	 */
	public void setUpdatedOn(Date updatedOn);

	/**
	 * Set the date created
	 * 
	 * @param createdOn the date created
	 */
	public void setCreatedOn(DateTime createdOn);

}