package com.chua.evergrocery.database.entity.base;

import java.io.Serializable;

/**
 * @version 1.0, Jan 18, 2016
 */
public interface BaseID<T extends Serializable>
		extends IEntity<T>
{
	/**
	 * Get the id of the entity
	 * 
	 * @return the id of the entity
	 */
	public T getId();

	/**
	 * Set the id of the entity
	 * 
	 * @param id the id to be set.
	 */
	public void setId(T id);
}
