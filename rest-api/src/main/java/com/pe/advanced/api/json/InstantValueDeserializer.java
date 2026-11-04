package com.pe.advanced.api.json;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

/**
 * Reads an instant.
 * <p>
 * Deliberately more forgiving than the serializer. What this API writes is one exact format,
 * and what it accepts is any ISO-8601 instant, with or without a fractional part and with an
 * offset other than UTC. A client that sends {@code 2026-11-03T16:15:00+02:00} meant a moment
 * in time and said so correctly; rejecting it would be pedantry rather than a contract.
 * Strict on the way out, tolerant on the way in.
 */
public class InstantValueDeserializer extends ValueDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser parser, DeserializationContext context) {
        final var value = parser.getString();
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Instant.parse(value);
        } catch (final DateTimeParseException instantFormatRejected) {
            try {
                return OffsetDateTime.parse(value).toInstant();
            } catch (final DateTimeParseException offsetFormatRejected) {
                throw new IllegalArgumentException("Not an ISO-8601 instant: '" + value + "'", offsetFormatRejected);
            }
        }
    }
}
