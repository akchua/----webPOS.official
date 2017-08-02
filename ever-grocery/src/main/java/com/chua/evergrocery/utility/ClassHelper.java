package com.chua.evergrocery.utility;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a helper utility that contains methods to manipulate a class.
 * 
 * @version 1.0, Jan 22, 2016
 */
public final class ClassHelper
{
	/** The logger for this class. */
	private static final Logger log = LoggerFactory.getLogger(ClassHelper.class);

	/**
	 * Get the generic parameter of the class at the specified index.
	 * 
	 * @param <T> the type of the class
	 * @param clazz the class to retrieve the generic parameter
	 * @param parameterIndex the index of the parameter.
	 * @return the class type. <code>null</code> when this method cannot retrieve the
	 *         parameter.
	 */
	public static <T> Class<T> getGenericParameter(Class<?> clazz, int parameterIndex)
	{
		Class<T> parameter = null;
		Type superClass = clazz.getGenericSuperclass();

		while (!ParameterizedType.class.isInstance(superClass) && superClass != null)
		{
			superClass = (superClass instanceof Class<?>) ? ((Class<?>) superClass).getGenericSuperclass() : null;
		}

		if (superClass instanceof ParameterizedType)
		{
			ParameterizedType paramType = (ParameterizedType) superClass;
			Type arguments[] = paramType.getActualTypeArguments();

			if (arguments != null && arguments.length > parameterIndex)
			{
				Type type = arguments[parameterIndex];

				if (type instanceof Class)
				{
					parameter = cast(type);
				}
			}
		}

		if (parameter == null)
		{
			log.error("The extended class {} must set the generic paramters.", clazz);
		}

		return parameter;
	}

	/**
	 * Cast the object to the type T. This method suppresses the warning.
	 * 
	 * @param <T> the expected type.
	 * @param object the object to cast.
	 * @return the cast object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object object)
	{
		return (T) object;
	}
	
	/**
	 * Find the resource by name.
	 * 
	 * @param name the name of the resource
	 * @return The URL resource, <code>null</code> when the resource is not found.
	 */
	public static URL getResource(String name)
	{
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return classLoader.getResource(name);
	}
	
	/**
	 * Find the resource by name and return the input stream.
	 * 
	 * @param name the name of the resource
	 * @return The input stream, <code>null</code> when the resource is not found.
	 */
	public static InputStream getResourceAsStream(String name)
	{
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return classLoader.getResourceAsStream(name);
	}

	/**
	 * Default constructor. This class is not allowed to be instantiated.
	 */
	private ClassHelper()
	{
	}
}