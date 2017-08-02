package com.chua.evergrocery.database.dao.base;

import java.io.Serializable;

import com.chua.evergrocery.database.entity.base.IEntity;

/**
 * A DAO interface that contains methods for reading data only.
 * 
 * @param <T> the persistent class, the class must implement {@link IEntity}
 * @param <ID> the type of the primary key of the class, the class must implement
 *          {@link Serializable}
 *
 * @version 1.0, Jan 22, 2016
 */
public interface ReadableDAO<T extends IEntity<ID>, ID extends Serializable>
{

	/**
	 * Finds an object instance from our database table given the primary key for locating
	 * that object.
	 * 
	 * @param primaryKey the primary key for locating the record in our database.
	 * @return Object creates and returns the found Object. The implementatin should make
	 *         sure the correct subclass is instatiated
	 * @throws RuntimeException
	 * @see #get(Serializable, boolean)
	 */
	public T get(ID primaryKey) throws RuntimeException;

	/**
	 * Retrieve an object instance from our database table given the primary key for locating
	 * that object
	 * 
	 * @param primaryKey the primary key for locating the record in our database,
	 * @param readonly makes the instance in readonly and is not dirty checked.
	 * @return Object creates and returns the found Object. The implementatin should make
	 *         sure the correct subclass is instatiated
	 * @throws RuntimeException
	 * @see #get(Serializable)
	 */
	public T get(ID primaryKey, boolean readonly) throws RuntimeException;

	/**
	 * Retrieve an object instance from our database table given the primary key for locating
	 * that object.
	 * 
	 * @param <E> type to return
	 * @param clazz
	 * @param primaryKey
	 * @return instance of E
	 * @throws RuntimeException
	 */
	public <E> E get(Class<E> clazz, ID primaryKey) throws RuntimeException;

	/**
	 * Find the object instance from our database table given the primary key for locating
	 * that object.
	 * 
	 * <p> 
	 * This method may be overrided to change the behavoir of this method.
	 * </p> 
	 * 
	 * @param primaryKey the primary key for locating the record in our database,
	 * @return the proxy to the item.
	 * @throws RuntimeException
	 */
	public T find(ID primaryKey) throws RuntimeException;

	/**
	 * Load the object instance from our database table given the primary key for locating
	 * that object. You must ensure that the primaryKey exist before calling this method.
	 * 
	 * @param primaryKey the primary key for locating the record in our database,
	 * @return the proxy to the item.
	 * @throws RuntimeException
	 */
	public T load(ID primaryKey) throws RuntimeException;

}