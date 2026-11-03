package com.pe.advanced.api.json;

import com.pe.advanced.api.dto.AbstractDto;

/**
 * Reads a contract type from the bytes this API accepts.
 * <p>
 * The target type is an argument rather than something discovered from the payload, and that
 * is the whole design. A generic deserializer that reads a type name out of the JSON needs the
 * type name to be in the JSON, which means every response carries a Java class name and the
 * published schema either documents it or lies by omission. Over HTTP nobody needs it: a
 * caller knows what it asked for, and on the server the framework deserializes into the
 * declared parameter type. The payload describes a value; the caller supplies the type.
 */
public final class ApiDeserializer {

    public static final ApiDeserializer instance = new ApiDeserializer();

    private ApiDeserializer() {
        // Singleton
    }

    public <T extends AbstractDto> T process(String payload, Class<T> type) {
        if (payload == null || payload.isBlank()) {
            return null;
        }
        return JsonMapperSingleton.instance.getJsonMapper().readValue(payload, type);
    }
}
