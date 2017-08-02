package com.chua.evergrocery.database.dao.base;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.transform.ResultTransformer;

import com.chua.evergrocery.exceptions.DatabaseException;
import com.chua.evergrocery.objects.ObjectList;

/**
 * A interface to support a DAO implementation. The methods declared in this interface
 * will not be visible in {@link DAO}.
 * 
 * @version 1.0, Jan 22, 2016
 * 
 * @param <T> the type of the persistent class.
 * 
 * @see LockedTranslatedAbstractDAO
 * @see DAO
 */
public interface DAOSupport<T>
{
	/**
	 * Count all item by Hibernate Criterions. This method must use database count.
	 * 
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param criterions criteria that must match
	 * 
	 * @return the total items (in integer).
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public int countAllByCriterion(String associationPaths[], String aliasNames[],
			int joinTypes[], Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;
	
	/**
	 * Find all item by Hibernate Criterions.
	 * 
	 * @param page 0 base page number
	 * @param maxResults maximum number of items to be retrive
	 * @param associatedPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders order of the items
	 * @param criterions criteria that must match
	 * 
	 * @return list of the matched items in a list.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public List<T> findAllByCriterionList(int page, int maxResults, String[] associatedPaths,
			String[] aliasNames, int[] joinTypes, Order[] orders, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Find all item by Hibernate Criterions.
	 * 
	 * @param associatedPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders order of the items
	 * @param criterions criterias that must match
	 * 
	 * @return list of the matched items in a list.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public List<T> findAllByCriterionList(String associatedPaths[], String aliasNames[],
			int joinTypes[], Order[] orders, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;
	
	/**
	 * Get the first item of the result.
	 * 
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param criterions criteria that must match
	 * 
	 * @return the first item or <code>null</code>.
	 * 
	 * @throws DatabaseException
	 * @throws HibernateException
	 * @throws IllegalArgumentException
	 */
	public T findUniqueResult(String associationPaths[], String aliasNames[],
			int joinTypes[], Criterion... criterions) 
			throws DatabaseException, HibernateException, IllegalArgumentException;

	
	/**
	 * Get the first item of the result.
	 * 
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders order of the items
	 * @param criterions criteria that must match
	 * 
	 * @return the first item or <code>null</code>.
	 * 
	 * @throws DatabaseException
	 * @throws HibernateException
	 * @throws IllegalArgumentException
	 */
	public T findUniqueResult(String associationPaths[], String aliasNames[],
			int joinTypes[], Order[] orders, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Find all item by Hibernate Criterions.
	 * 
	 * @param <E> the return type for the list.
	 * 
	 * @param clazz the class to query
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders order of the items
	 * @param criterions criteria that must match
	 * 
	 * @return list of the matched items in a list.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public <E> List<E> findAllByCriterionList(final Class<E> clazz, String associationPaths[],
			String aliasNames[], int joinTypes[], Order[] orders, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Find all item by Hibernate Criterions.
	 * 
	 * @param page 0 base page number
	 * @param maxResults maximum number of items to be retrive
	 * @param associatedPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders order of the items
	 * @param criterions criteria that must match
	 * 
	 * @return list of the items in a ObjectList. This method must return a <b>not-null</b> list but can be empty.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public ObjectList<T> findAllByCriterion(int page, int maxResults, String[] associatedPaths,
			String[] aliasNames, int[] joinTypes, Order[] orders, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Find all item by Hibernate Criterions.
	 * 
	 * @param countTotal <code>true</code> if this method will count the total result.
	 * @param page 0 base page number
	 * @param maxResults maximum number of items to be retrive
	 * @param associatedPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders order of the items
	 * @param criterions criteria that must match
	 * 
	 * @return list of the items in a ObjectList. This method must return a <b>not-null</b> list but can be empty.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public ObjectList<T> findAllByCriterion(boolean countTotal, int page, int maxResults, String[] associatedPaths,
			String[] aliasNames, int[] joinTypes, Order[] orders, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Find all item by Hibernate Criterions.
	 * 
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders order of the items
	 * @param criterions criteria that must match
	 * 
	 * @return list of the items in a ObjectList. This method must return a <b>not-null</b> list but can be empty.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public ObjectList<T> findAllByCriterion(String associationPaths[], String aliasNames[],
			int joinTypes[], Order[] orders, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Find all item by Hibernate Criterions.
	 * 
	 * @param countTotal if we will need the total number of items.
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders order of the items
	 * @param criterions criteria that must match
	 * 
	 * @return list of the items in a ObjectList. This method must return a <b>not-null</b> list but can be empty.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public ObjectList<T> findAllByCriterion(boolean countTotal, String associationPaths[],
			String aliasNames[], int joinTypes[], Order[] orders, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Find all item by Hibernate Criterions.
	 * 
	 * @param <E> the return type for the list.
	 * 
	 * @param clazz the persistent class
	 *          used.
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders order of the items
	 * @param criterions criteria that must match
	 * 
	 * @return list of the items in a ObjectList. This method must return a <b>not-null</b> list but can be empty.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public <E> ObjectList<E> findAllByCriterion(Class<E> clazz, String associationPaths[],
			String aliasNames[], int joinTypes[], Order[] orders, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * 
	 * Find all item by Hibernate Criterions.
	 * 
	 * <p>
	 * <b>Note</b>: This method do not page. The computation of total pages depends on the
	 * value of the parameter <em>doGetTotalPages</em>. The result will be sorted depends
	 * on the <em>orders</em> parameter, when <code>null</code> there will be no sorting.
	 * </p>
	 * 
	 * @param <E> the return type for the list.
	 * 
	 * @param clazz the persistent class
	 * @param countTotal if we will need the total number of items.
	 * @param associatedPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders order of the items
	 * @param criterions criteria that must match
	 * 
	 * @return list of the items in a ObjectList. This method must return a <b>not-null</b> list but can be empty.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public <E> ObjectList<E> findAllByCriterion(final Class<E> clazz, final boolean countTotal,
			final String[] associatedPaths, final String[] aliasNames, final int[] joinTypes,
			final Order[] orders, final Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Find all item by Hibernate Criterions.
	 * 
	 * @param <E> the return type for the list.
	 * 
	 * @param clazz the persistent class
	 * @param countTotal if we will need the total number of items.
	 * @param hasPaging there will be paging, and the parameter page and maxResults will be
	 *          used.
	 * @param page 0 base page number
	 * @param maxResults maximum number of items to be retrive
	 * @param associatedPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias name of the associatedPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders order of the items
	 * @param criterions criterias that must match
	 * 
	 * @return list of the items in a ObjectList. This method must return a <b>not-null</b> list but can be empty.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 * 
	 * @see org.hibernate.Criteria
	 * @see org.hibernate.criterion.CriteriaSpecification
	 * @see org.hibernate.criterion.Criterion
	 * @see org.hibernate.criterion.Order
	 * @see org.hibernate.criterion.Restrictions
	 */
	public <E> ObjectList<E> findAllByCriterion(Class<E> clazz, boolean countTotal,
			boolean hasPaging, int page, int maxResults, String[] associatedPaths, String[] aliasNames,
			int[] joinTypes, Order[] orders, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;
	
	/**
	 * <p>
	 * Query using Hibernate Criterions in projection mode.
	 * </p>
	 * 
	 * <p>
	 * <b>Note</b>: This method do not page, does not compute total, and order the result.
	 * </p>
	 * 
	 * @param projection the hibernate projection
	 * @param criterions criteria that must match
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 * 
	 * @return list of the items depends on the projection.
	 */
	public List<Object> findAllByCriterionProjection(Projection projection, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * <p>
	 * Query using Hibernate Criterions in projection mode.
	 * </p>
	 * 
	 * <p>
	 * <b>Note</b>: This method do not page, does not compute total, and order the result.
	 * </p>
	 * 
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias of the join entity. Must have the same length with
	 *          associationPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param projection the hibernate projection
	 * @param criterions criteria that must match
	 * 
	 * @return list of the items depends on the projection.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public List<Object> findAllByCriterionProjection(String associationPaths[], String aliasNames[],
			int joinTypes[], Projection projection, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Query using Hibernate Criterions in projection mode.
	 * 
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias of the join entity. Must have the same length with
	 *          associationPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders array of hibernate order.
	 * @param projection the hibernate projection
	 * @param criterions criteria that must match
	 * 
	 * @return list of the items depends on the projection.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public List<Object> findAllByCriterionProjection(String[] associationPaths, String[] aliasNames,
			int[] joinTypes, Order[] orders, Projection projection, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Query using Hibernate Criterions in projection mode.
	 * 
	 * <p>
	 * <b>Note</b>: This method do not page, does not compute total, and order the result.
	 * </p>
	 * 
	 * @param <Y> the type of the list to be returned.
	 * 
	 * @param nc The class expected to be returned in a list. For generic purpose.
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias of the join entity. Must have the same length with
	 *          associationPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param projection the hibernate projection
	 * @param resultTransformer the transformer for the result of the projection
	 * @param criterions criteria that must match
	 * 
	 * @return list of the items depends on the projection.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public <Y> List<Y> findAllByCriterionProjection(Class<Y> nc, String associationPaths[],
			String aliasNames[], int joinTypes[], Projection projection,
			ResultTransformer resultTransformer, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Query using Hibernate Criterions in projection mode.
	 * <p>
	 * <b>Note</b>: This method do not page, and does not compute total. But this will
	 * order the result by field.
	 * </p>
	 * 
	 * @param <Y> the type of the list to be returned.
	 * 
	 * @param nc The class expected to be returned in a list. For generic purpose.
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias of the join entity. Must have the same length with
	 *          associationPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders the order of the result
	 * @param projection the hibernate projection
	 * @param resultTransformer the transformer for the result of the projection
	 * @param criterions criteria that must match
	 * 
	 * @return list of the items depends on the projection.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public <Y> List<Y> findAllByCriterionProjection(Class<Y> nc, String associationPaths[],
			String aliasNames[], int joinTypes[], Order orders[], Projection projection,
			ResultTransformer resultTransformer, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Query using Hibernate Criterions in projection mode.
	 * 
	 * @param <Y> the type of the list to be returned.
	 * 
	 * @param nc The class expected to be returned in a list. For generic purpose.
	 * @param pageNumber 0 based page number. (valid value is from 0 and positive numbers
	 *          only)
	 * @param maxResults number of results to be retrieve. (valid value is positive number
	 *          excluding 0)
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias of the join entity. Must have the same length with
	 *          associationPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders the order of the result
	 * @param projection the hibernate projection
	 * @param resultTransformer the ransformer for the result of the projection
	 * @param criterions criterias that must match
	 * 
	 * @return list of the items depends on the projection.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public <Y> ObjectList<Y> findAllByCriterionProjection(Class<Y> nc, int pageNumber,
			int maxResults, String associationPaths[], String[] aliasNames, int joinTypes[],
			Order orders[], Projection projection, ResultTransformer resultTransformer,
			Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Query using Hibernate Criterions in projection mode.
	 * 
	 * @param <Y> the type of the list to be returned.
	 * 
	 * @param nc The class expected to be returned in a list. For generic purpose.
	 * @param hasPagination if to use the pagination method and compute the total. using the
	 *          pageNumber and maxResult parameter
	 * @param pageNumber 0 based page number. (valid value is from 0 and positive numbers
	 *          only)
	 * @param maxResults number of results to be retrieve. (valid value is positive number
	 *          excluding 0)
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias of the join entity. Must have the same length with
	 *          associationPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param hasOrder if we will order the result using the orders parameter
	 * @param orders the order of the result
	 * @param projection the hibernate projection
	 * @param resultTransformer the transformer for the result of the projection
	 * @param criterions criterias that must match
	 * 
	 * @return list of the items depends on the projection.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public <Y> ObjectList<Y> findAllByCriterionProjection(Class<Y> nc, boolean hasPagination,
			int pageNumber, int maxResults, String associationPaths[], String[] aliasNames,
			int joinTypes[], boolean hasOrder, Order orders[], Projection projection,
			ResultTransformer resultTransformer, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Query using Hibernate Criterions in projection mode.
	 * 
	 * @param <Y> the type of the list to be returned.
	 * 
	 * @param nc The class expected to be returned in a list. For generic purpose.
	 * @param pageNumber 0 based page number. (valid value is from 0 and positive numbers
	 *          only)
	 * @param maxResults number of results to be retrive. (valid value is positive number
	 *          excluding 0)
	 * @param doGetTotalPages if the method will compute the total number of items in
	 *          database (good for retrieving total number of result when there is paging)
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias of the join entity. Must have the same length with
	 *          associationPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders the order of the result
	 * @param projection the hibernate projection
	 * @param resultTransformer the transformer for the result of the projection
	 * @param criterions criterias that must match
	 * 
	 * @return list of the items depends on the projection.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public <Y> ObjectList<Y> findAllByCriterionProjection(Class<Y> nc, int pageNumber,
			int maxResults, boolean doGetTotalPages, String associationPaths[], String[] aliasNames,
			int joinTypes[], Order orders[], Projection projection, ResultTransformer resultTransformer,
			Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Query using Hibernate Criterions in projection mode.
	 * 
	 * @param <Y> the type of the list to be returned.
	 * 
	 * @param nc The class expected to be returned in a list. For generic purpose.
	 * @param hasPagination if to use the pagination method and compute the total. using the
	 *          pageNumber and maxResult parameter
	 * @param pageNumber 0 based page number. (valid value is from 0 and positive numbers
	 *          only)
	 * @param maxResults number of results to be retrive. (valid value is positive number
	 *          excluding 0)
	 * @param doGetTotalPages if the method will compute the total number of items in
	 *          database (good for retrieving total number of result when there is paging)
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias of the join entity. Must have the same length with
	 *          associationPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param hasOrder if we will order the result using the orders parameter
	 * @param orders the order of the result
	 * @param projection the hibernate projection
	 * @param resultTransformer the transformer for the result of the projection
	 * @param criterions criteria that must match
	 * 
	 * @return list of the items depends on the projection.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public <Y> ObjectList<Y> findAllByCriterionProjection(Class<Y> nc, boolean hasPagination,
			int pageNumber, int maxResults, boolean doGetTotalPages, String associationPaths[],
			String[] aliasNames, int joinTypes[], boolean hasOrder, Order orders[],
			Projection projection, ResultTransformer resultTransformer, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

	/**
	 * Query using Hibernate Criterions in projection mode.
	 * 
	 * <p>
	 * <b>Note</b>: This method do not page. The computation of total pages depends on the
	 * value of the parameter <em>doGetTotalPages</em>. The result will be sorted depends
	 * on the <em>orders</em> parameter, when <code>null</code> there will be no sorting.
	 * </p>
	 * 
	 * @param <Y> the type of the list to be returned.
	 * 
	 * @param nc The class expected to be returned in a list. For generic purpose.
	 * @param doGetTotalPages if the method will compute the total number of items in
	 *          database (good for retrieving total number of result when there is paging)
	 * @param associationPaths names of the join entity. Must have the same length with
	 *          joinType
	 * @param aliasNames the alias of the join entity. Must have the same length with
	 *          associationPaths
	 * @param joinTypes the type of join must be perform. Must have the same length with
	 *          associationPaths
	 * @param orders the order of the result
	 * @param projection the hibernate projection
	 * @param resultTransformer the transformer for the result of the projection
	 * @param criterions criterias that must match
	 * @return list of the items depends on the projection.
	 * 
	 * @throws IllegalArgumentException see the message for details.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public <Y> ObjectList<Y> findAllByCriterionProjection(Class<Y> nc, boolean doGetTotalPages,
			String associationPaths[], String[] aliasNames, int joinTypes[], Order orders[],
			Projection projection, ResultTransformer resultTransformer, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException;

}