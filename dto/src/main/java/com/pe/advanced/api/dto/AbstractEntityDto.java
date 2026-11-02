package com.pe.advanced.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

/**
 * An entity that can be changed after it exists, which is what makes it the first type here
 * with a version.
 * <p>
 * The version is the concurrency token from the schema, carried out to the client. It arrives
 * read-only: the client is told which version it is holding, and how it hands that number
 * back on an update is a question about the request rather than about this type.
 */
public abstract class AbstractEntityDto extends AbstractCreatableEntityDto {

    @Schema(description = "The exact time when the entity was last updated.", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(value = "updatedAt")
    private Instant updatedAt;

    @Schema(description = "The version of the entity being returned. Supply it when updating "
            + "this entity, so that a change made by somebody else in the meantime is "
            + "rejected rather than silently overwritten.",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonProperty(value = "version")
    private Integer version;

    protected AbstractEntityDto() {
        // POJO
    }

    protected AbstractEntityDto(UUID id) {
        super(id);
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
