package com.pe.advanced.api.json;

import com.pe.advanced.api.dto.AbstractDto;

/**
 * Turns a contract type into the exact bytes this API publishes.
 */
public final class ApiSerializer {

    public static final ApiSerializer instance = new ApiSerializer();

    private ApiSerializer() {
        // Singleton
    }

    public String process(AbstractDto dto) {
        if (dto == null) {
            return null;
        }
        return JsonMapperSingleton.instance.getJsonMapper().writeValueAsString(dto);
    }
}
