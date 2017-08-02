package com.chua.evergrocery.database.dao.base;

import java.io.Serializable;

import com.chua.evergrocery.database.entity.base.BaseID;
import com.chua.evergrocery.database.entity.base.IEntity;

/**
 * The DAO provides common process (or methods) used to manipulate a data from and to a
 * database.
 * 
 * @param <T> the persistent class, the class must implement {@link BaseID}
 * @param <ID> the type of the primary key of the class, the class must implement
 *          {@link Serializable}
 * 
 * @version 1.0, Jan 22, 2016
 * 
 * @see AbstractDAO
 * @see TranslatedAbstractDAO
 * @see LockedTranslatedAbstractDAO
 * @see BaseID
 * @see Serializable
 */
public interface DAO<T extends IEntity<ID>, ID extends Serializable>
		extends DAOClassSupport<T>, WritableDAO<T, ID>, ReloadableDAO<T, ID>, DeletableDAO<T, ID>,
		ReadableDAO<T, ID>
{
}