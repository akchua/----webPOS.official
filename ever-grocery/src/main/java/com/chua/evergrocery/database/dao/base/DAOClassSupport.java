package com.chua.evergrocery.database.dao.base;

/**
 * This class is a support class for DAO to provide ways to retrieve the information for
 * the entity.
 * 
 * @param <T> the type of the persistent class.
 * 
 * @version 1.0, Jan 22, 2016
 * 
 */
public interface DAOClassSupport<T>
{
	/**
	 * Create and return a new instance of the entity
	 * 
	 * @return null, if not successful (see log for details)
	 */
	public T createNewItemInstance();

	/**
	 * Get the persistent class that this DAO handles
	 * 
	 * @return the class object of the persistent class
	 */
	public Class<T> getEntityClass();

	/**
	 * Get the entity name if the name provided is not null or the class name itself
	 * 
	 * @return the entity name
	 */
	public String getEntityName();

	/**
	 * Get the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName();
}