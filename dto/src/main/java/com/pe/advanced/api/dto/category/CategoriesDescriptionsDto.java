package com.pe.advanced.api.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "A page of categories, as shown to anyone browsing the catalogue.")
public class CategoriesDescriptionsDto extends PageDto {

    @Schema(description = "The categories in this page.")
    @JsonProperty(value = "categories")
    private List<CategoryDescriptionDto> categories = new ArrayList<>();

    public CategoriesDescriptionsDto() {
        // POJO
    }

    public CategoriesDescriptionsDto(Integer totalPages, Long totalHits, List<CategoryDescriptionDto> categories) {
        super(totalPages, totalHits);
        this.categories = categories;
    }

    public List<CategoryDescriptionDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDescriptionDto> categories) {
        this.categories = categories;
    }
}
