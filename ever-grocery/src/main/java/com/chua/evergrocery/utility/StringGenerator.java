package com.chua.evergrocery.utility;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 3, 2016
 */
public final class StringGenerator {

	private static SecureRandom random = null;
	
	private StringGenerator() {
		
	}
	
	public static String nextString() {
		if(random == null) random = new SecureRandom();
		return new BigInteger(100, random).toString(32);
	}
}
