package com.chua.evergrocery.objects;

/**
 * The assertion utility.
 * 
 * @version 1.0, Jan 22, 2016
 * 
 */
public final class Assertion
{
	/**
	 * Assert that the instance will be a <b>not-null</b> value. If the instance passed is
	 * <code>null</code>, a {@link NullPointerException} will be throw. This throws a exception
	 * with a empty message.
	 * 
	 * @param instance the instance to be check if it is null or not
	 * @throws NullPointerException when instance is null.
	 */
	public static void assertNotNull(Object instance)
	{
		assertNotNull(instance, "");
	}

	/**
	 * Assert that the instance will be a <b>not-null</b> value. If the instance passed is
	 * <code>null</code>, a {@link NullPointerException} will be throw.
	 * 
	 * @param instance the instance to be check if it is null or not
	 * @param message the message for the Exception
	 * @throws NullPointerException when instance is null.
	 */
	public static void assertNotNull(Object instance, String message)
			throws NullPointerException
	{
		if (instance == null)
		{
			throw new NullPointerException(message);
		}
	}

	/**
	 * Assert that the two given objects are equal. If they are <b>not</b> equal a <b>{@link IllegalArgumentException}</b>
	 * will be thrown.
	 * 
	 * <p>
	 * <b>Note:</b> This method will use the {@link Object#equals(Object)} method, so the
	 * object given are required to follow the symmetric rule that is that <i>x = y</i>
	 * then <i>y = x</i>.
	 * </p>
	 * 
	 * @param ob1 object 1 to be compared against object 2.
	 * @param ob2 object 2 to be compared against object 1.
	 * @throws IllegalArgumentException the given object is not equal
	 */
	public static void assertEquals(Object ob1, Object ob2) throws IllegalArgumentException
	{
		if (!ob1.equals(ob2))
		{
			throw new IllegalArgumentException("object given are not equal!");
		}
	}

	/**
	 * Assert that the expression is <b>true</b>. If <b>false</b> a
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param expression the expression to check whether true or false
	 * @param message the message of the exception
	 * @throws IllegalArgumentException the expression is false
	 */
	public static void assertTrue(boolean expression, String message)
			throws IllegalArgumentException
	{
		if (!expression)
		{
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Assert that the expression is <b>false</b>. If <b>true</b> a
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param expression the expression to check whether true or false
	 * @param message the message of the exception
	 * @throws IllegalArgumentException the expression is true
	 */
	public static void assertFalse(boolean expression, String message)
	{
		assertTrue(!expression, message);
	}

	/** default private constructor */
	private Assertion()
	{
	}
}