package com.chua.evergrocery.database.dao.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chua.evergrocery.database.entity.base.BaseID;
import com.chua.evergrocery.database.entity.base.IEntity;
import com.chua.evergrocery.exceptions.DatabaseException;
import com.chua.evergrocery.objects.Assertion;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.objects.SortOrder;
import com.chua.evergrocery.utility.ClassHelper;

/**
 * This class provides the basic functionality of the hibernate. This class is intended
 * (not required) to be extended and pass the persistent class as a generic parameter.
 * 
 * <p>
 * This class can also be instantiated. But will require the class to be passed to the
 * constructor as parameter.
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
 */
public abstract class AbstractDAO<T extends IEntity<ID>, ID extends Serializable>
		implements DAO<T, ID>, DAOSupport<T>
{
	/** The logger for this class. */
	private final Logger log = LoggerFactory.getLogger(AbstractDAO.class.getName() + " (" + getClass().getName() + ")");
	
	/**
	 * A empty order to create a criteria without any paticular order.
	 */
	public static final Order NO_SORTING[] = new Order[0];

	/**
	 * The class passed thru the generic parameter.
	 */
	protected final Class<T> clazz;

	/**
	 * Order in listing the items. Can be null value
	 */
	private final Order orders[];

	/**
	 * The entity name... for checking purpose
	 */
	protected final String entityName;

	/**
	 * The class name, for shortcut purpose... so that you will not call clazz.getName()
	 */
	protected final String className;

	/**
	 * 
	 * @param isClassGiven
	 * @param entityClazz
	 * @param orders
	 */
	private AbstractDAO(boolean isClassGiven, Class<T> entityClazz, Order ...orders)
	{
		if (isClassGiven && entityClazz != null)
		{
			this.clazz = entityClazz;
		}
		else if(!isClassGiven)
		{
			this.clazz = ClassHelper.cast(ClassHelper.getGenericParameter(getClass(), 0));
		}
		else
		{
			this.clazz = null;
		}

		Assertion.assertNotNull(this.clazz, (isClassGiven) ? "clazz given is null" : "Cannot guest the entity parameter type (T).");
		
		javax.persistence.Entity entity = this.clazz.getAnnotation(javax.persistence.Entity.class);
		Assertion.assertTrue(entity != null,
		"The entity class must be annotated with javax.persistence.Entity!");
		
		this.orders = orders;

		if (entity != null && entity.name() != null && entity.name().length() > 0)
		{
			this.entityName = entity.name();
		}
		else
		{
			this.entityName = this.clazz.getName();
		}
		this.className = this.clazz.getName();
	}
	
	/**
	 * The default constructor for subclasses. This constructor will try to guest the class
	 * generic type; and when it cannot guest the type, this will throws an
	 * {@code Exception}. And when the class is not annotated with
	 * {@link javax.persistence.Entity} this will also throw a exception.
	 * 
	 * <p>
	 * To explicitly declare the class type use the constructor
	 * {@link #AbstractDAO(Class, Order...)}.
	 * </p>
	 * 
	 * @param orders the default order (in vararg), order by first to last (see sql
	 *          ordering)
	 * 
	 * @throws NullPointerException when this constructor cannot guest the generic type.
	 * @throws IllegalArgumentException when the generic class type is not annotated with
	 *           {@link javax.persistence.Entity}.
	 */
	protected AbstractDAO(Order... orders)
	{
		this(false, null, orders);
	}

	/**
	 * The constructor for instantiating and for subclass.
	 * 
	 * <p>
	 * It is recommended for a subclass to use the constructor
	 * {@link #AbstractDAO(Order...)} as that constructor will guest the class parameter,
	 * and is not prone to error (copy-paste-error).
	 * </p>
	 * 
	 * @param clazz the persistent class.
	 * @param orders the default order (in vararg), order by first to last (see sql
	 *          ordering)
	 * 
	 * @throws NullPointerException when the parameter {@code clazz} is null.
	 * @throws IllegalArgumentException when the {@code clazz} type is not annotated with
	 *           {@link javax.persistence.Entity}.
	 */
	public AbstractDAO(Class<T> clazz, Order... orders)
			throws NullPointerException, IllegalArgumentException
	{
		this(true, clazz, orders);
	}

	/**
	 * Get the current hibernate session. The method can create a new session or use a
	 * existing.
	 * 
	 * @return a session.
	 */
	protected abstract Session getSession();

	/**
	 * Release a hibernate session.
	 * 
	 * This method must not throw {@link NullPointerException} when
	 * the session parameter is null!
	 * 
	 * @param session the session to release.
	 */
	protected abstract void releaseSession(Session session);

	/**
	 * Translates a hibernate exception to a DatabaseException. The subclass can rethrow the
	 * exception, or translates. As the default implementation does nothing (does not
	 * rethrow it).
	 * 
	 * @param e the hibernate exception.
	 * 
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	protected void translateHibernateException(HibernateException e)
			throws DatabaseException, HibernateException
	{

	}

	/**
	 * This method is called before reading from the database, this allows subclass to do
	 * other things...
	 * 
	 * <p>
	 * Do not forget to call {@link #afterRead()} after reading.
	 * </p>
	 */
	protected void prepareRead()
	{

	}

	/**
	 * This method is called before writing to the database, this allows subclass to do
	 * other things...
	 * 
	 *  <p>
	 * Do not forget to call {@link #afterWrite()} after reading.
	 * </p>
	 */
	protected void prepareWrite()
	{

	}

	/**
	 * This method is called after reading from database, this allows subclass to do cleanup
	 * used by {@link #prepareRead()}...
	 */
	protected void afterRead()
	{

	}

	/**
	 * This method is called after writing to the database, this allows subclass to do
	 * cleanup used by {@link #prepareWrite()}...
	 */
	protected void afterWrite()
	{

	}

	@Override
	public T createNewItemInstance()
	{
		T instance = null;
		try
		{
			instance = clazz.newInstance();
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return instance;
	}

	@Override
	public final Class<T> getEntityClass()
	{
		return clazz;
	}

	@Override
	public final String getEntityName()
	{
		return entityName;
	}

	@Override
	public final String getClassName()
	{
		return className;
	}

	@Override
	public T get(ID primaryKey) throws HibernateException
	{
		return get(this.clazz, primaryKey);
	}

	@Override
	public <E> E get(Class<E> clazz, ID primaryKey) throws HibernateException
	{
		if (primaryKey == null)
		{
			return null;
		}

		E ret = null;
		Session session = null;

		prepareRead();
		try
		{
			session = getSession();
			ret = ClassHelper.<E>cast(session.get(clazz, primaryKey));
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterRead();
		}

		return ret;
	}

	@Override
	public T get(ID primaryKey, boolean readonly) throws HibernateException
	{
		if (primaryKey == null)
		{
			return null;
		}

		T ret = null;
		Session session = null;

		prepareRead();
		try
		{
			session = getSession();
			ret = ClassHelper.<T>cast(session.get(clazz, primaryKey));
			session.setReadOnly(ret, readonly);
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterRead();
		}

		return ret;
	}

	@Override
	public T load(ID primaryKey) throws HibernateException
	{
		if (primaryKey == null)
		{
			return null;
		}

		T ret = null;
		Session session = null;

		prepareRead();
		try
		{
			session = getSession();
			ret = ClassHelper.<T>cast(session.load(clazz, primaryKey));
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterRead();
		}

		return ret;
	}
	
	@Override
	public T reload(T entry) throws HibernateException, DatabaseException
	{
		if (entry == null)
		{
			return null;
		}
		
		T ret = null;
		Session session = null;
		
		prepareRead();
		try
		{
			session = getSession();
			final ID id = ((BaseID<ID>)entry).getId();
			session.evict(entry);
			ret = get(id);
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterRead();
		}
		
		return ret;
	}
	
	/**
	 * Find the object instance from our database table given the primary key for locating
	 * that object. This method may check the value of other field of the object before
	 * returning this.
	 * 
	 * <p> 
	 * The default implementation of this method calls {@link #get(Serializable)}, and 
	 * class extension may override this method to change its behavior.
	 * </p> 
	 * 
	 * @param primaryKey the primary key for locating the record in our database,
	 * @return the proxy to the item.
	 * @throws RuntimeException
	 */
	@Override
	public T find(ID primaryKey) throws RuntimeException
	{
		return get(primaryKey);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ID insert(T entry) throws NullPointerException, HibernateException
	{
		if (entry == null)
		{
			throw new NullPointerException("entry is null!");
		}

		ID serialized = null;
		Session session = null;

		prepareWrite();
		try
		{
			session = getSession();
			serialized = (ID) session.save(entry);
			session.flush();
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterWrite();
		}

		return serialized;
	}

	@Override
	public boolean update(T entry) throws HibernateException
	{
		if (entry == null)
		{
			throw new NullPointerException("entry is null!");
		}
		boolean ret = false;
		Session session = null;

		prepareWrite();
		try
		{
			session = getSession();
			session.update(entry);

			session.flush();

			ret = true;
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterWrite();
		}

		return ret;
	}

	@Override
	public boolean delete(ID id) throws HibernateException
	{
		final StringBuilder deleteHQL = new StringBuilder(100);

		deleteHQL.append("update ").append(className).append(
				" set isValid = 0 where id = :id");
		final String hql = deleteHQL.toString();
		if (log.isDebugEnabled())
			log.debug("Delete HQL: " + hql);

		boolean ret = false;
		Session session = null;

		prepareWrite();
		try
		{
			session = getSession();
			Query query = session.createQuery(hql);
			query.setParameter("id", id);

			int totalUpdated = query.executeUpdate();

			ret = totalUpdated > 0;

			if (log.isDebugEnabled())
				log.debug("Total deleted: " + totalUpdated);

			session.flush();
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterWrite();
		}

		return ret;
	}

	/**
	 * Permanently delete a record from the database.
	 * 
	 * @param entry
	 * @return true if success
	 */
	@Override
	public boolean erase(T entry) throws HibernateException
	{
		if (entry == null)
		{
			throw new NullPointerException("entry is null!");
		}

		boolean ret = false;
		Session session = null;

		prepareWrite();
		try
		{
			session = getSession();
			session.delete(entry);

			session.flush();

			ret = true;
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterWrite();
		}

		return ret;
	}

	@Override
	public int countAllByCriterion(String[] associationPaths, String[] aliasNames, int[] joinTypes,
			Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException
	{
		int result = -1;
		Session session = null;

		prepareRead();
		try
		{
			session = getSession();
			Criteria criteria = createCriteria(session, clazz, associationPaths, aliasNames,
					joinTypes, criterions);

			criteria.setProjection(Projections.rowCount());

			final Object resultObject = criteria.uniqueResult();
			
			if (resultObject instanceof Number)
			{
				result = ((Number)resultObject).intValue();
			}
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterRead();
		}

		return result;
	}

	/**
	 * Create a criteria using the session
	 * 
	 * @param <Y> the type of the class.
	 * 
	 * @param session the hibernate session
	 * @param clazz the persistent class
	 * @param associationPaths a associated path
	 * @param aliasNames the alias name for the associated path
	 * @param joinTypes the join type for the associated path
	 * @param criterions the query criterions
	 * 
	 * @return a criteria created using {@link Session#createCriteria(Class, String)}
	 * 
	 * @throws NullPointerException when session, clazz or null
	 * @throws IllegalArgumentException see message for details
	 * @throws HibernateException see details for more info...
	 */
	@SuppressWarnings("deprecation")
	protected static <Y> Criteria createCriteria(Session session, Class<Y> clazz,
			String associationPaths[], String[] aliasNames, int joinTypes[],
			Criterion... criterions) throws HibernateException
	{
		if (associationPaths != null && joinTypes != null
				&& associationPaths.length != joinTypes.length)
		{
			throw new IllegalArgumentException(
					"associatedPaths and joinTypes parameter must have the same length!");
		}

		Criteria criteria = session.createCriteria(clazz, "this");

		if (associationPaths != null && joinTypes != null)
		{
			if (aliasNames != null && aliasNames.length != associationPaths.length)
			{
				throw new IllegalArgumentException(
						"aliasNames parameter does not have the same length with associatedPaths!");
			}

			String[] nalias = (aliasNames != null) ? aliasNames : associationPaths;

			for (int i = 0; i < associationPaths.length; i++)
			{
				criteria.createAlias(associationPaths[i], nalias[i], joinTypes[i]);
			}
			for (int i = 0; i < associationPaths.length; i++)
			{
				criteria.setFetchMode(associationPaths[i], FetchMode.DEFAULT);
			}
		}

		for (Criterion criterion : criterions)
		{
			if (criterion != null)
				criteria.add(criterion);
		}

		return criteria;
	}

	@Override
	public List<Object> findAllByCriterionProjection(Projection projection,
			Criterion... criterions) throws HibernateException
	{
		return findAllByCriterionProjection(Object.class, false, -1, -1, false, null, null,
				null, false, null, projection, null, criterions).getList();
	}

	@Override
	public List<Object> findAllByCriterionProjection(String associationPaths[],
			String aliasNames[], int joinTypes[], Projection projection,
			Criterion... criterions) throws HibernateException
	{
		return findAllByCriterionProjection(Object.class, false, -1, -1, false,
				associationPaths, aliasNames, joinTypes, false, null, projection, null,
				criterions).getList();
	}

	@Override
	public List<Object> findAllByCriterionProjection(String associationPaths[],
			String[] aliasNames, int joinTypes[], Order orders[], Projection projection,
			Criterion... criterions) throws HibernateException
	{
		return findAllByCriterionProjection(Object.class, false, -1, -1, false,
				associationPaths, null, joinTypes, true, orders, projection, null, criterions).getList();
	}

	@Override
	public <Y> List<Y> findAllByCriterionProjection(Class<Y> nc, String associationPaths[],
			String aliasNames[], int joinTypes[], Projection projection,
			ResultTransformer resultTransformer, Criterion... criterions)
			throws HibernateException
	{
		return findAllByCriterionProjection(nc, false, -1, -1, false, associationPaths,
				aliasNames, joinTypes, false, null, projection, resultTransformer, criterions).getList();
	}

	@Override
	public <Y> List<Y> findAllByCriterionProjection(Class<Y> nc, String associationPaths[],
			String aliasNames[], int joinTypes[], Order orders[], Projection projection,
			ResultTransformer resultTransformer, Criterion... criterions)
			throws HibernateException
	{
		return findAllByCriterionProjection(nc, false, -1, -1, false, associationPaths,
				aliasNames, joinTypes, true, orders, projection, resultTransformer, criterions).getList();
	}

	@Override
	public <Y> ObjectList<Y> findAllByCriterionProjection(Class<Y> nc, int pageNumber,
			int maxResults, String associationPaths[], String[] aliasNames, int joinTypes[],
			Order orders[], Projection projection, ResultTransformer resultTransformer,
			Criterion... criterions) throws HibernateException
	{
		return findAllByCriterionProjection(nc, true, pageNumber, maxResults, true,
				associationPaths, aliasNames, joinTypes, true, orders, projection,
				resultTransformer, criterions);
	}

	@Override
	public <Y> ObjectList<Y> findAllByCriterionProjection(Class<Y> nc, int pageNumber,
			int maxResults, boolean doGetTotalPages, String associationPaths[],
			String[] aliasNames, int joinTypes[], Order orders[], Projection projection,
			ResultTransformer resultTransformer, Criterion... criterions)
			throws HibernateException
	{
		return findAllByCriterionProjection(nc, true, pageNumber, maxResults,
				doGetTotalPages, associationPaths, aliasNames, joinTypes, true, orders,
				projection, resultTransformer, criterions);
	}

	@Override
	public <Y> ObjectList<Y> findAllByCriterionProjection(Class<Y> nc,
			boolean hasPagination, int pageNumber, int maxResults, String associationPaths[],
			String[] aliasNames, int joinTypes[], boolean hasOrder, Order orders[],
			Projection projection, ResultTransformer resultTransformer, Criterion... criterions)
			throws HibernateException
	{
		return findAllByCriterionProjection(nc, hasPagination, pageNumber, maxResults, true,
				associationPaths, aliasNames, joinTypes, hasOrder, orders, projection,
				resultTransformer, criterions);
	}

	@Override
	public <Y> ObjectList<Y> findAllByCriterionProjection(Class<Y> nc,
			boolean doGetTotalPages, String associationPaths[], String[] aliasNames,
			int joinTypes[], Order orders[], Projection projection,
			ResultTransformer resultTransformer, Criterion... criterions)
			throws HibernateException
	{
		return findAllByCriterionProjection(nc, false, -1, -1, doGetTotalPages,
				associationPaths, aliasNames, joinTypes, (orders != null), orders, projection,
				resultTransformer, criterions);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Y> ObjectList<Y> findAllByCriterionProjection(Class<Y> nc,
			boolean hasPagination, int pageNumber, int maxResults, boolean doGetTotalPages,
			String associationPaths[], String aliasNames[], int joinTypes[], boolean hasOrder,
			Order orders[], Projection projection, ResultTransformer resultTransformer,
			Criterion... criterions) throws HibernateException
	{
		ObjectList<Y> list = new ObjectList<Y>();
		Session session = null;

		prepareRead();
		try
		{
			session = getSession();
			Criteria criteria = createCriteria(session, clazz, associationPaths, aliasNames,
					joinTypes, criterions);

			if (log.isTraceEnabled())
				log.trace("check hasOrder: " + hasOrder + ", check hasPagination: "
						+ hasPagination);

			if (hasOrder)
			{
				addOrderToCriteria(criteria, orders, false);
			}

			if (doGetTotalPages)
			{
				list.setTotal(getTotalItems(criteria));
			}

			if (hasPagination)
			{
				addPagingToCriteria(criteria, pageNumber, maxResults);
			}

			criteria.setProjection(projection);

			if (resultTransformer != null)
				criteria.setResultTransformer(resultTransformer);

			list.setList(criteria.list());
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterRead();
		}

		return list;
	}

	@Override
	public List<T> findAllByCriterionList(int page, int maxResults,
			String[] associatedPaths, String[] aliasNames, int[] joinTypes, Order[] orders,
			Criterion... criterions) throws HibernateException
	{
		return findAllByCriterion(this.clazz, false, true, page, maxResults, associatedPaths,
				aliasNames, joinTypes, orders, criterions).getList();
	}

	@Override
	public List<T> findAllByCriterionList(String associationPaths[], String aliasNames[],
			int joinTypes[], Order[] orders, Criterion... criterions) throws HibernateException
	{
		return findAllByCriterion(this.clazz, false, false, -1, -1, associationPaths,
				aliasNames, joinTypes, orders, criterions).getList();
	}

	@Override
	public T findUniqueResult(String[] associationPaths, String[] aliasNames, int[] joinTypes,
			Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException
	{
		return findUniqueResult(associationPaths, aliasNames, joinTypes, NO_SORTING, criterions);
	}

	@Override
	public T findUniqueResult(String[] associationPaths, String[] aliasNames, int[] joinTypes,
			Order[] orders, Criterion... criterions)
			throws DatabaseException, HibernateException, IllegalArgumentException
	{
		Session session = null;
		T result = null;

		prepareRead();
		try
		{
			session = getSession();
			Criteria criteria = createCriteria(session, clazz, associationPaths, aliasNames,
					joinTypes, criterions);

			List<?> list = criteria.list();
			
			@SuppressWarnings("unchecked")
			T newResult = (T)ObjectList.getUniqueResult(list);
			
			result = newResult;
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterRead();
		}

		return result;
	}

	@Override
	public <E> List<E> findAllByCriterionList(final Class<E> clazz,
			String associationPaths[], String aliasNames[], int joinTypes[], Order[] orders,
			Criterion... criterions) throws HibernateException
	{
		return findAllByCriterion(clazz, false, false, -1, -1, associationPaths, aliasNames,
				joinTypes, orders, criterions).getList();
	}

	@Override
	public ObjectList<T> findAllByCriterion(int page, int maxResults,
			String[] associatedPaths, String[] aliasNames, int[] joinTypes, Order[] orders,
			Criterion... criterions) throws HibernateException
	{
		return findAllByCriterion(this.clazz, true, true, page, maxResults, associatedPaths,
				aliasNames, joinTypes, orders, criterions);
	}

	@Override
	public ObjectList<T> findAllByCriterion(boolean countTotal, int page, int maxResults,
			String[] associatedPaths, String[] aliasNames, int[] joinTypes, Order[] orders,
			Criterion... criterions) throws HibernateException
	{
		return findAllByCriterion(this.clazz, countTotal, true, page, maxResults, associatedPaths,
				aliasNames, joinTypes, orders, criterions);
	}

	@Override
	public <E> ObjectList<E> findAllByCriterion(Class<E> clazz, String associationPaths[],
			String aliasNames[], int joinTypes[], Order[] orders, Criterion... criterions)
			throws HibernateException
	{
		return findAllByCriterion(clazz, true, false, -1, -1, associationPaths, aliasNames,
				joinTypes, orders, criterions);
	}

	@Override
	public ObjectList<T> findAllByCriterion(String associationPaths[], String aliasNames[],
			int joinTypes[], Order[] orders, Criterion... criterions) throws HibernateException
	{
		return findAllByCriterion(this.clazz, true, false, -1, -1, associationPaths,
				aliasNames, joinTypes, orders, criterions);
	}

	@Override
	public ObjectList<T> findAllByCriterion(boolean countTotal, String associationPaths[],
			String aliasNames[], int joinTypes[], Order[] orders, Criterion... criterions)
			throws HibernateException
	{
		return findAllByCriterion(this.clazz, countTotal, false, -1, -1, associationPaths,
				aliasNames, joinTypes, orders, criterions);
	}

	@Override
	public <E> ObjectList<E> findAllByCriterion(final Class<E> clazz,
			final boolean countTotal, final String[] associatedPaths,
			final String[] aliasNames, final int[] joinTypes, final Order[] orders,
			final Criterion... criterions) throws HibernateException
	{
		return findAllByCriterion(clazz, countTotal, false, -1, -1, associatedPaths,
				aliasNames, joinTypes, orders, criterions);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> ObjectList<E> findAllByCriterion(final Class<E> clazz,
			final boolean countTotal, final boolean hasPaging, final int page,
			final int maxResults, final String[] associatedPaths, final String[] aliasNames,
			final int[] joinTypes, final Order[] orders, final Criterion... criterions)
			throws HibernateException
	{
		if (associatedPaths != null && joinTypes != null
				&& associatedPaths.length != joinTypes.length)
		{
			throw new IllegalArgumentException(
					"aliases and joinType parameter must have the same length");
		}
		
		final ObjectList<E> ret = new ObjectList<E>();

		Session session = null;
		prepareRead();
		try
		{
			session = getSession();
			Criteria criteria = createCriteria(session, clazz, associatedPaths, aliasNames,
					joinTypes, criterions);

			if (countTotal)
			{
				ret.setTotal(getTotalItems(criteria));
			}

			addOrderToCriteria(criteria, orders, true);

			if (hasPaging)
			{
				addPagingToCriteria(criteria, page, maxResults);
			}
			ret.setList(criteria.list());
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterRead();
		}
		return ret;
	}

	/**
	 * Compute the total items in the criteria using projection. (by "select count(id)").
	 * 
	 * @param criteria
	 * @return number of items count for the criteria given.
	 * @throws HibernateException
	 */
	protected static int getTotalItems(Criteria criteria) throws HibernateException
	{
		/*criteria.setProjection(Projections.count("id"));*/
		criteria.setProjection(Projections.rowCount());

		Long totalCount = (Long) criteria.uniqueResult();

		int total = totalCount.intValue();

		criteria.setProjection(null);
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		return total;
	}

	/**
	 * Set the order of the criteria
	 * 
	 * @param criteria
	 * @param otherOrder
	 * @param useDefaultWhenNull
	 * @throws HibernateException
	 */
	protected void addOrderToCriteria(Criteria criteria, Order[] otherOrder,
			boolean useDefaultWhenNull) throws HibernateException
	{
		Order sortby[] = null;

		if (otherOrder != null)
		{
			log.trace("sorting table with order specified");
			sortby = otherOrder;
		}
		else if (orders != null && useDefaultWhenNull)
		{
			log.trace("sorting table with the default order");
			sortby = orders;
		}

		if (sortby != null && sortby.length > 0)
		{
			for (Order sort : sortby)
			{
				if (sort != null)
				{
					criteria.addOrder(sort);
				}
			}
		}
	}

	/**
	 * Zero base paging for easier computation.
	 * 
	 * @param criteria where the paging to be set
	 * @param page zero base 0 for first page
	 * @param maxResults number of items to be fetch
	 * @throws HibernateException
	 */
	protected static void addPagingToCriteria(Criteria criteria, int page, int maxResults)
			throws HibernateException
	{
		if (page >= 0 && maxResults > 0)
		{
			int firstResult = page * maxResults;
			criteria.setFirstResult(firstResult);
		}

		if (maxResults > 0)
		{
			criteria.setMaxResults(maxResults);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(T... objs) throws HibernateException
	{
		Session session = null;

		prepareRead();
		try
		{
			session = getSession();
			for (T obj : objs)
			{
				if (objs != null)
				{
					session.refresh(obj);
				}
			}
		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterRead();
		}
	}

	@Override
	public void batchInsert(Collection<T> objs) throws HibernateException
	{
		Session session = null;

		prepareWrite();
		try
		{
			session = getSession();
			
			int i = 0;
			for (T t : objs)
			{
				if (t != null)
				{
					session.save(t);

					if ((++i) % 20 == 0)
					{
						i = 0;
						session.flush();
					}
				}
				else
				{
					log.warn("objs collection contains a null value!");
				}
			}
			session.flush();

		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterWrite();
		}
	}

	@Override
	public void batchUpdate(Collection<T> objs) throws HibernateException
	{
		Session session = null;

		prepareWrite();
		try
		{
			session = getSession();

			int i = 0;
			for (T t : objs)
			{
				if (t != null)
				{
					session.update(t);

					if ((++i) % 20 == 0)
					{
						i = 0;
						session.flush();
					}
				}
				else
				{
					log.error("objs collection contains a null value!");
				}
			}
			session.flush();

		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterWrite();
		}
	}

	@Override
	public void batchErase(Collection<T> objs) throws RuntimeException
	{
		Session session = null;

		prepareWrite();
		try
		{
			session = getSession();

			int i = 0;
			for (T t : objs)
			{
				if (t != null)
				{
					session.delete(t);

					if ((++i) % 20 == 0)
					{
						i = 0;
						session.flush();
					}
				}
				else
				{
					log.error("objs collection contains a null value!");
				}
			}
			session.flush();

		}
		catch (HibernateException e)
		{
			log.error(e.getMessage(), e);
			translateHibernateException(e);
		}
		finally
		{
			releaseSession(session);
			afterWrite();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void batchInsert(T... objs) throws HibernateException
	{
		batchInsert(Arrays.asList(objs));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void batchUpdate(T... objs) throws HibernateException
	{
		batchUpdate(Arrays.asList(objs));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void batchErase(T... objs) throws RuntimeException
	{
		batchErase(Arrays.asList(objs));
	}

	/**
	 * Construct a {@link Order} using a {@link SortOrder} and the name of the field.
	 * 
	 * @param order the {@link SortOrder} which we will determines how the field to be
	 *          sorted
	 * @param field the field name.
	 * @return the array of order or null
	 */
	protected Order[] getOrderBySortOrderAndField(SortOrder order, String field)
	{
		Order[] ret = null;

		if (order != null && field != null)
		{
			switch (order)
			{
			case asc:
			case desc:
				ret = new Order[1];
				ret[0] = order.getOrder(field);
				break;
			case noSorting:
			case defaultValue:
				break;
			}
		}

		return ret;
	}
}