package com.pe.advanced.api.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractEntityDto;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A category as the people who curate the taxonomy see it, with the audit trail and the
 * version.
 */
@Schema(description = "The category's full data.")
public class CategoryDto extends AbstractEntityDto {

    @Schema(description = "The category's name. Unique across all categories.")
    @JsonProperty(value = "name")
    private String name;

    @Schema(description = "What the category is for.")
    @JsonProperty(value = "description")
    private String description;

    @Schema(description = "The category's current status.")
    @JsonProperty(value = "status")
    private CategoryStatusDto status;

    public CategoryDto() {
        // POJO
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryStatusDto getStatus() {
        return status;
    }

    public void setStatus(CategoryStatusDto status) {
        this.status = status;
    }
}
