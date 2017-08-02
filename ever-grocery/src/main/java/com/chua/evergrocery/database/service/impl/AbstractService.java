package com.chua.evergrocery.database.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;

import com.chua.evergrocery.database.dao.DAO;
import com.chua.evergrocery.database.entity.base.BaseID;
import com.chua.evergrocery.database.service.Service;
import com.chua.evergrocery.exceptions.DatabaseException;
import com.chua.evergrocery.objects.ObjectList;

/**
 * We build a face for DAOs
 * 
 * @param <T> the persistent class, the class must implement {@link BaseID}
 * @param <ID> the type of the primary key of the class, the class must implement
 *          {@link Serializable}
 * @param <D> the DAO class.
 * 
 * @version 1.0 Jan 22, 2016
 */
public abstract class AbstractService<T extends BaseID<ID>, ID extends Serializable, D extends DAO<T, ID>>
		implements Service<T, ID>
{
	/** The DAO. */
	protected D dao;

	/**
	 * Construct the service by providing the DAO.
	 * 
	 * @param dao the DAO.
	 */
	protected AbstractService(D dao)
	{
		if (dao == null)
		{
			throw new NullPointerException("DAO is null!");
		}
		this.dao = dao;
	}
	
	@Override
	public T reload(ID id, boolean readonly) throws NullPointerException
	{
		return dao.get(id, readonly);
	}
	
	@Override
	public T reload(ID id) throws NullPointerException
	{
		return dao.get(id);
	}

	@Override
	public ID insert(T obj) throws NullPointerException
	{
		return dao.insert(obj);
	}

	@Override
	public boolean update(T obj) throws NullPointerException
	{
		return dao.update(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(T... objs)
	{
		dao.refresh(objs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void batchInsert(T... objs)
	{
		dao.batchInsert(Arrays.asList(objs));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void batchUpdate(T... objs)
	{
		dao.batchUpdate(Arrays.asList(objs));
	}

	@Override
	public void batchInsert(Collection<T> objs)
	{
		dao.batchInsert(objs);
	}

	@Override
	public void batchUpdate(Collection<T> objs)
	{
		dao.batchUpdate(objs);
	}

	@Override
	public T getItemInstance()
	{
		return dao.createNewItemInstance();
	}

	@Override
	public Class<T> getEntityClass()
	{
		return dao.getEntityClass();
	}

	@Override
	public T reload(T entry) throws HibernateException, DatabaseException
	{
		return dao.reload(entry);
	}

	@Override
	public T get(ID primaryKey)
	{
		return dao.get(primaryKey);
	}

	@Override
	public T get(ID primaryKey, boolean readonly)
	{
		return dao.get(primaryKey, readonly);
	}

	@Override
	public T load(ID primaryKey)
	{
		return dao.load(primaryKey);
	}

	@Override
	public boolean delete(ID id) throws RuntimeException
	{
		return dao.delete(id);
	}

	@Override
	public boolean erase(T entry) throws NullPointerException, RuntimeException
	{
		return dao.erase(entry);
	}

	@Override
	public <E> E get(Class<E> clazz, ID primaryKey) throws RuntimeException
	{
		return dao.get(clazz, primaryKey);
	}

	@Override
	public boolean delete(T obj)
	{
		return dao.delete(obj.getId());
	}

	@Override
	public List<T> findAllList()
	{
		return dao.findAllList();
	}
	
	@Override
	public ObjectList<T> findAll()
	{
		return dao.findAll();
	}

	@Override
	public T find(ID primaryKey)
	{
		return dao.find(primaryKey);
	}

	@Override
	public void batchErase(Collection<T> objs) throws RuntimeException
	{
		dao.batchErase(objs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void batchErase(T... objs) throws RuntimeException
	{
		dao.batchErase(objs);
	}
	
	@Override
	public void evict(T object)
	{
		dao.evict(object);
	}
	
	@Override
	public void setReadOnly(T object, boolean readOnly)
	{
		dao.setReadOnly(object, readOnly);
	}
}