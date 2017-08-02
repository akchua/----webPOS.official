package com.chua.evergrocery.database.entity.base;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @version 1.0, Jan 18, 2016
 * 
 * @see BaseObject
 * @see BaseEntity
 */
@MappedSuperclass
public class BaseUpdatable extends BaseEntity
		implements Serializable, IBaseUpdatable
{
	/** For serialization purpose */
	private static final long serialVersionUID = 1663731598611417574L;

	/** The date when the entity is last updated. */
	private Date updatedOn;

	/** The date when the entity is created. */
	private DateTime createdOn;

	/**
	 * Construct the updatable base object. This can only be called by subclass.
	 */
	protected BaseUpdatable()
	{
		super();
	}

	@Version
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "updated_on", nullable = false)
	@Override
	public Date getUpdatedOn()
	{
		Date copyUpdatedOn = null;
		if (updatedOn != null)
		{
			copyUpdatedOn = new Date(updatedOn.getTime());
		}
		return copyUpdatedOn;
	}

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "created_on", nullable = false)
	@Override
	public DateTime getCreatedOn()
	{
		if (createdOn == null)
		{
			createdOn = new DateTime();
		}
		return createdOn;
	}

	@Override
	public void setUpdatedOn(Date updatedOn)
	{
		this.updatedOn = null;

		if (updatedOn != null)
		{
			this.updatedOn = new Date(updatedOn.getTime());
		}
	}

	@Override
	public void setCreatedOn(DateTime createdOn)
	{
		this.createdOn = createdOn;
	}
	
	@Override
	public void writeData(ObjectOutput out) throws IOException
	{
		super.writeData(out);
		out.writeObject(updatedOn);
		out.writeObject(createdOn);
	}
	
	@Override
	public void readData(ObjectInput in) throws IOException, ClassNotFoundException
	{
		super.readData(in);
		updatedOn = (Date)in.readObject();
		createdOn = (DateTime)in.readObject();
	}
	
	@Override
	public void reset()
	{
		super.reset();
		updatedOn = null;
		createdOn = null;
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