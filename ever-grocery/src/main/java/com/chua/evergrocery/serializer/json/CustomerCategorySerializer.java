package com.chua.evergrocery.serializer.json;

import java.io.IOException;

import com.chua.evergrocery.database.entity.CustomerCategory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public class CustomerCategorySerializer extends JsonSerializer<CustomerCategory> {

	@Override
	public void serialize(CustomerCategory customerCategory, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeObject(customerCategory);
	}
}
