package com.chua.evergrocery.database.entity.base;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @version 1.0, Jan 18, 2016s
 * 
 * @see BaseUpdatable
 * @see BaseObject
 */

@SuppressWarnings("rawtypes")
@MappedSuperclass
public class BaseEntity
		implements Serializable, BaseID<Long>, Comparable
{
	/**
	 * For serialization purpose.
	 */
	private static final long serialVersionUID = 8026550697870133500L;

	/** The id of the entity */
	private Long id;

	/**
	 * Construct a entity. This can only be called by subclass.
	 */
	protected BaseEntity()
	{
		super();
	}

	/**
	 * Get the id of the entity
	 * 
	 * @return the id of the entity
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * Set the id of the entity
	 * 
	 * @param id the id to be set.
	 */
	@Override
	public void setId(Long id)
	{
		this.id = id;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (this == obj)
		{
			return true;
		}

		if (obj instanceof BaseID)
		{
			BaseID<?> bobj = (BaseID<?>) obj;
			
			final Object thisid = getId();
			final Object objid = bobj.getId();
			
			if (thisid == null || objid == null)
			{
				return false;
			}

			return thisid.equals(objid);
		}

		return false;
	}
	
	@Override
	public int compareTo(Object o)
	{
		if (o == null)
		{
			return 1;
		}
		
		if (this == o)
		{
			return 0;
		}
		
		
		int result = -1;
		
		if (o instanceof BaseEntity)
		{
			final Long otherId = ((BaseEntity)o).id;
			if (id == null && otherId == null)
			{
				result = 0;
			}
			else if (id != null)
			{
				result = id.compareTo(otherId);
			}
			else
			{
				result = -1;
			}
		}
		
		return result;
	}

	@Override
	public int hashCode()
	{
		return (id == null) ? 0 : id.hashCode();
	}
	
	/**
	 * Write the internal data to a output stream.
	 * 
	 * <p>
	 * Subclasses are required to override this method.
	 * </p>
	 * 
	 * @param out the output stream.
	 * 
	 * @throws IOException
	 */
	public void writeData(ObjectOutput out) throws IOException
	{
		//ObjectIOUtils.writeLongValueWithNullFlag(out, this.id);
	}
	
	/**
	 * Read the internal data from a input stream.
	 * 
	 * <p>
	 * Subclasses are required to override this method.
	 * </p>
	 * 
	 * @param in the input stream.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void readData(ObjectInput in) throws IOException, ClassNotFoundException
	{
		//this.id = ObjectIOUtils.readLongValueWithNullFlag(in);
	}
	
	/**
	 * Reset the data. 
	 */
	public void reset()
	{
		id = null;
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