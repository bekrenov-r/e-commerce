package com.bekrenovr.ecommerce.orders.util;

import com.bekrenovr.ecommerce.catalog.model.Size;
import com.bekrenovr.ecommerce.catalog.model.SizeFactory;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SizeDeserializer extends StdDeserializer<Size> {
    private final SizeFactory sizeFactory;

    @Autowired
    public SizeDeserializer(SizeFactory sizeFactory) {
        super(Size.class);
        this.sizeFactory = sizeFactory;
    }

    @Override
    public Size deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return sizeFactory.getSize(node.asText());
    }
}
