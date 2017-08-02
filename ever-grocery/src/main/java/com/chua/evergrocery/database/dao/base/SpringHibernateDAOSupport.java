package com.chua.evergrocery.database.dao.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

/**
 * Class for supporting hibernate DAO with spring framework.
 * 
 * @version 1.0, Jan 22, 2016
 *
 */
public class SpringHibernateDAOSupport
		extends HibernateDaoSupport
{
	/**
	 * Construct a dao support. This will make the class not to create a new hibernate
	 * session.
	 */
	public SpringHibernateDAOSupport()
	{
		super();
	}	

	@Override
	protected HibernateTemplate createHibernateTemplate(SessionFactory sessionFactory)
	{
		final HibernateTemplate hibernateTemplate = super.createHibernateTemplate(sessionFactory);
		return hibernateTemplate;
	}

	/**
	 * Get the hibernate session.
	 * 
	 * @return the hibernate session.
	 */
	public Session getHibernateSession()
	{
		return currentSession();
	}

	/**
	 * Release a hibernate session.
	 * 
	 * @param session the session to release.
	 */
	public void releaseHibernateSession(Session session)
	{
		//super.releaseSession(session);
	}
}