package com.pe.advanced.api.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractCreatableEntityDto;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A category as a visitor sees it while browsing: enough to label a section and follow it.
 * No status, because only an active category is ever returned here, and a field whose value
 * is always the same is noise rather than information.
 */
@Schema(description = "A category, as shown to anyone browsing the catalogue.")
public class CategoryDescriptionDto extends AbstractCreatableEntityDto {

    @Schema(description = "The category's name.")
    @JsonProperty(value = "name")
    private String name;

    @Schema(description = "What the category is for.")
    @JsonProperty(value = "description")
    private String description;

    public CategoryDescriptionDto() {
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
