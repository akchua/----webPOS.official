package com.chua.evergrocery.exceptions;

/**
 * General database error occurred.
 * 
 * @version 1.0 Jan 22, 2016
 *
 */
public class DatabaseException extends RuntimeException
{
	/** Serialization purpose */
	private static final long serialVersionUID = -5974700041843976513L;

	/**
	 */
	public DatabaseException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DatabaseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public DatabaseException(String message)
	{
		super(message);
	}
	
	/**
	 * @param cause
	 */
	public DatabaseException(Throwable cause)
	{
		super(cause);
	}

}