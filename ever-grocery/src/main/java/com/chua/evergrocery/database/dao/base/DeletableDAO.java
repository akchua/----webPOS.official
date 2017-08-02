package com.chua.evergrocery.database.dao.base;

import java.io.Serializable;
import java.util.Collection;

import com.chua.evergrocery.database.entity.base.IEntity;

/**
 * A DAO interface that is for deleting objects.
 * 
 * @version 1.0, Jan 22, 2016
 *
 * @param <T>
 * @param <ID>
 */
public interface DeletableDAO<T extends IEntity<ID>, ID extends Serializable>
{

	/**
	 * Deletes an object by id from the database.
	 * 
	 * @param id the id of the entity.
	 * @return true when succeed, false when failed.
	 * @throws RuntimeException
	 */
	public boolean delete(ID id) throws RuntimeException;

	/**
	 * Permanently delete a record from the database.
	 * 
	 * @param entry the entity to be deleted
	 * @return true if success.
	 * @throws NullPointerException when entry is null
	 * @throws RuntimeException
	 */
	public boolean erase(T entry) throws NullPointerException, RuntimeException;
	
	/**
	 * Permanently delete records by batch
	 * 
	 * @param objs objects to be deleted
	 * @throws RuntimeException
	 */
	public void batchErase(Collection<T> objs) throws RuntimeException;
	
	/**
	 * Permanently delete records by batch
	 * 
	 * @param objs objects to be deleted
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public void batchErase(T... objs) throws RuntimeException;

}