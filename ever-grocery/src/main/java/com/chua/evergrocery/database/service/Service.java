package com.chua.evergrocery.database.service;

import java.io.Serializable;

import com.chua.evergrocery.database.entity.base.BaseID;
import com.chua.evergrocery.database.prototype.Prototype;

/**
 * All service classes will implement this interface
 *
 * @param <T> the persistent class, the class must implement {@link BaseID}
 * @param <ID> the type of the primary key of the class, the class must implement
 *          {@link Serializable}
 * 
 * @version 1.0 Jan 23, 2016
 */
public interface Service<T extends BaseID<ID>, ID extends Serializable>
		extends Prototype<T, ID>
{
	/**
	 * Reload the item. This method must not be called in outsite scope (servlet, actions, dwr, etc...).
	 * 
	 * @param id the item to reload.
	 * @return the item reloaded.
	 * 
	 * @throws NullPointerException
	 */
	T reload(ID id) throws NullPointerException;

	/**
	 * Reload the item. This method must not be called in outside scope (servlet, actions, DWR, etc...).
	 * 
	 * @param id the item to reload.
	 * @param readonly make the return item read-only. (hibernate will not persist).
	 * @return the item reloaded.
	 * 
	 * @throws NullPointerException
	 */
	T reload(ID id, boolean readonly) throws NullPointerException;
	
	/**
	 * create and return a new instance of the entity
	 * 
	 * @return null, if not successful (see log for details)
	 */
	T getItemInstance();
	
	/**
	 * Get the class of the DAO it handles
	 * 
	 * @return the entity class.
	 */
	Class<T> getEntityClass();
	
	/**
	 * Sets the valid flag to 0 so that the record will not be retrieved anymore
	 * @param obj the object to be invalidated in our database based on the primary key
	 * @return <code>true</code> when the entity is successfully deleted.
	 */
	boolean delete(T obj);
}