package com.pe.advanced.api.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The field a category listing is sorted by.")
public enum CategorySortOptionDto {

    NAME,
    CREATED_AT
}
