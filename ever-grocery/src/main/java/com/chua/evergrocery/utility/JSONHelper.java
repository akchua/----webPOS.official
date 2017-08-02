package com.chua.evergrocery.utility;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONHelper {

	public static final <T extends Object> T parse(String data, Class<T> clazz) {
		T parsedData = null;
		
		try {
			parsedData = (T) new ObjectMapper().readValue(data, clazz);
		} catch(IOException ioe) {
			parsedData = null;
		}
		
		return parsedData;
	}
}
