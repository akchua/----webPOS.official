package com.chua.evergrocery.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This is a utility class used for writing in Text Format (.txt)
 * 
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 11, 2016
 */
public class TextWriter {
	
	/**
	 * This class does not need to be instantiated
	 */
	private TextWriter() {
		
	}
	
	/**
	 * This static method is used to write a Java String to a txt file
	 * 
	 * @param message The message to be written
	 * @param path The path where the file will be created
	 */
	public static void write(String message, String path) {
		TextWriter.write(message, path, false);
	}
	
	public static void write(String message, String path, Boolean append) {
		try {
			File file = new File(path);
			if(file.getParentFile() != null) file.getParentFile().mkdirs();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, append));
			
			String[] tokens = message.split("\n");
			for(String s : tokens) {
				bw.write(s);
				bw.newLine();
			}
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
