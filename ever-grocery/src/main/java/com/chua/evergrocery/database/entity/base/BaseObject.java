package com.chua.evergrocery.database.entity.base;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * @version 1.0, Jan 18, 2016
 * 
 * @see BaseUpdatable
 * @see BaseEntity
 */
@MappedSuperclass
public class BaseObject
		extends BaseUpdatable
		implements IBaseObject, Serializable
{
	/**
	 * For Serializable purpose
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = -5655075156805429911L;

	/** The validity of the entity. */
	private boolean isValid;

	/** The sole constructor. This can only be called by subclass. */
	protected BaseObject()
	{
		this.isValid = true;
	}

	@Basic
	@Column(name = "valid", nullable = false)
	@Override
	public Boolean getIsValid()
	{
		return Boolean.valueOf(isValid);
	}
	
	@Transient
	@Override
	public boolean isValid()
	{
		return isValid;
	}

	@Override
	public void setIsValid(Boolean isValid)
	{
		final boolean newValue = ((isValid != null) ? isValid.booleanValue() : false);
		setValid(newValue);
	}

	@Override
	public void setValid(boolean isValid)
	{
		this.isValid = isValid;
	}

	/**
	 * Copy (soft) the fields from another object. The purpose of this method is to help the
	 * subclass implementing {@link com.chua.evergrocery.entity.base.Copyable#copyFrom(Object)} as
	 * not to create codes that copy the fields belonging to this class.
	 * 
	 * @param object the object which we are copying.
	 */
	protected void copyFrom(BaseObject object)
	{
//		this.setId(object.getId());
		this.setUpdatedOn(object.getUpdatedOn());
		this.setCreatedOn(object.getCreatedOn());
		this.isValid = object.isValid;
	}

	@Override
	public void writeData(ObjectOutput out) throws IOException
	{
		super.writeData(out);
		out.writeBoolean(isValid);
	}
	
	@Override
	public void readData(ObjectInput in) throws IOException, ClassNotFoundException
	{
		super.readData(in);
		isValid = in.readBoolean();
	}

	@Override
	public void reset()
	{
		super.reset();
		isValid = false;
	}

	/**
	 * Write the object to a object output stream according to the specificaton of {@link Serializable}.
	 * 
	 * @param out
	 * 
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		writeData(out);
	}

	/**
	 * Read the object from a object input stream according to the specificaton of {@link Serializable}.
	 * 
	 * @param out
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		readData(in);
	}

	/**
	 * Read the object with no data according to the specificaton of {@link Serializable}.
	 * 
	 * @throws ObjectStreamException
	 */
	@SuppressWarnings("unused")
	private void readObjectNoData() throws ObjectStreamException
	{
		reset();
	}
}