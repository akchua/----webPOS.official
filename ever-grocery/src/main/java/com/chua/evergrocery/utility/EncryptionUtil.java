package com.chua.evergrocery.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class EncryptionUtil {

	/** The md5 digest class. can be null when md5 is not supported. */
	private static final MessageDigest md5Digestor;
	
	static
	{
		MessageDigest digestor = null;
		try
		{
			digestor = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e)
		{
		}
		md5Digestor = digestor;
	}

	/**
	 * Prints the array of bytes to a string.
	 * @param b
	 * @return the string bytes.
	 */
	private static String bytesToHex(byte[] b)
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; ++i)
		{
			sb.append((Integer.toHexString((b[i] & 0xFF) | 0x100)).substring(1, 3));
		}
		return sb.toString();
	}

	/**
	 * Get the md5 value of the string data.
	 * @param data the string.
	 * @return a md5 of the data given.
	 */
	public static String getMd5(String data)
	{
		String md5 = "";
		if (md5Digestor != null)
		{
			byte[] mybytes = data.getBytes();
			byte[] md5digest = md5Digestor.digest(mybytes);
			md5 = bytesToHex(md5digest);
		}
		return md5;
	}

	/**
	 * This class is not to be extended.
	 */
	private EncryptionUtil()
	{
		
	}
}
