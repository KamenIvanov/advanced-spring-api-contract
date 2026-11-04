package com.pe.advanced.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

/**
 * An entity a client may change, which is what makes it the first type here with a version.
 * <p>
 * The version is the concurrency token from the schema, carried out to the caller, and it is
 * the reason this type exists separately from {@link AbstractCreatableEntityDto}: a summary
 * handed to an anonymous reader has nothing to do with a version, because that reader cannot
 * write. The concept arrives with the operation that needs it, exactly as it did in the
 * entity hierarchy.
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
