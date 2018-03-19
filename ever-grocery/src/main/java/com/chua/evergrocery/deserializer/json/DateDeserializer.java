package com.chua.evergrocery.deserializer.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   4 Sep 2017
 */
public class DateDeserializer extends StdDeserializer<Date> {

	private static final long serialVersionUID = 1475011767205281098L;

	public DateDeserializer() {
		this(null);
	}
	
	public DateDeserializer(Class<?> vc) {
		super(vc);
	}
	
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		final Date date;
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			date = sdf.parse(node.asText());
			return date;
		} catch (NullPointerException e) {
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
