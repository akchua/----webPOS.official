package com.chua.evergrocery.database.dao.base;

import java.io.Serializable;

import org.hibernate.HibernateException;

import com.chua.evergrocery.database.entity.base.IEntity;
import com.chua.evergrocery.exceptions.DatabaseException;

/**
 * A DAO interface that contains methods for reloading, refreshing data only.
 * 
 * @param <T> the persistent class, the class must implement {@link IEntity}
 * @param <ID> the type of the primary key of the class, the class must implement
 *          {@link Serializable}
 *          
 * @version 1.0, Jan 22, 2016
 */
public interface ReloadableDAO<T extends IEntity<ID>, ID extends Serializable>
{

	/**
	 * Reload the object instance. This method will try to remove the object instance from
	 * hibernate cache, and retrieve it.
	 * 
	 * <p>
	 * <b>Note:</b> This method will force to cast the object to {@link com.chua.evergrocery.entity.base.BaseID}. 
	 * </p>
	 * 
	 * @param entry the entry to reload.
	 * 
	 * @return the reloaded entry.
	 * 
	 * @throws HibernateException
	 * @throws DatabaseException
	 */
	public T reload(T entry) throws HibernateException, DatabaseException;

	/**
	 * Refresh the persistent instance.
	 * 
	 * @param objs the objects to be refreshed.
	 * 
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public void refresh(T... objs) throws RuntimeException;

}