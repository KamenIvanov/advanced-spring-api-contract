package com.pe.advanced.api.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractDto;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * What a caller sends to change a category. Same fields as creation here, and that is a
 * coincidence rather than a reason to share a type: the two will diverge the first time
 * something becomes settable only at creation, and a shared type would then need a comment
 * instead of a compiler.
 */
@Schema(description = "The data required to update a category.")
public class UpdateCategoryDto extends AbstractDto {

    @Schema(description = "The category's name. Unique across all categories.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "name")
    private String name;

    @Schema(description = "What the category is for, shown to anyone browsing it.")
    @JsonProperty(value = "description")
    private String description;

    public UpdateCategoryDto() {
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
}
