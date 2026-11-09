package com.pe.advanced.api.dto.category.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * The outcome for one product in a batch assignment.
 */
@Schema(description = "The outcome of assigning one product to a category.")
public class ProductAssignmentResultDto extends AbstractDto {

    @Schema(description = "The product this outcome is about.")
    @JsonProperty(value = "productId")
    private UUID productId;

    @Schema(description = "What happened to it.")
    @JsonProperty(value = "outcome")
    private ProductAssignmentOutcomeDto outcome;

    @Schema(description = "The assignment that was created. Present only when the outcome is ASSIGNED.")
    @JsonProperty(value = "assignmentId")
    private UUID assignmentId;

    public ProductAssignmentResultDto() {
        // POJO
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public ProductAssignmentOutcomeDto getOutcome() {
        return outcome;
    }

    public void setOutcome(ProductAssignmentOutcomeDto outcome) {
        this.outcome = outcome;
    }

    public UUID getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(UUID assignmentId) {
        this.assignmentId = assignmentId;
    }
}
