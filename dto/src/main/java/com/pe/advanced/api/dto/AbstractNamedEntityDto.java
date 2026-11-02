package com.pe.advanced.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Most things in this API have a name a person reads. Keeping it one level above the concrete
 * types means a client that only needs to list and label things can work against this type
 * without knowing what it is listing.
 */
public abstract class AbstractNamedEntityDto extends AbstractEntityDto {

    @Schema(description = "The entity's name.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "name")
    private String name;

    protected AbstractNamedEntityDto() {
        // POJO
    }

    protected AbstractNamedEntityDto(UUID id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
