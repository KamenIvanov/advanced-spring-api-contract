package com.pe.advanced.api.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The fields a product listing may be sorted by. An enum rather than a free string, because
 * a sort field reaches an index: anything outside this list is either an unindexed column or
 * a column that does not exist, and both answers belong in the contract rather than in a 400.
 */
@Schema(description = "The field a product listing is sorted by.")
public enum ProductSortOptionDto {

    NAME,
    PRICE,
    CREATED_AT
}
