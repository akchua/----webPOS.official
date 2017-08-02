package com.chua.evergrocery.database.dao.base;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.base.IEntity;
import com.chua.evergrocery.exceptions.DatabaseException;
import com.chua.evergrocery.exceptions.OutdatedVersionException;

/**
 * A subclass of {@link AbstractDAO} that translates the HibernateException to a
 * DatabaseException.
 * 
 * @param <T> The persistent class type.
 * @param <ID> The type of the id of the persistent class. As rule of hibernate the id of
 *          a class must implement {@link Serializable}.
 * 
 * @version 1.0, Jan 22, 2016
 * 
 */
public abstract class TranslatedAbstractDAO<T extends IEntity<ID>, ID extends Serializable>
		extends AbstractDAO<T, ID>
{

	/**
	 * @param clazz
	 * @param orders
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public TranslatedAbstractDAO(Class<T> clazz, Order... orders)
			throws NullPointerException, IllegalArgumentException
	{
		super(clazz, orders);
	}

	/**
	 * @param orders
	 */
	protected TranslatedAbstractDAO(Order... orders)
	{
		super(orders);
	}

	@Override
	protected void translateHibernateException(HibernateException e)
			throws DatabaseException, HibernateException
	{
		translateHibernateToDatabaseException(e);
	}

	/**
	 * Translate and re-throw a hibernate exception to a database exception
	 * 
	 * @param e the hibernate exception to translate
	 * @throws DatabaseException the converted database exception 
	 */
	public static final void translateHibernateToDatabaseException(HibernateException e) throws DatabaseException
	{
		throw wrapHibernateExceptionToDatabaseException(e);
	}

	/**
	 * Wraps the hibernate exception to a database exception.
	 * 
	 * @param e the hibernate exception to wrap
	 * @return the database exception.
	 */
	public static DatabaseException wrapHibernateExceptionToDatabaseException(HibernateException e)
	{
		return (e instanceof StaleStateException) ? new OutdatedVersionException(e) : 
			new DatabaseException(e);
	}

}