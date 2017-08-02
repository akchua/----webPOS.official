package com.chua.evergrocery.database.entity.base;

/**
 * @version 1.0, Jan 18, 2016
 */
public interface IBaseObject extends IBaseUpdatable
{
	/**
	 * Get the validity of the entity
	 * 
	 * @return Boolean.TRUE if valid, Boolean.FALSE if not valid
	 * 
	 * @see #isValid
	 */
	public Boolean getIsValid();
	
	/**
	 * Is this entity valid. This method returns the primitive type and is not persisted.
	 * 
	 * @return <code>true</code> when this entity is valid.
	 */
	public boolean isValid();
	
	/**
	 * Set the validity of the entity.
	 * 
	 * @param isValid a instance of Boolean, <code>null</code> value is considered false.
	 */
	public void setIsValid(Boolean isValid);
	
	/**
	 * Set the validity of the entity.
	 * 
	 * @param isValid the new value to determine when this item is valid or not.
	 */
	public void setValid(boolean isValid);
}