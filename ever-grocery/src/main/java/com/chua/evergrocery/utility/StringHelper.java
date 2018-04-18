package com.chua.evergrocery.utility;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 11, 2016
 */
public class StringHelper {
	
	private StringHelper() {
		
	}

	/**
	 * Centers the string in the given length.
	 * Pads white spaces on the whole length including after the string.
	 * @param s The string to be centered.
	 * @param length The length of the area.
	 * @return The centered string.
	 */
	public static String center(String s, int length) {
		return center(s, length, ' ');
	}
	
	public static String center(String s, int length, char fill) {
		if(s == null || length <= s.length()) return s;
		
		String centered = "";
		int temp = (length - s.length()) / 2;
		for(int i = 0; i < temp; i++) centered += fill;
		centered += s;
		for(int i = 0; i < temp; i++) centered += fill;
		if((length - s.length()) % 2 == 1) centered += fill;
		
		return centered;
	}
	
	/**
	 * Removes all special characters from a string.
	 * Preserves the file type of the string.
	 * @param s The string to be converted.
	 * @return The file safe string.
	 */
	public static String convertToFileSafeFormat(String s) {
		return s;
	}
	
	/**
	 * Extracts the file extension from a given string (if any)
	 * @param fileName The file name where extension is to be extracted from
	 * @return The extracted file extension without the dot (ex. jpg, pdf ...)
	 */
	public static String getFileExtension(String fileName) {
		String[] tokens = fileName.split("\\.");
		return tokens[tokens.length - 1];
	}
}
