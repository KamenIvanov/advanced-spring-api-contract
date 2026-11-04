package com.pe.advanced.api.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A category's lifecycle. A category is retired by archiving rather than by deletion, which
 * is why there is no transition out of ARCHIVED and no delete that takes assignments with it.
 */
@Schema(description = "The category's status.")
public enum CategoryStatusDto {

    INACTIVE,
    ACTIVE,
    ARCHIVED
}
