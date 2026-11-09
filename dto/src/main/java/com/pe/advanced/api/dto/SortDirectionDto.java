package com.pe.advanced.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The direction in which a result is sorted.")
public enum SortDirectionDto {

    ASC,
    DESC
}
