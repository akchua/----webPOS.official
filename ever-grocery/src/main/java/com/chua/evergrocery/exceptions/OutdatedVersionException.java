package com.chua.evergrocery.exceptions;

/**
 * This exception is thrown when the persistent instance is out-dated.
 * 
 * @version 1.0 Jan 22, 2016
 * 
 */
public class OutdatedVersionException
		extends DatabaseException
{
	/** Serialization purpose */
	private static final long serialVersionUID = 4576728600253142249L;

	/**
	 * @see DatabaseException#DatabaseException()
	 */
	public OutdatedVersionException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @see DatabaseException#DatabaseException(String, Throwable)
	 */
	public OutdatedVersionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 * @see DatabaseException#DatabaseException(String)
	 */
	public OutdatedVersionException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 * @see DatabaseException#DatabaseException(Throwable)
	 */
	public OutdatedVersionException(Throwable cause)
	{
		super(cause);
	}

}