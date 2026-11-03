package com.pe.advanced.api.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractDto;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * What a caller sends to create a category. No status: a category starts inactive by a rule
 * the server owns, and becomes visible only when somebody decides it should.
 */
@Schema(description = "The data required to create a category.")
public class NewCategoryDto extends AbstractDto {

    @Schema(description = "The category's name. Unique across all categories.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "name")
    private String name;

    @Schema(description = "What the category is for, shown to anyone browsing it.")
    @JsonProperty(value = "description")
    private String description;

    public NewCategoryDto() {
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
