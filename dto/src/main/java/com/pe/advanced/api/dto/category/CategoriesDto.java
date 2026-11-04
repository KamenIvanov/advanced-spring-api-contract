package com.pe.advanced.api.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "A page of categories, with their full data.")
public class CategoriesDto extends PageDto {

    @Schema(description = "The categories in this page.")
    @JsonProperty(value = "categories")
    private List<CategoryDto> categories = new ArrayList<>();

    public CategoriesDto() {
        // POJO
    }

    public CategoriesDto(Integer totalPages, Long totalHits, List<CategoryDto> categories) {
        super(totalPages, totalHits);
        this.categories = categories;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }
}
