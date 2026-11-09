package com.pe.advanced.api.json;

import tools.jackson.core.SerializableString;
import tools.jackson.core.io.CharacterEscapes;
import tools.jackson.core.io.SerializedString;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * The one mapper this API is described by.
 * <p>
 * It belongs in the contract rather than in the service because the way a value appears on the
 * wire is a fact about the API, not a detail of whoever implements it. A specification can say
 * that a price is a string, only this configuration makes it one. Publishing the mapper
 * alongside the interfaces means a consumer can read the format from the same artifact that
 * declares the operations, and the integration tests that exercise it are testing the
 * published contract rather than a local approximation of it.
 * <p>
 * That only holds while the service uses this mapper for its HTTP layer too. A service that
 * lets the framework configure its own mapper has two configurations, the tests prove one of
 * them, and the difference shows up on the wire - which is why the application module takes
 * its mapper bean from here instead of leaving it to autoconfiguration.
 */
public final class JsonMapperSingleton {

    /**
     * U+2028 and U+2029 are valid in a JSON string and are line terminators in older
     * JavaScript. A payload containing one parses as JSON and then breaks when a consumer
     * embeds it in a script, which is a bug that belongs to whoever serialized it.
     */
    private static final char[] CHARS_TO_ESCAPE = { ' ', ' ' };

    private static final JsonMapper JSON_MAPPER = build();

    public static final JsonMapperSingleton instance = new JsonMapperSingleton();

    private JsonMapperSingleton() {
        // Singleton
    }

    public JsonMapper getJsonMapper() {
        return JSON_MAPPER;
    }

    private static JsonMapper build() {
        final var factory = JsonFactory.builder()
                .characterEscapes(new JavaScriptSafeEscapes())
                .build();

        final var module = new SimpleModule("advanced-api-contract");

        // A price is money, and money in a JSON number is a double in every browser that
        // reads it. Writing it as a string keeps the scale the server decided on.
        module.addSerializer(BigDecimal.class, ToStringSerializer.instance);

        // java.time is built into databind in Jackson 3, and its output is correct ISO-8601
        // rather than one fixed shape. This API publishes one shape.
        module.addSerializer(Instant.class, new InstantValueSerializer());
        module.addDeserializer(Instant.class, new InstantValueDeserializer());

        return JsonMapper.builder(factory)
                .addModule(module)
                .build();
    }

    private static final class JavaScriptSafeEscapes extends CharacterEscapes {

        private final Map<Integer, String> customEscapes = createCustomEscapes();

        @Override
        public int[] getEscapeCodesForAscii() {
            return CharacterEscapes.standardAsciiEscapesForJSON();
        }

        @Override
        public SerializableString getEscapeSequence(int ch) {
            final var escaped = customEscapes.get(ch);
            return escaped == null ? null : new SerializedString(escaped);
        }

        private static Map<Integer, String> createCustomEscapes() {
            final Map<Integer, String> escapes = new HashMap<>();
            for (final char character : CHARS_TO_ESCAPE) {
                escapes.put((int) character, String.format("\\u%04X", (int) character));
            }
            return escapes;
        }
    }
}
