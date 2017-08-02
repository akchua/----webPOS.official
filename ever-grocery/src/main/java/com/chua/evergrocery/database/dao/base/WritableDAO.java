package com.chua.evergrocery.database.dao.base;

import java.io.Serializable;
import java.util.Collection;

import com.chua.evergrocery.database.entity.base.IEntity;

/**
 * A DAO interface that contains methods for writing data only.
 * 
 * @param <T> the persistent class, the class must implement {@link IEntity}
 * @param <ID> the type of the primary key of the class, the class must implement
 *          {@link Serializable}
 * 
 * @version 1.0, Jan 22, 2016
 */
public interface WritableDAO<T extends IEntity<ID>, ID extends Serializable>
{

	/**
	 * Inserts an object into out database table
	 * 
	 * @param entry the object to be inserted into our database
	 * @return the primary key of the entry that is inserted
	 * @throws NullPointerException when entry is null
	 * @throws RuntimeException
	 */
	public ID insert(T entry) throws NullPointerException, RuntimeException;

	/**
	 * Updates a record in our database. The record to be updated is located based on the
	 * primary key of the given object.
	 * 
	 * @param entry the object to be updated into our database. The object must be loaded
	 *          using the {@link ReadableDAO#get(Serializable)}, {@link ReadableDAO#load}
	 *          or any object that has been retrieved from the database
	 * @throws NullPointerException when entry is null
	 * @throws RuntimeException
	 * @return true if success
	 */
	public boolean update(T entry) throws NullPointerException, RuntimeException;

	/**
	 * Insert objects by batch
	 * 
	 * @param objs objects to be inserted
	 * 
	 * @throws RuntimeException
	 */
	public void batchInsert(Collection<T> objs) throws RuntimeException;

	/**
	 * update records by batch
	 * 
	 * @param objs objects to be updated
	 * @throws RuntimeException
	 */
	public void batchUpdate(Collection<T> objs) throws RuntimeException;

	/**
	 * Insert objects by batch (varargs style)
	 * 
	 * @param objs objects to be inserted
	 * 
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public void batchInsert(T... objs) throws RuntimeException;

	/**
	 * update records by batch (varargs style)
	 * 
	 * @param objs objects to be updated
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public void batchUpdate(T... objs) throws RuntimeException;

}