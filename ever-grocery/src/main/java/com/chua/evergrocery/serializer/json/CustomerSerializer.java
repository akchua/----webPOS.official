package com.chua.evergrocery.serializer.json;

import java.io.IOException;

import com.chua.evergrocery.database.entity.Customer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomerSerializer 
		extends JsonSerializer<Customer> {

	@Override
	public void serialize(Customer customer, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeObject(customer);
	}
}
