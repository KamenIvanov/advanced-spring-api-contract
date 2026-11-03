package com.pe.advanced.api.dto.category.assignment;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * What happened to one product in a batch assignment.
 * <p>
 * Only ASSIGNED changed anything. The other three are reasons a product was left alone, and
 * they are distinguished rather than merged into one failure because a caller does something
 * different with each: ALREADY_ASSIGNED is success by another name, PRODUCT_NOT_FOUND is a
 * bad identifier worth reporting, and PRODUCT_ARCHIVED is a product whose owner retired it.
 */
@Schema(description = "What happened to one product in a batch assignment.")
public enum ProductAssignmentOutcomeDto {

    ASSIGNED,
    ALREADY_ASSIGNED,
    PRODUCT_NOT_FOUND,
    PRODUCT_ARCHIVED
}
