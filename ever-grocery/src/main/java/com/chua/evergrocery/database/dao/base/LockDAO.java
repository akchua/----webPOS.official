package com.chua.evergrocery.database.dao.base;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A lock DAO supports locking mechanism. This class is not a sub-interface of DAO, but is recommended
 * for the implementation to also implement DAO
 * 
 * @version 1.0, Jan 22, 2016
 *
 * @see DAO
 */
public interface LockDAO
{
	/**
	 * Get the read write lock.
	 * 
	 * @return a instance of {@link ReentrantReadWriteLock}.
	 */
	public ReentrantReadWriteLock getReadWriteLock();
	
	/**
	 * Get the read lock
	 * 
	 * @return intance of read Lock 
	 */
	public Lock getReadLock();
	
	/**
	 * Get the write lock
	 * 
	 * @return instance of write Lock
	 */
	public Lock getWriteLock();
	
	/**
	 * Get the reentrant lock.
	 * 
	 * @return instance of ReentrantLock from DAOImpl
	 */
	public Lock getReentrantLock();
}