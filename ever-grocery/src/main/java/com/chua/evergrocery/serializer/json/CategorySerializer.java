package com.chua.evergrocery.serializer.json;

import java.io.IOException;

import com.chua.evergrocery.database.entity.Category;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CategorySerializer extends JsonSerializer<Category> {

	@Override
	public void serialize(Category category, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeObject(category);
	}
}
