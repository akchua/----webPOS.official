package com.chua.evergrocery.database.prototype;

import java.io.Serializable;
import java.util.List;

import com.chua.evergrocery.database.dao.base.DeletableDAO;
import com.chua.evergrocery.database.dao.base.ReadableDAO;
import com.chua.evergrocery.database.dao.base.ReloadableDAO;
import com.chua.evergrocery.database.dao.base.WritableDAO;
import com.chua.evergrocery.database.entity.base.BaseID;
import com.chua.evergrocery.objects.ObjectList;

/**
 * 
 * @param <T> the persistent class, the class must implement {@link BaseID}
 * @param <ID> the type of the primary key of the class, the class must implement
 *          {@link Serializable}
 * 
 * @version 1.0, Jan 22, 2016
 *
 */
public interface Prototype<T extends BaseID<ID>, ID extends Serializable>
		extends ReloadableDAO<T, ID>, ReadableDAO<T, ID>, WritableDAO<T, ID>, DeletableDAO<T, ID>
{
	/**
	 * Retrieves all the records in our table into a <code>java.util.List</code> object.
	 * The keys inside the <code>java.util.List</code> object are the primary keys of the
	 * record
	 * 
	 * @return all valid items.
	 */
	List<T> findAllList();
	
	/**
	 * Retrieves all the records in our table into a <code>java.util.List</code> object.
	 * The keys inside the <code>java.util.List</code> object are the primary keys of the
	 * record
	 * 
	 * @return all valid items.
	 */
	ObjectList<T> findAll();
	
	/**
	 * Evict an object from first level cache. Make sure to lazy load associated collection of objects before calling this.
	 * 
	 * @param object the object to evict
	 */
	void evict(T object);
	
	/**
	 * Set the object by the given read only value.
	 * 
	 * @param object the object 
	 * @param readOnly the read only flag
	 */
	void setReadOnly(T object, boolean readOnly);
}