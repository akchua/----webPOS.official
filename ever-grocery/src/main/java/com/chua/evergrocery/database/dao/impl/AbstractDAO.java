package com.chua.evergrocery.database.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import com.chua.evergrocery.database.dao.DAO;
import com.chua.evergrocery.database.dao.base.SpringHibernateDAOSupport;
import com.chua.evergrocery.database.entity.base.BaseID;
import com.chua.evergrocery.objects.Assertion;
import com.chua.evergrocery.objects.ObjectList;

/**
 * This class provides the basic functionality of the hibernate. This class is intended to be
 * extended and pass the persistent class as a generic parameter.
 * 
 * @param <T> The persistent class (the Class object is retrieved in the constructor).
 * @param <ID> The type of the id of the persistent class. As rule of hibernate the id of a class must
 * implement {@link Serializable}.
 * 
 * @see DAO
 * @see com.chua.evergrocery.database.dao.base.AbstractDAO
 * @see com.chua.evergrocery.database.entity.base.BaseID
 * @see java.io.Serializable
 */
public abstract class AbstractDAO<T extends BaseID<ID>, ID extends Serializable>
		extends com.chua.evergrocery.database.dao.base.AbstractDAO<T, ID>
		implements DAO<T, ID>
{
	/** The logger for this class. */
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AbstractDAO.class);
	
	/** The dao support. */
	private final SpringHibernateDAOSupport daoSupport = new SpringHibernateDAOSupport();
	
	/**
	 * the only constructor
	 * @param orders the default order
	 */
	protected AbstractDAO(Order ... orders)
	{
		super(orders);
	}
	
	/**
	 * Get the session factory set.
	 * @return the session factory
	 */
	public final SessionFactory getSessionFactory()
	{
		return daoSupport.getSessionFactory();
	}

	/**
	 * Set the session factory.
	 * @param sessionFactory the session factory.
	 */
	@Resource(name = "sessionFactory")
	public final void setSessionFactory(SessionFactory sessionFactory)
	{
		daoSupport.setSessionFactory(sessionFactory);
	}
	
	@Override
	protected Session getSession()
	{
		return daoSupport.getHibernateSession();
	}

	@Override
	protected void releaseSession(Session session)
	{
		daoSupport.releaseHibernateSession(session);
	}

	/**
	 * Commit the transaction
	 * 
	 * @param tran
	 */
	protected static void commitTransaction(Transaction tran)
	{
		if (tran != null)
		{
			tran.commit();
		}
	}

	/**
	 * Rollback the transaction
	 * 
	 * @param tran
	 */
	protected static void rollbackTransaction(Transaction tran)
	{
		if (tran != null)
		{
			try
			{
				tran.rollback();
			}
			catch (HibernateException e)
			{
				logger.error("Exception occurred while rollbacking transaction");
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Throws a illegal argument exception when the instance is <code>null</code>.
	 * 
	 * @param instance the instance to check
	 * @param message the message for error.
	 * 
	 * @throws IllegalArgumentException
	 */
	protected final void assertNotNull(Object instance, String message) throws IllegalArgumentException
	{
		try
		{
			Assertion.assertNotNull(instance, message);
		}
		catch (NullPointerException e) // the assersion always throws NPE. we wrap it.
		{
			throw new IllegalArgumentException(message, e);
		}
	}
	
	@Override
	public List<T> findAllList()
	{
		return findAllByCriterionList(null, null, null, null, Restrictions.eq("isValid", Boolean.TRUE));
	}
	
	@Override
	public ObjectList<T> findAll()
	{
		return findAllByCriterion(null, null, null, null, Restrictions.eq("isValid", Boolean.TRUE));
	}
	
	/*@Override
	public boolean delete(ID id)
	{
		final StringBuilder deleteHQL = new StringBuilder(100);
		
		deleteHQL.append("update ").append(className).append(" set isValid = 0 where id = :id");
		final String hql = deleteHQL.toString();
		
		if (logger.isDebugEnabled()) logger.debug("Delete HQL: " + hql);

		boolean ret = false;
		Transaction tran = null;
		Session session = null;
		try
		{
			session = getSession();
			tran = session.beginTransaction();

			Query query = session.createQuery(hql);
			query.setParameter("id", id);
			
			int totalUpdated = query.executeUpdate();

			session.flush();
			commitTransaction(tran);
			
			ret = totalUpdated > 0;
			
			if (logger.isDebugEnabled()) logger.debug("Total deleted: " + totalUpdated);
		}
		catch (HibernateException e)
		{
			rollbackTransaction(tran);
			logger.error(e.getMessage(), e);
		}
		finally
		{
			releaseSession(session);
		}
		return ret;
	}*/
	
	@Override
	public void evict(T object)
	{
		Session session = null;
		
		try {
			session = getSession();
			session.evict(object);
		} catch(HibernateException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void setReadOnly(T object, boolean readOnly)
	{
		if(object != null)
		{
			Session session = null;
			
			try {
				session = getSession();
				session.setReadOnly(object, readOnly);
			} catch(HibernateException e) {
				logger.error(e.getMessage(), e);
			}	
		}
	}
}