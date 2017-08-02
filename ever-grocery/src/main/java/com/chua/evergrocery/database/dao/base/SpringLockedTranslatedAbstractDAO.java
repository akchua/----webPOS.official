package com.chua.evergrocery.database.dao.base;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.base.IEntity;

/**
 * A locked, translated DAO that uses spring DAO support to open and release a hibernate session. 
 * 
 * @param <T> The persistent class type.
 * @param <ID> The type of the id of the persistent class. As rule of hibernate the id of
 *          a class must implement {@link Serializable}.
 * 
 * @version 1.0, Jan 22, 2016

 */
public abstract class SpringLockedTranslatedAbstractDAO<T extends IEntity<ID>, ID extends Serializable>
		extends LockedTranslatedAbstractDAO<T, ID>
{
	/** The dao support for setting of session factory. */
	private final SpringHibernateDAOSupport support = new SpringHibernateDAOSupport();
	
	/**
	 * @param clazz
	 * @param orders
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public SpringLockedTranslatedAbstractDAO(Class<T> clazz, Order... orders)
			throws NullPointerException, IllegalArgumentException
	{
		super(clazz, orders);
	}

	/**
	 * @param orders
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public SpringLockedTranslatedAbstractDAO(Order... orders)
			throws NullPointerException, IllegalArgumentException
	{
		super(orders);
	}
	
	/**
	 * Get the session factory set.
	 * @return the session factory
	 */
	public final SessionFactory getSessionFactory()
	{
		return support.getSessionFactory();
	}

	/**
	 * Set the session factory.
	 * @param sessionFactory the session factory.
	 */
	public final void setSessionFactory(SessionFactory sessionFactory)
	{
		support.setSessionFactory(sessionFactory);
	}

	@Override
	protected Session getSession()
	{
		return support.getHibernateSession();
	}

	@Override
	protected void releaseSession(Session session)
	{
		if (session == null) return;

		support.releaseHibernateSession(session);
	}
	
	
}