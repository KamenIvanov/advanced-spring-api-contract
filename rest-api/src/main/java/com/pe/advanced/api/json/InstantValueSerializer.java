package com.pe.advanced.api.json;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.time.Instant;

/**
 * Writes an instant in the one format this API publishes. Note the Jackson 3 type names:
 * {@code JsonSerializer} became {@code ValueSerializer}, and the context argument is a
 * {@code SerializationContext} rather than a provider.
 */
public class InstantValueSerializer extends ValueSerializer<Instant> {

    @Override
    public void serialize(Instant value, JsonGenerator generator, SerializationContext context) {
        generator.writeString(ApiInstantFormat.FORMATTER.format(value));
    }
}
