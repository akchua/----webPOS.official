package com.chua.evergrocery.serializer.json;

import java.io.IOException;

import com.chua.evergrocery.database.entity.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class UserSerializer 
		extends JsonSerializer<User> {

	@Override
	public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeObject(user);
	}
}
