package com.pe.advanced.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

/**
 * Everything the server has created and will not change: the identifier it assigned and the
 * moment it did so. Both are read-only from a client's point of view, which is why the
 * types a client sends do not extend this one.
 */
public abstract class AbstractCreatableEntityDto extends AbstractDto {

    @Schema(description = "The entity's identification, assigned by the server.", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(value = "id")
    private UUID id;

    @Schema(description = "The exact time when the entity was created.", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(value = "createdAt")
    private Instant createdAt;

    protected AbstractCreatableEntityDto() {
        // POJO
    }

    protected AbstractCreatableEntityDto(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
