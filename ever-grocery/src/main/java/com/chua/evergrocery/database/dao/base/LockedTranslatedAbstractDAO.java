package com.chua.evergrocery.database.dao.base;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.base.BaseID;
import com.chua.evergrocery.database.entity.base.IEntity;

/**
 * This class provides the basic functionality of the hibernate. This class is intended
 * (not required) to be extended and pass the persistent class as a generic parameter.
 * 
 * <p>
 * This class can also be instantiated. But will require the class to be passed to the
 * constructor as parameter.
 * </p>
 * 
 * <p>
 * The methods of this class are locked by a read-write lock for data integrity.
 * </p>
 * 
 * @param <T> The persistent class type.
 * @param <ID> The type of the id of the persistent class. As rule of hibernate the id of
 *          a class must implement {@link Serializable}.
 * 
 * @version 1.0, Jan 22, 2016
 * 
 * @see DAO
 * @see DAOSupport
 * @see BaseID
 * @see java.io.Serializable
 * @see org.springframework.orm.hibernate3.support.HibernateDaoSupport
 * 
 */
public abstract class LockedTranslatedAbstractDAO<T extends IEntity<ID>, ID extends Serializable>
		extends TranslatedAbstractDAO<T, ID>
		implements DAO<T, ID>, DAOSupport<T>, LockDAO
{

	/** Lock that will be used in DAO's */
	protected final Lock reentrantLock = new ReentrantLock();

	/* fair read-write locks */
	/** The read write lock for thread synchronizations. */
	protected final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

	/** The read lock. */
	protected final Lock readLock = readWriteLock.readLock();

	/** The write lock. */
	protected final Lock writeLock = readWriteLock.writeLock();

//	/** The logger for this class. */
//	private static final Log log = LogFactory.getLog(LockedTranslatedAbstractDAO.class);

	/**
	 * 
	 * @param orders
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	protected LockedTranslatedAbstractDAO(Order... orders)
			throws NullPointerException, IllegalArgumentException
	{
		super(orders);
	}

	/**
	 * 
	 * @param clazz
	 * @param orders
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public LockedTranslatedAbstractDAO(Class<T> clazz, Order... orders)
			throws NullPointerException, IllegalArgumentException
	{
		super(clazz, orders);
	}

	@Override
	public Lock getReentrantLock()
	{
		return reentrantLock;
	}

	@Override
	public Lock getReadLock()
	{
		return readLock;
	}

	@Override
	public Lock getWriteLock()
	{
		return writeLock;
	}

	@Override
	public ReentrantReadWriteLock getReadWriteLock()
	{
		return readWriteLock;
	}

	@Override
	protected void prepareRead()
	{
		readLock.lock();
	}

	@Override
	protected void prepareWrite()
	{
		writeLock.lock();
	}

	@Override
	protected void afterRead()
	{
		readLock.unlock();
	}

	@Override
	protected void afterWrite()
	{
		writeLock.unlock();
	}

}