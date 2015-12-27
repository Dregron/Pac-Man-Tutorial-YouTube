package com.dregronprogram.tiled_map;

import java.io.IOException;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ObjectDeserializer extends JsonDeserializer<Object> {

	@Override
	public Property deserialize(JsonParser jsonParser, DeserializationContext arg1) throws IOException, JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        Entry<String, JsonNode> next = node.fields().next();
        Property property = new Property();
        property.setId(next.getKey());
        property.setValue(next.getValue().textValue());
		return property;
	}
}
