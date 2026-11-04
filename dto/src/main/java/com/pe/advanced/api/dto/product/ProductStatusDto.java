package com.pe.advanced.api.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The lifecycle a product moves through. The transitions are a server-side rule rather than
 * something a client can read off this list, which is why a status change is an operation of
 * its own and not a field a client may set.
 */
@Schema(description = "The product's status.")
public enum ProductStatusDto {

    DRAFT,
    ACTIVE,
    OUT_OF_STOCK,
    ARCHIVED
}
